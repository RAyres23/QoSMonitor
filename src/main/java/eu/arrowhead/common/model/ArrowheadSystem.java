package eu.arrowhead.common.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArrowheadSystem {
	
	private String systemGroup;
	private String systemName;
	private String IPAddress;
	private String port;
	private String authenticationInfo;
	
	public ArrowheadSystem(){
		
	}
	
	public ArrowheadSystem(String systemGroup, String systemName, 
			String iPAddress, String port, String authenticationInfo) {
		super();
		this.systemGroup = systemGroup;
		this.systemName = systemName;
		this.IPAddress = iPAddress;
		this.port = port;
		this.authenticationInfo = authenticationInfo;
	}

	public String getSystemGroup() {
		return systemGroup;
	}

	public void setSystemGroup(String systemGroup) {
		this.systemGroup = systemGroup;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAuthenticationInfo() {
		return authenticationInfo;
	}

	public void setAuthenticationInfo(String authenticationInfo) {
		this.authenticationInfo = authenticationInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((systemGroup == null) ? 0 : systemGroup.hashCode());
		result = prime * result + ((systemName == null) ? 0 : systemName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrowheadSystem other = (ArrowheadSystem) obj;
		if (systemGroup == null) {
			if (other.systemGroup != null)
				return false;
		} else if (!systemGroup.equals(other.systemGroup))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		return true;
	}
	
	
}
