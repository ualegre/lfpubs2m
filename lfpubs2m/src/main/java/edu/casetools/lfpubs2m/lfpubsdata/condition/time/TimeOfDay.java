package edu.casetools.lfpubs2m.lfpubsdata.condition.time;
public class TimeOfDay {
	String hour;
	String minute;
	String second;
	double miliseconds;
	boolean higherThan;
	
	public TimeOfDay(){
		hour   = "";
		minute = "";
		second = "";
		higherThan = false;
		miliseconds=0;
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
		this.miliseconds=Double.parseDouble(timeOfDay);
		
	}
	public void setTimeOFDaySince(String timeOfDay){
		String[] time=timeOfDay.split(":");
		this.hour=time[0];
		this.minute=time[1];
		this.second=time[2];
		this.higherThan=true;
		this.miliseconds=Integer.valueOf(this.hour)*3600+Integer.valueOf(this.minute)*60+Integer.valueOf(this.second);
		
	}
	public void setTimeOFDayUntil(String timeOfDay){
		String[] time=timeOfDay.split(":");
		this.hour=time[0];
		this.minute=time[1];
		this.second=time[2];
		this.higherThan=false;
		this.miliseconds=Integer.valueOf(this.hour)*3600+Integer.valueOf(this.minute)*60+Integer.valueOf(this.second);
		
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
	
	public Double getMiliseconds() {
		return miliseconds;
	}

	public void setMiliseconds(int miliseconds) {
		this.miliseconds = miliseconds;
	}
	public void setMili(int hour, int minute, int seconds){
		int  time=(hour*3600)+(minute*60)+seconds;
		this.miliseconds=time;
	}

	public String getTimeOfDayClockFormat(){
		return this.hour+":"+this.minute+":"+this.second;
	}
	public String getTimeOfDayClockFormat2(TimeOfDay time){
		return time.hour+":"+time.minute+":"+time.second;
	}
	
	
}
