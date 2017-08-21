package edu.casetools.lfpubs2m.core.lfpubsdata.condition.time;

public class DayBound {

		String since;
		String until;
		int priority;
		
	public DayBound(){
		since=null;
		until=null;
		priority=0;
	}
	public DayBound(String since, String until){
		this.since=since;
		this.until=until;
	}
	public String getSince() {
		return since;
	}
	
	public String getUntil() {
		return until;
	}

	public void setSince(String since) {
		this.since = since;
	}
	public void setUntil(String until) {
		this.until = until;
	}
	public void setPriority(int priority) {
		this.priority=priority;
	}
	public int getPriority() {
		return priority;
	}
	
	
}
