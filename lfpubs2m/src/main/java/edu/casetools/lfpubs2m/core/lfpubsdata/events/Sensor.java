package edu.casetools.lfpubs2m.core.lfpubsdata.events;

import edu.casetools.lfpubs2m.core.reader.Syntax;

public class Sensor {
	String id;
	String status;
	
	public Sensor(){
		status = "";
		id = "";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		if(status.equalsIgnoreCase("off"))
			this.status = Syntax.NEGATIVE_SIGN;
	}
	
	public String getNegatedStatus(){
		if(this.status.equals(Syntax.NEGATIVE_SIGN)) return "";
		else return Syntax.NEGATIVE_SIGN;
	}
	public boolean isNegative(){
		if(this.status.equals(Syntax.NEGATIVE_SIGN)==true){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
