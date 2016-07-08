package eu.arrowhead.common.model.messages;

import eu.arrowhead.common.model.ArrowheadSystem;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Message used to add a new monitor log.
 *
 * @author Renato Ayres
 */
@XmlRootElement
public class QoSMonitorLog {

    private String type;
    private ArrowheadSystem provider;
    private ArrowheadSystem consumer;
    private Map<String, String> parameters;
    private Long timestamp;

    /**
     * Creates a new instance with no parameters initialized.
     */
    public QoSMonitorLog() {
    }

    /**
     * Creates a new instance with the given monitor type, service provider,
     * service consumer, monitor parameters, and monitor timestamp.
     *
     * @param type the monitor type
     * @param provider the service provider
     * @param consumer the service consumer
     * @param parameters the monitor parameters. It works by getting the value
     * of the parameter (key) e.g. key=bandwidth, value=100
     * @param timestamp the monitor timestamp
     */
    public QoSMonitorLog(String type, ArrowheadSystem provider,
            ArrowheadSystem consumer, Map<String, String> parameters, Long timestamp) {
        this.type = type;
        this.provider = provider;
        this.consumer = consumer;
        this.parameters = parameters;
        this.timestamp = timestamp;
    }

    /**
     * Gets the monitor type
     *
     * @return the monitor type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the monitor type
     *
     * @param type the monitor type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the service provider
     *
     * @return the service provider
     */
    public ArrowheadSystem getProvider() {
        return provider;
    }

    /**
     * Sets the service provider
     *
     * @param provider the service provider
     */
    public void setProvider(ArrowheadSystem provider) {
        this.provider = provider;
    }

    /**
     * Gets the service consumer
     *
     * @return the service consumer
     */
    public ArrowheadSystem getConsumer() {
        return consumer;
    }

    /**
     * Sets the service consumer
     *
     * @param consumer the service consumer
     */
    public void setConsumer(ArrowheadSystem consumer) {
        this.consumer = consumer;
    }

    /**
     * Gets the monitor parameters
     *
     * @return the monitor parameters
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Sets the monitor parameters.
     *
     * @param parameters the monitor parameters
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets the monitor timestamp
     *
     * @return the monitor timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the monitor timestamp
     *
     * @param timestamp the monitor timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
