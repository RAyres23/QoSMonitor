/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.core.qos.monitor.type.presentation.model;

import eu.arrowhead.core.qos.monitor.event.model.Event;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ID0084D
 */
public class PresentationEvent {

    /**
     * Variable containing the event's producer uid.
     */
    private final SimpleStringProperty from;

    /**
     * Variable containing the event type.
     */
    private final SimpleStringProperty type;

    /**
     * Variable containing the event's severity.
     */
    private final SimpleIntegerProperty severity;

    /**
     * Variable containing the event payload.
     */
    private final SimpleStringProperty payload;

    public PresentationEvent(String from, String type, int severity, String payload) {
        this.from = new SimpleStringProperty(from);
        this.type = new SimpleStringProperty(type);
        this.severity = new SimpleIntegerProperty(severity);
        this.payload = new SimpleStringProperty(payload);
    }

    public PresentationEvent(Event event) {
        this.from = new SimpleStringProperty(event.getFrom());
        this.type = new SimpleStringProperty(event.getType());
        this.severity = new SimpleIntegerProperty(event.getDescription().getSeverity());
        this.payload = new SimpleStringProperty(event.getPayload());
    }

    /**
     * Gets the value of the from property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFrom() {
        return from.get();
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getType() {
        return type.get();
    }

    /**
     * Gets the value of the severity property.
     *
     * @return possible object is {@link String }
     *
     */
    public int getSeverity() {
        return severity.get();
    }

    /**
     * Gets the value of the payload property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPayload() {
        return payload.get();
    }

}
