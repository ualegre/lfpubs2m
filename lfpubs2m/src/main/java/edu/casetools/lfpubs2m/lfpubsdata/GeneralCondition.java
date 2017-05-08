package edu.casetools.lfpubs2m.lfpubsdata;

import java.util.Arrays;
import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeOfDay;


public class GeneralCondition{

	
	Vector<String> DayOfWeek;
	TimeBound timebound;
	

	public GeneralCondition(){
		DayOfWeek=new Vector<String>();
		timebound=new TimeBound();
	}
public void setDayOfWeek(String dayOfWeek){
	String[] days=dayOfWeek.toLowerCase().split(",");
	this.DayOfWeek=new Vector<String>(Arrays.asList(days));
	
	
}

public void setGeneralCondition(GeneralCondition generalCondition){
	this.DayOfWeek=generalCondition.DayOfWeek;
	this.timebound=generalCondition.timebound;
}

public Vector<String> getDayOfWeek() {
	return DayOfWeek;
}
public TimeBound getTimebound() {
	return timebound;
}
public void setTimebound(TimeBound timebound) {
	this.timebound.setSince(timebound.getSince());
	this.timebound.setUntil(timebound.getUntil());
}
public void setSince(TimeOfDay since){
	this.timebound.setSince(since);
}
public void setUntil(TimeOfDay until){
	this.timebound.setUntil(until);
}


}