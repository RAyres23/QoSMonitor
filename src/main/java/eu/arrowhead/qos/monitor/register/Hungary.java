/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.qos.monitor.register;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ID0084D
 */
public class Hungary implements ServiceRegister {

    private static Properties props;

    /**
     * Gets the properties file named 'registry.properties'.
     *
     * @return the Properties from properties file 'registry.properties'
     */
    public synchronized Properties getProps() {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return props;
    }

    @Override
    public void registerQoSMonitorService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
