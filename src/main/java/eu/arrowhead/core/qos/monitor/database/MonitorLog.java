/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.database;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * A representation of a MonitorLog for MongoDB.
 *
 * @author Renato Ayres
 */
public class MonitorLog {

    private final ObjectId id;
    private String protocol;
    private Map<String, String> parameters;
    private Long timestamp;

    /**
     * Creates a new instance with a generated id.
     */
    public MonitorLog() {
        id = new ObjectId();
    }

    /**
     * Creates a new instance with a generated id, using the given monitor
     * protocol, monitor timestamp, and monitor parameters.
     *
     * @param type the monitor protocol
     * @param timestamp the monitor timestamp
     * @param parameters the monitor parameters
     */
    public MonitorLog(String type, Long timestamp, Map<String, String> parameters) {
        this(new ObjectId(), type, timestamp, parameters);
    }

    /**
     * Creates a new instance using the given id, monitor protocol, monitor
     * timestamp, and monitor parameters.
     *
     * @param id the id
     * @param type the monitor protocol
     * @param timestamp the monitor timestamp
     * @param parameters the monitor parameters
     */
    public MonitorLog(final ObjectId id, String type, Long timestamp, Map<String, String> parameters) {
        this.id = id;
        this.protocol = type;
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
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Sets the monitor parameters
     *
     * @param parameters the monitor parameters
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MonitorLog log = (MonitorLog) obj;

        if (!this.protocol.equalsIgnoreCase(log.protocol)) {
            return false;
        }

        if (!Objects.equals(this.timestamp, log.timestamp)) {
            return false;
        }

        Set<String> names = this.parameters.keySet();

        for (String name : names) {
            if (!log.getParameters().containsKey(name)) {
                return false;
            }
            if (!log.getParameters().get(name).equalsIgnoreCase(log.getParameters().get(name))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.protocol);
        hash = 83 * hash + Objects.hashCode(this.parameters);
        hash = 83 * hash + Objects.hashCode(this.timestamp);
        return hash;
    }

}
