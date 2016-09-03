package edu.casetools.lfpubs2m.lfpubsdata.condition.time;

public class TimeBound {
	
	int 	  priority;
	TimeOfDay since;
	TimeOfDay until;
		
	public TimeBound(){
		since = null;
		until = null;
		priority = 0;
	}
	
	
	
	public int getPriority() {
		return priority;
	}



	public void setPriority(int priority) {
		this.priority = priority;
	}



	public TimeOfDay getSince() {
		return since;
	}
	public void setSince(TimeOfDay since) {
		this.since = since;
	}
	
	public void setSinceString(String since){
		if(this.since == null) this.since = new TimeOfDay();
		this.since.setTimeOfDay(since);
	}
	
	public void setUntilString(String until){
		if(this.until == null) this.until = new TimeOfDay();
		this.until.setTimeOfDay(until);
		
	}
	
	public TimeOfDay getUntil() {
		return until;
	}
	public void setUntil(TimeOfDay until) {
		this.until = until;
	}
	
}
