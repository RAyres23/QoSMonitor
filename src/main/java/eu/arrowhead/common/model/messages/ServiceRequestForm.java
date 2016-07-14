package eu.arrowhead.common.model.messages;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import eu.arrowhead.common.model.ArrowheadService;
import eu.arrowhead.common.model.ArrowheadSystem;
import eu.arrowhead.core.qos.factories.Value;

@XmlRootElement
public class ServiceRequestForm {

    private ArrowheadService requestedService;
    private Map<String, Value> requestedQoS;
    private ArrowheadSystem requesterSystem;
    private Map<String, Boolean> orchestrationFlags = new HashMap<>();

    public ServiceRequestForm() {
        super();
        requestedQoS = new HashMap<>();
    }

    public ServiceRequestForm(ArrowheadService requestedService, Map<String, Value> requestedQoS, ArrowheadSystem requesterSystem) {
        this.requestedService = requestedService;
        this.requestedQoS = requestedQoS;
        this.requesterSystem = requesterSystem;
        this.orchestrationFlags.put("matchmaking", false);
        this.orchestrationFlags.put("externalServiceRequest", false);
        this.orchestrationFlags.put("triggerInterCloud", false);
        this.orchestrationFlags.put("metadataSearch", false);
        this.orchestrationFlags.put("pingProvider", false);
    }

    public ServiceRequestForm(ArrowheadService requestedService, Map<String, Value> requestedQoS, ArrowheadSystem requesterSystem,
            Map<String, Boolean> orchestrationFlags) {
        this.requestedService = requestedService;
        this.requestedQoS = requestedQoS;
        this.requesterSystem = requesterSystem;
        this.orchestrationFlags = orchestrationFlags;
    }

    public ArrowheadService getRequestedService() {
        return requestedService;
    }

    public void setRequestedService(ArrowheadService requestedService) {
        this.requestedService = requestedService;
    }

    public ArrowheadSystem getRequesterSystem() {
        return requesterSystem;
    }

    public void setRequesterSystem(ArrowheadSystem requesterSystem) {
        this.requesterSystem = requesterSystem;
    }

    public Map<String, Boolean> getOrchestrationFlags() {
        return orchestrationFlags;
    }

    public void setOrchestrationFlags(Map<String, Boolean> orchestrationFlags) {
        this.orchestrationFlags = orchestrationFlags;
    }

    public Map<String, Value> getRequestedQoS() {
        return requestedQoS;
    }

    public void setRequestedQoS(Map<String, Value> requestedQoS) {
        this.requestedQoS = requestedQoS;
    }

}
