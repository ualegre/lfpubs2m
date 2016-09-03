package edu.casetools.lfpubs2m.lfpubsdata.action;

import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.events.Sensor;



public class ThenDo {
	Vector<Sensor> delayedEvents;
	Vector<Sensor> consequence;
	String time;
	String type;
	String whenBase;
	String whenPlus;
	
	public ThenDo(){
		delayedEvents = new Vector<Sensor>();
		consequence   = new Vector<Sensor>();
		type = "";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vector<Sensor> getDelayedEvents() {
		return delayedEvents;
	}

	public void setDelayedEvents(Vector<Sensor> delayedEvents) {
		this.delayedEvents = delayedEvents;
	}

	public Vector<Sensor> getConsequence() {
		return consequence;
	}

	public void setConsequence(Vector<Sensor> consequence) {
		this.consequence = consequence;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWhenBase() {
		return whenBase;
	}

	public void setWhenBase(String whenBase) {
		this.whenBase = whenBase;
	}

	public String getWhenPlus() {
		return whenPlus;
	}

	public void setWhenPlus(String whenPlus) {
		float auxiliar = Float.parseFloat(whenPlus);
		auxiliar = Math.round(auxiliar);
		int result = (int)auxiliar;
		this.whenPlus = ""+result;
	}
	
	public void setSensor(Sensor sensor){
		this.delayedEvents.add(sensor);
	}

	public void setConsequence(Sensor sensor) {
		this.consequence.add(sensor);
		
	}
	
	
}
