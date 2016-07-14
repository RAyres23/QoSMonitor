/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.database;

import java.util.Map;
import org.bson.types.ObjectId;

/**
 * A representation of a MonitorLog for MongoDB.
 *
 * @author Renato Ayres
 */
public class MonitorLog {

    private final ObjectId id;
    private String type;
    private Map<String, Double> parameters;
    private Long timestamp;

    /**
     * Creates a new instance with a generated id.
     */
    public MonitorLog() {
        id = new ObjectId();
    }

    /**
     * Creates a new instance with a generated id, using the given monitor type,
     * monitor timestamp, and monitor parameters.
     *
     * @param type the monitor type
     * @param timestamp the monitor timestamp
     * @param parameters the monitor parameters
     */
    public MonitorLog(String type, Long timestamp, Map<String, Double> parameters) {
        this(new ObjectId(), type, timestamp, parameters);
    }

    /**
     * Creates a new instance using the given id, monitor type, monitor
     * timestamp, and monitor parameters.
     *
     * @param id the id
     * @param type the monitor type
     * @param timestamp the monitor timestamp
     * @param parameters the monitor parameters
     */
    public MonitorLog(final ObjectId id, String type, Long timestamp, Map<String, Double> parameters) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.parameters = parameters;
    }

    /**
     * Gets the log id.
     *
     * @return the log id
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
     * Gets the monitor timestamp.
     *
     * @return the monitor timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the monitor timestamp.
     *
     * @param timestamp the monitor timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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
     * Sets the monitor parameters
     *
     * @param parameters the monitor parameters
     */
    public void setParameters(Map<String, Double> parameters) {
        this.parameters = parameters;
    }

}
