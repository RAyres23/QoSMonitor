/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.arrowhead.qos.monitor.event;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Renato Ayres
 */
public class SLAVerificationResponse {

    private boolean SLABroken;
    private List<SLAVerificationParameter> parameters;

    public SLAVerificationResponse() {
        this.SLABroken = false;
        this.parameters = new ArrayList();
    }

    public SLAVerificationResponse(List<SLAVerificationParameter> parameters) {
        this.SLABroken = !parameters.isEmpty();
        this.parameters = parameters;
    }

    public boolean isSLABroken() {
        return SLABroken;
    }

    public List<SLAVerificationParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<SLAVerificationParameter> parameters) {
        this.SLABroken = true;
        this.parameters = parameters;
    }

    public boolean addParameter(SLAVerificationParameter parameter) {
        this.SLABroken = true;
        return this.parameters.add(parameter);
    }
}
