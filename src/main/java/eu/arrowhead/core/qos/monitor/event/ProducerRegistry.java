/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.event;

import eu.arrowhead.core.qos.monitor.event.model.ProducerType;
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

    private ProducerType producerType;
    private static final Logger LOG = Logger.getLogger(ProducerRegistry.class.getName());

    public ProducerRegistry() {
        initProducerType();
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

    private ProducerType initProducerType() {
        producerType = new ProducerType();
        producerType.setUid(getProps().getProperty("producer.uid"));
        producerType.setType(getProps().getProperty("producer.type"));
        producerType.setName(getProps().getProperty("producer.name"));
        return producerType;
    }

    public int registerAsProducer() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(EventProducerConfig.getServiceURI());

        Response response;
        response = target
                .path(EventProducerConfig.getServiceRegistryAsConsumerPath())
                .path(producerType.getUid())
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(producerType));

        EventProducer.setProducerType(producerType);

        int statusCode = response.getStatus();

        client.close();

        LOG.log(Level.INFO, "Response status received from EventHandler: {0}", statusCode);

        return statusCode;
    }
}
