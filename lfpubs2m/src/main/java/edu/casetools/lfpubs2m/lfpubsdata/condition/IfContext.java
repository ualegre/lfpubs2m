package edu.casetools.lfpubs2m.lfpubsdata.condition;

import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;


public class IfContext {
	
	Vector<SensorBound> sensorBound;
	Vector<TimeBound> 	timeBound;
	Vector<DayBound>	dayBound;
	
	public IfContext(){
		sensorBound = new Vector<SensorBound>();
		timeBound   = new Vector<TimeBound>();
		dayBound	= new Vector<DayBound>();
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

	public Vector<DayBound> getDayBound() {
		return dayBound;
	}

	public void setDayBound(Vector<DayBound> dayBound) {
		this.dayBound = dayBound;
	}
	public void addDayBound( DayBound dayBound){
		this.dayBound.add(dayBound);
	}
	
	
	
}
