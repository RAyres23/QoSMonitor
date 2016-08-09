package eu.arrowhead.common.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for storing Arrowhead Clouds in the database. The "operator" and
 * "cloud_name" columns must be unique together.
 */
@XmlRootElement
public class ArrowheadCloud {

    private String operator;
    private String cloudName;
    private String address;
    private String port;
    private String gatekeeperServiceURI;
    private String authenticationInfo;

    public ArrowheadCloud() {
    }

    public ArrowheadCloud(String operator, String cloudName, String address, String port,
            String gatekeeperServiceURI, String authenticationInfo) {
        this.operator = operator;
        this.cloudName = cloudName;
        this.address = address;
        this.port = port;
        this.gatekeeperServiceURI = gatekeeperServiceURI;
        this.authenticationInfo = authenticationInfo;
    }

    public ArrowheadCloud(ArrowheadCloud ownCloud) {
        this.operator = ownCloud.getOperator();
        this.cloudName = ownCloud.getCloudName();
        this.address = ownCloud.getAddress();
        this.port = ownCloud.getPort();
        this.gatekeeperServiceURI = ownCloud.getGatekeeperServiceURI();
        this.authenticationInfo = ownCloud.getAuthenticationInfo();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getGatekeeperServiceURI() {
        return gatekeeperServiceURI;
    }

    public void setGatekeeperServiceURI(String gatekeeperServiceURI) {
        this.gatekeeperServiceURI = gatekeeperServiceURI;
    }

    public String getAuthenticationInfo() {
        return authenticationInfo;
    }

    public void setAuthenticationInfo(String authenticationInfo) {
        this.authenticationInfo = authenticationInfo;
    }

    public boolean isValid() {
        if (operator == null || cloudName == null || address == null || gatekeeperServiceURI == null) {
            return false;
        }
        return true;
    }

    public boolean isValidForDatabase() {
        if (operator == null || cloudName == null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((cloudName == null) ? 0 : cloudName.hashCode());
        result = prime * result + ((gatekeeperServiceURI == null) ? 0 : gatekeeperServiceURI.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ArrowheadCloud other = (ArrowheadCloud) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (cloudName == null) {
            if (other.cloudName != null) {
                return false;
            }
        } else if (!cloudName.equals(other.cloudName)) {
            return false;
        }
        if (gatekeeperServiceURI == null) {
            if (other.gatekeeperServiceURI != null) {
                return false;
            }
        } else if (!gatekeeperServiceURI.equals(other.gatekeeperServiceURI)) {
            return false;
        }
        if (operator == null) {
            if (other.operator != null) {
                return false;
            }
        } else if (!operator.equals(other.operator)) {
            return false;
        }
        if (port == null) {
            if (other.port != null) {
                return false;
            }
        } else if (!port.equals(other.port)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + operator + ":" + cloudName + ")";
    }

}
