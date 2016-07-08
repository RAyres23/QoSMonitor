package eu.arrowhead.qos.monitor;

import eu.arrowhead.common.exception.MonitorRuleNotFoundException;
import eu.arrowhead.common.exception.NoMonitorParametersException;
import eu.arrowhead.common.model.messages.QoSMonitorAddRule;
import eu.arrowhead.common.model.messages.QoSMonitorLog;
import eu.arrowhead.common.model.messages.QoSMonitorRemoveRule;
import eu.arrowhead.qos.monitor.database.MongoDatabaseManager;
import eu.arrowhead.qos.monitor.database.MonitorLog;
import eu.arrowhead.qos.monitor.database.MonitorRule;
import eu.arrowhead.qos.monitor.event.SLAVerification;
import eu.arrowhead.qos.monitor.type.Monitor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    private static Properties props;
    private static final String MONITOR_TYPE_PACKAGE = "eu.arrowhead.core.qos.monitor.type.";
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();
    private static final Logger LOG = Logger.getLogger(QoSMonitorService.class.getName());

    /**
     * A new QoSMonitorService instance with a initialized MongoDatabaseManager
     * instance.
     */
    public QoSMonitorService() {
    }

    /**
     * Gets the properties file named 'monitor.properties'.
     *
     * @return the Properties from properties file 'monitor.properties'
     */
    public synchronized Properties getProps() {
        try {
            if (props == null) {
                props = new Properties();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("monitor.properties");
                if (inputStream != null) {
                    props.load(inputStream);
                    inputStream.close();
                } else {
                    throw new FileNotFoundException("Properties file 'eventhandler.properties' not found in the classpath");
                }
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        return props;
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
    /**
     * Adds a new monitor rule to MongoDB.
     *
     * @param message message with the information needed for the rule to be
     * added
     * @throws ClassNotFoundException thrown when trying to initialize a class
     * that cannot be found. This may be due to the type in the message being
     * wrong or mistyped
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void addMonitorRule(QoSMonitorAddRule message)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        if (message.getParameters().isEmpty()) {
            throw new NoMonitorParametersException("No monitor parameters found!");
        }

        Monitor monitor = getMonitorClass(message.getType());

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
     * @throws ClassNotFoundException thrown when trying to initialize a class
     * that cannot be found. This may be due to the type in the message being
     * wrong or mistyped
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void addMonitorLog(QoSMonitorLog message) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        if (message.getParameters().isEmpty()) {
            throw new NoMonitorParametersException("No monitor parameters found!");
        }

        MonitorRule rule = MongoDatabaseManager.getInstance().findRule(message.getProvider(), message.getConsumer());

        if (rule == null) {
            throw new MonitorRuleNotFoundException("No rule created for the given services");
        }

        Monitor monitor = getMonitorClass(message.getType());

        if (!(message.getType().equals(message.getType()))) {
            String excMessage = "Monitor type different from the existing rule for the given services."
                    + "\nYour type: " + message.getType() + "Existing rule type: " + rule.getType();
            LOG.log(Level.WARNING, excMessage);
            throw new MonitorRuleNotFoundException(excMessage);
        }

        MonitorLog log = monitor.filterLogMessage(message);

        MongoDatabaseManager.getInstance().insertLog(log, message.getProvider(), message.getConsumer());

        LOG.log(Level.INFO, "Executing SLAVerification [SEPARATE THREAD]");

        EXEC.execute(new SLAVerification(monitor, rule, log));

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
