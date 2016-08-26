/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.event;

import eu.arrowhead.core.qos.monitor.event.model.Producer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Renato
 */
public class ProducerRegistry {

    private Producer producer;
    private static final Logger LOG = Logger.getLogger(ProducerRegistry.class.getName());

    public ProducerRegistry() {
        initProducer();
    }

    /**
     * Gets the properties file named 'monitor.properties'.
     *
     * @return the Properties from properties file 'monitor.properties'
     */
    private synchronized Properties getProps() {
        Properties props = null;
        try {
            props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("monitor.properties");
            if (inputStream != null) {
                props.load(inputStream);
                inputStream.close();
            } else {
                throw new FileNotFoundException("Properties file 'eventhandler.properties' not found in the classpath");
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        return props;
    }

    private Producer initProducer() {
        producer = new Producer();
        producer.setUid(getProps().getProperty("producer.uid"));
        producer.setType(getProps().getProperty("producer.type"));
        producer.setName(getProps().getProperty("producer.name"));
        //FIXME
        EventProducer.setProducer(producer.getUid());
        return producer;
    }

    public int registerAsProducer() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(EventProducerConfig.getServiceURI());

        Response response;
        response = target
                .path(EventProducerConfig.getServiceRegistryAsProducerPath())
                .path("producer")
                .path(producer.getUid())
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(producer));

        EventProducer.setProducer(producer.getUid());

        int statusCode = response.getStatus();

        client.close();

        LOG.log(Level.INFO, "Response status received from EventHandler: {0}", statusCode);

        return statusCode;
    }
}
