package edu.casetools.mreasoner.lfpubs2m.core.lfpubsdata.condition.sensor;

public class SensorBound {
	
	int priority;
	ContextSensor since;
	ContextSensor until;
	
	public SensorBound(){
		priority = 0;
		since = null;
		until = null;
	}
	
	
	
	public int getPriority() {
		return priority;
	}



	public void setPriority(int priority) {
		this.priority = priority;
	}



	public ContextSensor getSince() {
		return since;
	}
	public void setSince(ContextSensor since) {
		this.since = since;
	}
	public ContextSensor getUntil() {
		return until;
	}
	public void setUntil(ContextSensor until) {
		this.until = until;
	}
	
	
}
