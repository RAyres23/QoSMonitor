package eu.arrowhead.common.model.messages;

import eu.arrowhead.common.model.ArrowheadSystem;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceError {

    private String type;
    private ArrowheadSystem system;
    private Map<String, String> parameters;
    private String errorMessage;

    public ServiceError() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ServiceError(String type, ArrowheadSystem system, Map<String, String> parameters, String errorMessage) {
        super();
        this.system = system;
        this.parameters = parameters;
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrowheadSystem getSystem() {
        return system;
    }

    public void setSystem(ArrowheadSystem system) {
        this.system = system;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
