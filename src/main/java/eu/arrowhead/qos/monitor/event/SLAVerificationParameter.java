/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.qos.monitor.event;

/**
 *
 * @author Renato Ayres
 */
public class SLAVerificationParameter {

    private String name;
    private Double requestedValue;
    private Double loggedValue;

    public SLAVerificationParameter(String name, Double requestedValue, Double loggedValue) {
        this.name = name;
        this.requestedValue = requestedValue;
        this.loggedValue = loggedValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRequestedValue() {
        return requestedValue;
    }

    public void setRequestedValue(Double requestedValue) {
        this.requestedValue = requestedValue;
    }

    public Double getLoggedValue() {
        return loggedValue;
    }

    public void setLoggedValue(Double loggedValue) {
        this.loggedValue = loggedValue;
    }
}
