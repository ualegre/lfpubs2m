package edu.casetools.lfpubs2m.lfpubsdata;

import java.util.Vector;

public class GeneralCondition {

	
	String[] DayOfWeek;
	String TimeOfDaySmall;
	String TimeOfDayBig;
	

	public GeneralCondition(){
		DayOfWeek=null;
		TimeOfDaySmall=null;
		TimeOfDayBig=null;
	}
public void setDayOfWeek(String dayOfWeek){
	this.DayOfWeek=dayOfWeek.split(",");
	//System.out.println(DayOfWeek[0]);    ///Esti hau gero aldau
	
}
public void setTimeDaySmall(String smallTime){
	if(this.TimeOfDaySmall==null){
		this.TimeOfDaySmall=smallTime;
	}	
}
public void setTimeDayBig(String bigTime){
	if(this.TimeOfDayBig==null){
		this.TimeOfDayBig=bigTime;
	}
	}
public void setGeneralCondition(GeneralCondition generalCondition){
	this.DayOfWeek=generalCondition.DayOfWeek;
	this.TimeOfDayBig=generalCondition.TimeOfDayBig;
	this.TimeOfDaySmall=generalCondition.TimeOfDaySmall;
}
public String[] getDayOfWeek() {
	return DayOfWeek;
}
public String getTimeOfDaySmall() {
	return TimeOfDaySmall;
}
public String getTimeOfDayBig() {
	return TimeOfDayBig;
}

}