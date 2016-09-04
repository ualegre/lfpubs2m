package edu.casetools.lfpubs2m.lfpubsdata.condition.time;

public class TimeOfDay {
	String hour;
	String minute;
	String second;
	boolean higherThan;
	
	public TimeOfDay(){
		hour   = "";
		minute = "";
		second = "";
		higherThan = false;
	}
	
	public boolean isHigherThan() {
		return higherThan;
	}

	public void setHigherThan(boolean higherThan) {
		this.higherThan = higherThan;
	}

	public void setTimeOfDay(String timeOfDay){
		float aux   = Float.parseFloat(timeOfDay);
		int hour   = getHourFromTimeOfDay(aux);
		int minute = getMinutesFromTimeOfDay(aux,hour);
		int second = getSecondsFromTimeOfDay(aux,hour,minute);

		this.hour 	= correct(hour) ;
		this.minute = correct(minute);
		this.second = correct(second);
	}
	
	private int getHourFromTimeOfDay(float timeOfDay){
		int hour = (int)timeOfDay/3600;
		return hour;
	}
	private int getMinutesFromTimeOfDay(float timeOfDay,int hour){
		int minute = (int)(timeOfDay/60) - (hour*60);
		return minute;
	}
	
	private int getSecondsFromTimeOfDay(float timeOfDay,int hour,int minute){
		int second = (int)timeOfDay - (hour * 3600) - (minute * 60);
		return second;
	}
	
	private String correct(int time){
		if(time < 10) return "0"+time;
		else return""+time;
	}
	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}
	
	public String getTimeOfDayClockFormat(){
		return this.hour+":"+this.minute+":"+this.second;
	}
	
	
	
}
