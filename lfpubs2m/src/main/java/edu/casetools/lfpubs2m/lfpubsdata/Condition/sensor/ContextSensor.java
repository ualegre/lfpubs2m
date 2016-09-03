package edu.casetools.lfpubs2m.lfpubsdata.Condition.sensor;

import edu.casetools.lfpubs2m.lfpubsdata.events.Sensor;

public class ContextSensor extends Sensor{
	String sensorName;
    String value;
    boolean higherThan;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isHigherThan() {
		return higherThan;
	}

	public void setHigherThan(boolean higherThan) {
		this.higherThan = higherThan;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
    
	
    
}
