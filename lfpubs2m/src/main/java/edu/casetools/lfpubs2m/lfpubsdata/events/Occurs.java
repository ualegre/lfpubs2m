package edu.casetools.lfpubs2m.lfpubsdata.events;

import java.util.Vector;

public class Occurs {
	String type;
	String time;
	Vector<Sensor> sensors;
	int frequency;
	
	public Occurs(){
		sensors = new Vector<Sensor>();
		frequency = 0;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Vector<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(Vector<Sensor> sensors) {
		this.sensors = sensors;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public void setSensor(Sensor sensor){
		this.sensors.add(sensor);
	}
	public void setConsequence(Sensor sensor) {
		
	}
	
	
	
}
