package eu.arrowhead.qos.factories;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *  
 * @author Paulo
 *
 */
@XmlRootElement
public class Value {
	
	private String measuring_unit;
	private String value;
	
	protected Value(){
		
	}
	
	protected Value(String value, String measuring_unit) {
		super();
		this.value = value;
		this.measuring_unit = measuring_unit;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	public String getMeasuring_unit() {
		return measuring_unit;
	}

	public void setMeasuring_unit(String measuring_unit) {
		this.measuring_unit = measuring_unit;
	}

	@Override
	public String toString() {
		return value;
	}
	
	
}
