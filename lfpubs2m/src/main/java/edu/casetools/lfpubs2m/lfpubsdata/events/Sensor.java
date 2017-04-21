package edu.casetools.lfpubs2m.lfpubsdata.events;

import edu.casetools.lfpubs2m.reader.Syntax;

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
		if(status.equals("off")||status.equals("OFF"))
		this.status = Syntax.NEGATIVE_SIGN;
	}
	
	public String getNegatedStatus(){
		if(this.status.equals(Syntax.NEGATIVE_SIGN)) return "";
		else return Syntax.NEGATIVE_SIGN;
	}
	public boolean isNegative(String status){
		if(status.equals("off")||status.equals("OFF")){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
