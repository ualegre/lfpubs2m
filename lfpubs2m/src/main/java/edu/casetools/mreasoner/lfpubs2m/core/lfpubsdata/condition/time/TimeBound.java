package edu.casetools.mreasoner.lfpubs2m.core.lfpubsdata.condition.time;

public class TimeBound {
	
	int 	  priority;
	TimeOfDay since;
	TimeOfDay until;
		
	public TimeBound(){
		since = null;
		until = null;
		priority = 0;
	}
	public TimeBound(TimeOfDay since, TimeOfDay until, int priority){
		this.priority=priority;
		this.since=since;
		this.until=until;
	}
	public void setTimeBound(TimeBound timeBound){
		this.priority=timeBound.priority;
		this.since=timeBound.since;
		this.until=timeBound.until;
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
