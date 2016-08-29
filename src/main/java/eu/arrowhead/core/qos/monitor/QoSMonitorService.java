package eu.arrowhead.core.qos.monitor;

import eu.arrowhead.common.exception.InvalidMonitorTypeException;
import eu.arrowhead.common.exception.MonitorRuleNotFoundException;
import eu.arrowhead.common.exception.NoMonitorParametersException;
import eu.arrowhead.common.model.messages.QoSMonitorAddRule;
import eu.arrowhead.common.model.messages.QoSMonitorLog;
import eu.arrowhead.common.model.messages.QoSMonitorRemoveRule;
import eu.arrowhead.common.model.messages.ServiceError;
import eu.arrowhead.core.qos.monitor.database.MongoDatabaseManager;
import eu.arrowhead.core.qos.monitor.database.MonitorLog;
import eu.arrowhead.core.qos.monitor.database.MonitorRule;
import eu.arrowhead.core.qos.monitor.event.EventProducerConfig;
import eu.arrowhead.core.qos.monitor.event.ProducerRegistry;
import eu.arrowhead.core.qos.monitor.event.SLAVerification;
import eu.arrowhead.core.qos.monitor.event.model.Event;
import eu.arrowhead.core.qos.monitor.registry.ServiceRegister;
import eu.arrowhead.core.qos.monitor.type.Monitor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the QoSMonitor Service class. It takes care of all the aspects of the
 * QoSMonitor Service.
 *
 * @author Renato Ayres
 */
public class QoSMonitorService {

    private static final String MONITOR_TYPE_PACKAGE = "eu.arrowhead.core.qos.monitor.type.";
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();
    private static final Logger LOG = Logger.getLogger(QoSMonitorService.class.getName());

    //FIXME only used in startService
    private static final List<String> REGISTERED = new ArrayList();
    //FIXME only used in startService
    private final String MONITOR_REGISTRY_PACKAGE = "eu.arrowhead.core.qos.monitor.register.";

    /**
     * A new QoSMonitorService instance with a initialized MongoDatabaseManager
     * instance.
     */
    public QoSMonitorService() {
    }

//    /**
//     * Returns an initialized instance of the MongoDatabaseManager class.
//     *
//     * @return MongoDatabaseManager static instance
//     */
//    public MongoDatabaseManager getDatabaseManager() {
//        if (database == null) {
//            database = new MongoDatabaseManager();
//        }
//        return database;
//    }
    public void startService() {
        //Register QoSMonitor service in service registry
        List<String> registries = getServiceRegistry();

        registries.stream().forEach((registry) -> {
            try {
                LOG.log(Level.INFO, "Getting " + MONITOR_REGISTRY_PACKAGE + "{0} class.", registry);
                ServiceRegister register = getRegistryClass(registry);
                LOG.log(Level.INFO, "Registering in {0} ServiceRegistry", register.getClass().getName());
                if (register.registerQoSMonitorService()) {
                    REGISTERED.add(registry);
                    LOG.log(Level.INFO, "Registry in {0} successful!", register.getClass().getName());
                } else {
                    LOG.log(Level.WARNING, "Registry in {0} unsuccessful!", register.getClass().getName());
                }
            } catch (ClassNotFoundException ex) {
                String excMessage = "Not registered in registry " + registry + ". "
                        + "Registry class " + registry + " not found. Make "
                        + "sure you have the right registry class for your "
                        + "situation and that it's available in this version "
                        + "and/or not misspelled.";
                LOG.log(Level.SEVERE, excMessage);
//                throw new RuntimeException(excMessage);
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
            }
        });

        // [PT] Starting MongoDB, loading EventProducer configurations and registering in EventHandler
        EventProducerConfig.loadConfigurations();
        new ProducerRegistry().registerAsProducer();

        MongoDatabaseManager.getInstance().startManager();
    }

    /**
     * Adds a new monitor rule to MongoDB.
     *
     * @param message message with the information needed for the rule to be
     * added
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void addMonitorRule(QoSMonitorAddRule message)
            throws InstantiationException, IllegalAccessException {

        if (message.getParameters().isEmpty()) {
            throw new NoMonitorParametersException("No monitor parameters found!");
        }

        Monitor monitor = null;
        try {
            monitor = getMonitorClass(message.getType());
        } catch (ClassNotFoundException ex) {
            String excMessage = "Type " + message.getType() + " not found. Make "
                    + "sure you have the right monitor type for your "
                    + "situation and that it's available in this version "
                    + "and/or not misspelled.";
            LOG.log(Level.SEVERE, excMessage);
            throw new InvalidMonitorTypeException(excMessage);
        }

        MonitorRule rule = monitor.filterRuleMessage(message);

        MongoDatabaseManager.getInstance().replaceRule(rule);

    }

    /**
     * Removes a monitor rule from the MongoDB.
     *
     * @param message message with information needed for the rule to be removed
     */
    public void removeMonitorRule(QoSMonitorRemoveRule message) {
        MongoDatabaseManager.getInstance().deleteRule(message.getProvider(), message.getConsumer());
    }

    /**
     * Adds a new monitor log to MongoDB.
     *
     * @param message message with the information needed for the log to be
     * added
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void addMonitorLog(QoSMonitorLog message) throws InstantiationException, IllegalAccessException {

        if (message.getParameters().isEmpty()) {
            throw new NoMonitorParametersException("No monitor parameters found!");
        }

        MonitorRule rule = MongoDatabaseManager.getInstance().findRule(message.getProvider(), message.getConsumer());

        if (rule == null) {
            throw new MonitorRuleNotFoundException("No rule created for the given services");
        }

        Monitor monitor = null;
        try {
            monitor = getMonitorClass(message.getType());
        } catch (ClassNotFoundException ex) {
            String excMessage = "Type " + message.getType() + " not found. Make "
                    + "sure you have the right monitor type for your "
                    + "situation and that it's available in this version "
                    + "and/or not misspelled.";
            LOG.log(Level.SEVERE, excMessage);
            throw new InvalidMonitorTypeException(excMessage);
        }

        if (!(message.getType().equals(rule.getType()))) {
            String excMessage = "Monitor type different from the existing rule for the given services."
                    + "\nYour type: " + message.getType() + "Existing rule type: " + rule.getType();
            LOG.log(Level.SEVERE, excMessage);
            throw new MonitorRuleNotFoundException(excMessage);
        }

        MonitorLog log = monitor.filterLogMessage(message);

        MongoDatabaseManager.getInstance().insertLog(log, message.getProvider(), message.getConsumer());

        LOG.log(Level.INFO, "Executing SLAVerification [SEPARATE THREAD]");

        SLAVerification verification = new SLAVerification(monitor, rule, log);
        EXEC.execute(verification);

    }

    /**
     * Intermediates between message and monitor type.
     *
     * @param message ServiceError message
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public void addServiceError(ServiceError message) throws InstantiationException, IllegalAccessException {

        if (message.getParameters().isEmpty()) {
            throw new NoMonitorParametersException("No parameters found in service error message!");
        }

        Monitor monitor = null;
        try {
            monitor = getMonitorClass(message.getType());
        } catch (ClassNotFoundException ex) {
            String excMessage = "Type " + message.getType() + " not found. Make "
                    + "sure you have the right monitor type for your "
                    + "situation and that it's available in this version "
                    + "and/or not misspelled.";
            LOG.log(Level.SEVERE, excMessage);
            throw new InvalidMonitorTypeException(excMessage);
        }

        Event event = monitor.addServiceError(message);
        
//        EventProducer producer = new EventProducer(event);
//        producer.publishEvent();

    }

    /**
     * Gets a list of ServiceRegistry to register the QoSMonitor service.
     *
     * @return list of ServiceRegistry
     */
    private List<String> getServiceRegistry() {
        Properties props = null;
        String[] registries;
        try {
            props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("serviceregistry.properties");
            if (inputStream != null) {
                props.load(inputStream);
                inputStream.close();
            } else {
                String exMsg = "Properties file 'serviceregistry.properties' not found in the classpath";
                LOG.log(Level.SEVERE, exMsg);
                throw new FileNotFoundException(exMsg);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        registries = props.getProperty("registry.option").split(",");
        if (registries.length == 0) {
            String exMsg = "No ServiceRegistry values found in registry.option of serviceregistry.properties file.";
            LOG.log(Level.SEVERE, exMsg);
            throw new RuntimeException(exMsg);
        }
        return Arrays.asList(registries);
    }

    /**
     * Returns an initialized instance of class, finding it by it's name.
     *
     * @param name name of the class to instantiate
     * @return a Monitor implementation
     * @throws ClassNotFoundException thrown when trying to initialize a class
     * that cannot be found. This may be due to the type in the message being
     * wrong or mistyped
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public ServiceRegister getRegistryClass(String name)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> cl;

        cl = Class.forName(MONITOR_REGISTRY_PACKAGE + name);

        ServiceRegister monitor = (ServiceRegister) cl.newInstance();

        return monitor;
    }

    /**
     * Returns an initialized instance of class, finding it by it's name.
     *
     * @param name name of the class to instantiate
     * @return a Monitor implementation
     * @throws ClassNotFoundException thrown when trying to initialize a class
     * that cannot be found. This may be due to the type in the message being
     * wrong or mistyped
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Monitor getMonitorClass(String name)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> cl;

        cl = Class.forName(MONITOR_TYPE_PACKAGE + name);

        Monitor monitor = (Monitor) cl.newInstance();

        return monitor;
    }
}
