/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.event;

import eu.arrowhead.core.qos.monitor.event.model.EventType;
import eu.arrowhead.core.qos.monitor.event.model.ProducerType;
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
 * @author Renato Ayres
 */
public class EventProducer {

    private final Client client;
    private final WebTarget target;
    private final EventType event;
    private static ProducerType producerType;
    private static final Logger LOG = Logger.getLogger(EventProducer.class.getName());

    public EventProducer(EventType event) {
        client = ClientBuilder.newClient();
        target = client.target(EventProducerConfig.getServiceURI());
        this.event = event;
    }

//    /**
//     * Gets the properties file named 'monitor.properties'.
//     *
//     * @return the Properties from properties file 'monitor.properties'
//     */
//    private synchronized Properties getProps() {
//        Properties props = null;
//        try {
//            props = new Properties();
//            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("monitor.properties");
//            if (inputStream != null) {
//                props.load(inputStream);
//                inputStream.close();
//            } else {
//                throw new FileNotFoundException("Properties file 'eventhandler.properties' not found in the classpath");
//            }
//        } catch (FileNotFoundException ex) {
//            LOG.log(Level.SEVERE, ex.getMessage());
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, ex.getMessage());
//        }
//        return props;
//    }
    public Client getClient() {
        return client;
    }

    public WebTarget getTarget() {
        return target;
    }

    public EventType getEventType() {
        return event;
    }

    public static ProducerType getProducerType() {
        return producerType;
    }

    public static void setProducerType(ProducerType producerType) {
        EventProducer.producerType = producerType;
    }

    public int publishEvent() {
        LOG.log(Level.INFO, "Sending event to EventHandler. Service URI: {0}\nPublish URI: {1}", new Object[]{target.getUri(), EventProducerConfig.getServicePublishEventPath()});

        Response response = getTarget()
                .path(EventProducerConfig.getServicePublishEventPath())
                .path(getProducerType().getUid())
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(event));

        int statusCode = response.getStatus();

        client.close();

        LOG.log(Level.INFO, "Response status received from EventHandler: {0}", statusCode);

        return statusCode;
    }
}
