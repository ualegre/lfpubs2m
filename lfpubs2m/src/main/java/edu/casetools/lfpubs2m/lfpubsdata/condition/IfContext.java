package edu.casetools.lfpubs2m.lfpubsdata.condition;

import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;


public class IfContext {
	
	Vector<SensorBound> sensorBound;
	Vector<TimeBound> 	timeBounds;
	Vector<String>	dayBound;
	
	public IfContext(){
		sensorBound = new Vector<SensorBound>();
		timeBounds   = new Vector<TimeBound>();
		dayBound	= new Vector<String>();
	}

	public Vector<SensorBound> getSensorBound() {
		return sensorBound;
	}

	public void setSensorBound(Vector<SensorBound> sensorBound) {
		this.sensorBound = sensorBound;
	}

	public Vector<TimeBound> getTimeBound() {
		return timeBounds;
	}

	public void setTimeBound(Vector<TimeBound> timeBound) {
		this.timeBounds = timeBound;
	}

	public void addSensorBound(SensorBound sensorBound) {
		this.sensorBound.add(sensorBound);
		
	}

	public void addCalendarBound(TimeBound timeBound) {
		this.timeBounds.add(timeBound);
		
	}

	public Vector<String> getDayBound() {
		return dayBound;
	}

	public void setDayBound(Vector<String> dayBound) {
		this.dayBound = dayBound;
	}
	
}
