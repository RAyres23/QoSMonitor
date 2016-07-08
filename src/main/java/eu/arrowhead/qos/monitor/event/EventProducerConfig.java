/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.qos.monitor.event;

import eu.arrowhead.common.model.ArrowheadService;
import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.common.model.messages.OrchestrationForm;
import eu.arrowhead.common.model.messages.OrchestrationResponse;
import eu.arrowhead.common.model.messages.ServiceRequestForm;
import eu.arrowhead.qos.factories.Value;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * @author ID0084D
 */
public class EventProducerConfig {

    private static EventProducerConfig instance;
    private static URI serviceURI;
    private static String serviceRegistryAsConsumerPath;
    private static String servicePublishEventPath;
    private static String serviceHistoricalsPath;
    private Properties props;
    private static final Logger LOG = Logger.getLogger(EventProducerConfig.class.getName());

    public static synchronized EventProducerConfig getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    private static void initInstance() {
        if (instance == null) {
            instance = new EventProducerConfig();
        }
    }

    private EventProducerConfig() {
        initConfig();
    }

    public static void loadConfigurations() {
        initInstance();
    }

    public void reloadConfigurations() {
        EventProducerConfig.serviceURI = null;
        EventProducerConfig.serviceRegistryAsConsumerPath = null;
        EventProducerConfig.servicePublishEventPath = null;
        EventProducerConfig.serviceHistoricalsPath = null;
        this.props = null;
        initConfig();
    }

    /**
     * Gets the properties file named 'eventhandler.properties'.
     *
     * @return the Properties from properties file 'eventhandler.properties'
     */
    private synchronized Properties getProps() {
        try {
            if (props == null) {
                props = new Properties();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("eventhandler.properties");
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

    private void initConfig() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getProps().getProperty("orchestrator.orchestration.uri"));

        ArrowheadService requestedService = getRequestedService();
        ArrowheadSystem requesterSystem = getRequesterSystem();

        ServiceRequestForm form = new ServiceRequestForm(requestedService, new HashMap<String, Value>(), requesterSystem);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(form, MediaType.APPLICATION_JSON));

        if (response.getStatusInfo().getStatusCode() != Response.Status.OK.getStatusCode()) {
            String msg = response.getStatusInfo().getReasonPhrase();
            client.close();
            throw new RuntimeException(msg);
        }

        OrchestrationForm orchForm = response.readEntity(OrchestrationResponse.class).getResponse().get(0);

        client.close();

        try {
//            setServiceURI(new URI("http://" + orchForm.getProvider().getIPAddress() + "+" + orchForm.getProvider().getPort() + "/eventhandler"));
            setServiceURI(new URI("http",
                    null,
                    orchForm.getProvider().getIPAddress(),
                    Integer.parseInt(orchForm.getProvider().getPort()),
                    orchForm.getServiceURI(),
                    null,
                    null));
        } catch (URISyntaxException ex) {
            String exMsg = "Failure in construction of service URI";
            LOG.log(Level.SEVERE, exMsg);
            throw new RuntimeException(exMsg);
        }
        setServiceRegistryAsConsumerPath(getProps().getProperty("eventhandler.registrypath"));
        setServicePublishEventPath(getProps().getProperty("eventhandler.publishpath"));
        setServiceHistoricalsPath(getProps().getProperty("eventhandler.historicalspath"));
    }

    private ArrowheadService getRequestedService() {
        ArrowheadService requestedService = new ArrowheadService();
        ArrayList<String> interfaces = new ArrayList<>();

        requestedService.setServiceGroup(getProps().getProperty("eventhandler.servicegroup"));
        requestedService.setServiceDefinition(getProps().getProperty("eventhandler.servicedefinition"));
        requestedService.setMetaData("Dummy metadata");
        interfaces.add("RESTJSON");
        requestedService.setInterfaces(interfaces);

        return requestedService;
    }

    private static ArrowheadSystem getRequesterSystem() {
        ArrowheadSystem system = null;
        try {
            system = new ArrowheadSystem("CORE", "qosmonitor", Inet4Address.getLocalHost().getHostAddress(), "8080", "qosmonitorcert");
        } catch (UnknownHostException ex) {
            Logger.getLogger(EventProducerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return system;
    }

    public static URI getServiceURI() {
        return serviceURI;
    }

    private static void setServiceURI(URI serviceURI) {
        EventProducerConfig.serviceURI = serviceURI;
    }

    public static String getServiceRegistryAsConsumerPath() {
        return serviceRegistryAsConsumerPath;
    }

    private static void setServiceRegistryAsConsumerPath(String serviceRegistryAsConsumerPath) {
        EventProducerConfig.serviceRegistryAsConsumerPath = serviceRegistryAsConsumerPath;
    }

    public static String getServicePublishEventPath() {
        return servicePublishEventPath;
    }

    private static void setServicePublishEventPath(String servicePublishEventPath) {
        EventProducerConfig.servicePublishEventPath = servicePublishEventPath;
    }

    public static String getServiceHistoricalsPath() {
        return serviceHistoricalsPath;
    }

    private static void setServiceHistoricalsPath(String serviceHistoricalsPath) {
        EventProducerConfig.serviceHistoricalsPath = serviceHistoricalsPath;
    }
}
