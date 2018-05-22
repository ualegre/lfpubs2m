package edu.casetools.mreasoner.lfpubs2m.core.lfpubsdata.condition.time;

public class DayOfWeek {

	String day;
	int priority;
	
	public DayOfWeek(){
		day="";
		priority=0;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	public boolean isWeekday(String day){
		if((day.equals("saturday")==true)||(day.equals("sunday")==true)){
			return false;
		}
		else{
			return true;
		}
	}
	
	
}
