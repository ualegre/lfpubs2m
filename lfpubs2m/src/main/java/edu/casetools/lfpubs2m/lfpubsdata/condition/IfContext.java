package edu.casetools.lfpubs2m.lfpubsdata.condition;

import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;


public class IfContext {
	
	Vector<SensorBound> sensorBound;
	Vector<TimeBound> 	timeBounds;
	Vector<DayBound>	dayBound;
	
	public IfContext(){
		sensorBound = new Vector<SensorBound>();
		timeBounds   = new Vector<TimeBound>();
		dayBound	= new Vector<DayBound>();
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
		this.timeBounds=filterCalendarBound(timeBounds);
		
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
	
	public  Vector<TimeBound> filterCalendarBound(Vector<TimeBound> timeBounds){
		
		TimeBound timeBound2=new TimeBound();
		timeBound2.setTimeBound(timeBounds.get(0));
		for(int i=0;i<timeBounds.size();i++){
			if(timeBounds.get(i).getSince()!=null){
				if(timeBounds.get(i).getSince().getMiliseconds()>timeBound2.getSince().getMiliseconds()){
					timeBound2.setSince(timeBounds.get(i).getSince());
				}
			}
			if(timeBounds.get(i).getUntil()!=null){
				if(timeBounds.get(i).getUntil().getMiliseconds()<timeBound2.getUntil().getMiliseconds()){
					timeBound2.setUntil(timeBounds.get(i).getUntil());
				}
				
			}
		}
		
		timeBounds.clear();
		timeBounds.add(timeBound2);
		return timeBounds;
	}
	
}
