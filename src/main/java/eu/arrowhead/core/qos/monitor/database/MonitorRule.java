package eu.arrowhead.core.qos.monitor.database;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * A representation of a MonitorRule for MongoDB.
 *
 * @author Renato Ayres
 */
public class MonitorRule {

    private final ObjectId id;
    private String protocol;
    private String providerSystemName;
    private String providerSystemGroup;
    private String consumerSystemName;
    private String consumerSystemGroup;
    private Map<String, String> parameters;
    private boolean softRealTime;

    /**
     * Creates a new instance with a generated id.
     */
    public MonitorRule() {
        id = new ObjectId();
    }

    /**
     * Creates a new instance with a generated id, using the given provider
     * system name, provider system group, consumer system name, consumer system
     * group, monitor protocol, and monitor parameters and a time related
     * clause.
     *
     * @param type the monitor protocol
     * @param providerSystemName the provider system name
     * @param providerSystemGroup the provider system group
     * @param consumerSystemName the consumer system name
     * @param consumerSystemGroup the consumer system group
     * @param parameters the monitor parameters
     * @param softRealTime the time related clause
     */
    public MonitorRule(String type,
            String providerSystemName, String providerSystemGroup,
            String consumerSystemName, String consumerSystemGroup,
            Map<String, String> parameters, boolean softRealTime) {
        this(new ObjectId(), type,
                providerSystemName, providerSystemGroup,
                consumerSystemName, consumerSystemGroup,
                parameters, softRealTime);
    }

    /**
     * Creates a new instance using the given id, provider system name, provider
     * system group, consumer system name, consumer system group, monitor
     * protocol, and monitor parameters and a time related clause.
     *
     * @param id the id
     * @param type the monitor protocol
     * @param providerSystemName the provider system definition
     * @param providerSystemGroup the provider system group
     * @param consumerSystemName the consumer system definition
     * @param consumerSystemGroup the consumer system group
     * @param parameters the monitor parameters
     * @param softRealTime the time related clause
     */
    public MonitorRule(final ObjectId id, String type,
            String providerSystemName, String providerSystemGroup,
            String consumerSystemName, String consumerSystemGroup,
            Map<String, String> parameters, boolean softRealTime) {
        this.id = id;
        this.protocol = type;
        this.providerSystemName = providerSystemName;
        this.providerSystemGroup = providerSystemGroup;
        this.consumerSystemName = consumerSystemName;
        this.consumerSystemGroup = consumerSystemGroup;
        this.parameters = parameters;
        this.softRealTime = softRealTime;
    }

    /**
     * Gets the rule id.
     *
     * @return the rule id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Gets the monitor protocol.
     *
     * @return the monitor protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the monitor protocol.
     *
     * @param protocol the monitor protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the provider system name.
     *
     * @return the provider system name
     */
    public String getProviderSystemName() {
        return providerSystemName;
    }

    /**
     * Sets the provider system name.
     *
     * @param providerSystemName the provider system name
     */
    public void setProviderSystemName(String providerSystemName) {
        this.providerSystemName = providerSystemName;
    }

    /**
     * Gets the provider system group.
     *
     * @return the provider system group
     */
    public String getProviderSystemGroup() {
        return providerSystemGroup;
    }

    /**
     * Sets the provider system group.
     *
     * @param providerSystemGroup the provider system group
     */
    public void setProviderSystemGroup(String providerSystemGroup) {
        this.providerSystemGroup = providerSystemGroup;
    }

    /**
     * Gets the consumer system name.
     *
     * @return the consumer system name
     */
    public String getConsumerSystemName() {
        return consumerSystemName;
    }

    /**
     * Sets the consumer system name.
     *
     * @param consumerSystemName the consumer system name
     */
    public void setConsumerSystemName(String consumerSystemName) {
        this.consumerSystemName = consumerSystemName;
    }

    /**
     * Gets the consumer system group.
     *
     * @return the consumer system group
     */
    public String getConsumerSystemGroup() {
        return consumerSystemGroup;
    }

    /**
     * Sets the consumer system group.
     *
     * @param consumerSystemGroup the consumer system group
     */
    public void setConsumerSystemGroup(String consumerSystemGroup) {
        this.consumerSystemGroup = consumerSystemGroup;
    }

    /**
     * Gets the monitor parameters.
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
     * Gets the time related clause.
     *
     * @return the time related clause
     */
    public boolean isSoftRealTime() {
        return softRealTime;
    }

    /**
     * Sets the time related clause.
     *
     * @param softRealTime the time related clause
     */
    public void setSoftRealTime(boolean softRealTime) {
        this.softRealTime = softRealTime;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MonitorRule rule = (MonitorRule) obj;

        if (!this.providerSystemName.equalsIgnoreCase(rule.providerSystemName)) {
            return false;
        }

        if (!this.providerSystemGroup.equalsIgnoreCase(rule.providerSystemGroup)) {
            return false;
        }

        if (!this.consumerSystemName.equalsIgnoreCase(rule.consumerSystemName)) {
            return false;
        }

        if (!this.consumerSystemGroup.equalsIgnoreCase(rule.consumerSystemGroup)) {
            return false;
        }

        if (!this.protocol.equalsIgnoreCase(rule.protocol)) {
            return false;
        }

        if (this.softRealTime != rule.softRealTime) {
            return false;
        }

        Set<String> names = this.parameters.keySet();

        for (String name : names) {
            if (!rule.getParameters().containsKey(name)) {
                return false;
            }
            if (!rule.getParameters().get(name).equalsIgnoreCase(rule.getParameters().get(name))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.protocol);
        hash = 37 * hash + Objects.hashCode(this.providerSystemName);
        hash = 37 * hash + Objects.hashCode(this.providerSystemGroup);
        hash = 37 * hash + Objects.hashCode(this.consumerSystemName);
        hash = 37 * hash + Objects.hashCode(this.consumerSystemGroup);
        hash = 37 * hash + Objects.hashCode(this.parameters);
        hash = 37 * hash + (this.softRealTime ? 1 : 0);
        return hash;
    }
}
