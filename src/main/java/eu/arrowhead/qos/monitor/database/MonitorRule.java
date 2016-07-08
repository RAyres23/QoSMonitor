package eu.arrowhead.qos.monitor.database;

import java.util.Map;

import org.bson.types.ObjectId;

/**
 * A representation of a MonitorRule for MongoDB.
 *
 * @author Renato Ayres
 */
public class MonitorRule {

    private final ObjectId id;
    private String type;
    private String providerSystemName;
    private String providerSystemGroup;
    private String consumerSystemName;
    private String consumerSystemGroup;
    private Map<String, Double> parameters;
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
     * group, monitor type, and monitor parameters and a time related clause.
     *
     * @param type the monitor type
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
            Map<String, Double> parameters, boolean softRealTime) {
        this(new ObjectId(), type,
                providerSystemName, providerSystemGroup,
                consumerSystemName, consumerSystemGroup,
                parameters, softRealTime);
    }

    /**
     * Creates a new instance using the given id, provider system name, provider
     * system group, consumer system name, consumer system group, monitor type,
     * and monitor parameters and a time related clause.
     *
     * @param id the id
     * @param type the monitor type
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
            Map<String, Double> parameters, boolean softRealTime) {
        this.id = id;
        this.type = type;
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
     * Gets the monitor type.
     *
     * @return the monitor type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the monitor type.
     *
     * @param type the monitor type
     */
    public void setType(String type) {
        this.type = type;
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
    public Map<String, Double> getParameters() {
        return parameters;
    }

    /**
     * Sets the monitor parameters.
     *
     * @param parameters the monitor parameters
     */
    public void setParameters(Map<String, Double> parameters) {
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

        return this.consumerSystemGroup.equalsIgnoreCase(rule.consumerSystemGroup);
    }
}
