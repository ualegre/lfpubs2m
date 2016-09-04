package edu.casetools.lfpubs2m.lfpubsdata.condition;

import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;


public class IfContext {
	
	Vector<SensorBound> sensorBound;
	Vector<TimeBound> 	timeBound;
	
	public IfContext(){
		sensorBound = new Vector<SensorBound>();
		timeBound   = new Vector<TimeBound>();
	}

	public Vector<SensorBound> getSensorBound() {
		return sensorBound;
	}

	public void setSensorBound(Vector<SensorBound> sensorBound) {
		this.sensorBound = sensorBound;
	}

	public Vector<TimeBound> getTimeBound() {
		return timeBound;
	}

	public void setTimeBound(Vector<TimeBound> timeBound) {
		this.timeBound = timeBound;
	}

	public void addSensorBound(SensorBound sensorBound) {
		this.sensorBound.add(sensorBound);
		
	}

	public void addCalendarBound(TimeBound timeBound) {
		this.timeBound.add(timeBound);
		
	}
	
	
	
}
