package eu.arrowhead.common.listener;

import eu.arrowhead.core.qos.monitor.database.MongoDatabaseManager;
import eu.arrowhead.core.qos.monitor.event.EventProducerConfig;
import eu.arrowhead.core.qos.monitor.event.ProducerRegistry;
import eu.arrowhead.core.qos.monitor.registry.Register;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextClass implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ServletContextClass.class.getName());
    private final Register register = new Register();

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        try {
            String logName = "QoSMonitor"
                    .concat(Calendar.getInstance().get(Calendar.YEAR) + "")
                    .concat(Calendar.getInstance().get(Calendar.MONTH) + "")
                    .concat(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "")
                    .concat(".log");
            Logger.getGlobal().addHandler(new FileHandler(logName, true));
        } catch (IOException | SecurityException ex) {
            LOG.log(Level.WARNING, "Failed in adding handler to LOG facility", ex);
        }

        LOG.log(Level.INFO, "[QoSMonitor] Servlet deployed.");

        LOG.log(Level.INFO, "Working Directory = {0}", System.getProperty("user.dir"));

        //Service Registry
        register.registerAll();

        // Load EventProducer configurations and register in EventHandler
        EventProducerConfig.loadConfigurations();
        new ProducerRegistry().registerAsProducer();

        MongoDatabaseManager.getInstance().startManager();

        LOG.info("Setup completed.");

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("[QoSMonitor] Destroying servlet.");

        //Stop MongoDB
        MongoDatabaseManager.getInstance().stopManager();

        //Unregister from service registry
        register.unregisterAll();

        System.out.println("[QoSMonitor] Servlet destroyed.");
    }
}
