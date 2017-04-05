package edu.casetools.lfpubs2m.lfpubsdata.condition.time;

public class DayBound {

		String since;
		String until;
		
	public DayBound(){
		since=null;
		until=null;
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
	
	
}
