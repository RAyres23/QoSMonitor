/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.registry;

import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.common.model.ServiceMetadata;
import eu.arrowhead.common.model.messages.ServiceRegistryEntry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author ID0084D
 */
public class Hungary implements ServiceRegister {

    private Properties props;
    private static final Logger LOG = Logger.getLogger(Hungary.class.getName());

    /**
     * Gets the properties file named 'registry.properties'.
     *
     * @return the Properties from properties file 'registry.properties'
     */
    private synchronized Properties getProps() {
        try {
            if (props == null) {
                props = new Properties();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hungary.properties");
                if (inputStream != null) {
                    props.load(inputStream);
                    inputStream.close();
                } else {
                    throw new FileNotFoundException("Properties file 'hungary.properties' not found in the classpath");
                }
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
        return props;
    }

    @Override
    public boolean registerQoSMonitorService() {

        ServiceRegistryEntry entry = createServiceRegistryEntry();

        Properties temp = getProps();

        String registryURI = temp.getProperty("serviceregistry.uri");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(registryURI);

        String serviceGroup = temp.getProperty("monitor.service.group");
        String serviceName = temp.getProperty("monitor.service.name");
        String interfaces = temp.getProperty("monitor.service.interfaces");

        Response response = target
                .path(serviceGroup)
                .path(serviceName)
                .path(interfaces)
                .request()
                .header("Content-Type", "application/json")
                .post(Entity.json(entry));

        int statusCode = response.getStatus();
        LOG.log(Level.INFO, "ServiceRegistry response: {0}", statusCode);

        client.close();

        return statusCode > 199 && statusCode < 300;
    }

    @Override
    public boolean unregisterQoSMonitorService() {
        ServiceRegistryEntry entry = createServiceRegistryEntry();

        Properties temp = getProps();

        String registryURI = temp.getProperty("serviceregistry.uri");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(registryURI);

        String serviceGroup = temp.getProperty("monitor.service.group");
        String serviceName = temp.getProperty("monitor.service.name");
        String interfaces = temp.getProperty("monitor.service.interfaces");

        Response response = target
                .path(serviceGroup)
                .path(serviceName)
                .path(interfaces)
                .request()
                .header("Content-Type", "application/json")
                .put(Entity.json(entry));

        int statusCode = response.getStatus();
        LOG.log(Level.INFO, "ServiceRegistry response: {0}", statusCode);

        client.close();

        return statusCode > 199 && statusCode < 300;
    }

    private ServiceRegistryEntry createServiceRegistryEntry() {
        Properties temp = getProps();

        ArrowheadSystem provider = createArrowheadMonitorSystem();

        String serviceURI = temp.getProperty("monitor.service.uri");
        List<ServiceMetadata> serviceMetadata = new ArrayList<>();

        String[] keys = temp.getProperty("monitor.service.metadata.key").split(",");
        Queue<String> values = new ArrayDeque<>(Arrays.asList(temp.getProperty("monitor.service.metadata.value").split(",")));

        for (String key : keys) {
            serviceMetadata.add(new ServiceMetadata(key, values.remove()));
        }

        String tsig_key = temp.getProperty("serviceregistry.tsig");
        String version = temp.getProperty("monitor.service.version");

        return new ServiceRegistryEntry(provider, serviceURI, serviceMetadata, tsig_key, version);
    }

    private ArrowheadSystem createArrowheadMonitorSystem() {
        Properties temp = getProps();

        String systemGroup = temp.getProperty("monitor.system.group");
        String systemName = temp.getProperty("monitor.system.name");

        String address;
        try {
            address = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            LOG.log(Level.WARNING, "Not able to get local host from system. Using default value in hungary.properties file");
            address = temp.getProperty("monitor.system.address");
        }

        String port = temp.getProperty("monitor.system.port");
        String authenticationInfo = temp.getProperty("monitor.system.authenticationInfo");

        return new ArrowheadSystem(systemGroup, systemName, address, port, authenticationInfo);
    }

}
