package edu.casetools.lfpubs2m.lfpubsdata;

import java.util.Iterator;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import edu.casetools.lfpubs2m.lfpubsdata.action.ThenDo;
import edu.casetools.lfpubs2m.lfpubsdata.condition.IfContext;
import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeOfDay;
import edu.casetools.lfpubs2m.lfpubsdata.events.Occurs;
import edu.casetools.lfpubs2m.lfpubsdata.events.Sensor;
import edu.casetools.lfpubs2m.reader.Syntax;


public class LFPUBSPattern {
	String id;
	
	Vector<Sensor> events;
	Vector<TimeBound> calendar_context;
	Vector<SensorBound> sensor_context;
	Vector<Sensor> consequences;
	Vector<String> day_context;
	Vector<String>	outputs;
	String general_context;
	Boolean actuator;
	Long aux;
	String delay;
	
	public LFPUBSPattern(){
		events   = new Vector<Sensor>();
		calendar_context	 = new Vector<TimeBound>();
		sensor_context		 = new Vector<SensorBound>();
		consequences  		 = new Vector<Sensor>();
		day_context			 = new Vector<String>();
		outputs				 = new Vector<String>();
		actuator			 = false;
	}

	public void setContext(IfContext context){
		this.calendar_context = context.getTimeBound();
		this.sensor_context   = context.getSensorBound();
		this.day_context	  = context.getDayBound();
	}
	
	public void setEvent(Occurs occurs) {
		for(int i=0;i<occurs.getSensors().size();i++){
			events.add( occurs.getSensors().get(i) );
		}
		if(occurs.getFrequency()==0){		// all the automation patterns introduced from LFPUBS related with the actuator have frequency=0;
			this.actuator=true;
		}
	}

	public void setAction(ThenDo thenDo) {
		this.consequences = thenDo.getConsequence();
		delay=thenDo.getWhenPlus();
		if(delay!=null){
		long aux=Long.valueOf(delay);
		long h=(long)aux/60/60;
		long m=((long)aux/60)%60;
		long s=((long)aux%60);
		delay=String.format("%02d:%02d:%02d",h,m,s);
		if(delay.compareTo("00:00:00")==0){
			delay="00:00:01"; //hau gerotxuo beirau
		}
		}
	}
	public void addOutput(String output){
		if(this.outputs!=null){
			this.outputs.add(output);
		}
	}
	

	public Boolean getActuator() {
		return actuator;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Vector<Sensor> getEvents() {
		return events;
	}

	public Vector<TimeBound> getCalendar_context() {
		return calendar_context;
	}

	public Vector<SensorBound> getSensor_context() {
		return sensor_context;
	}

	public Vector<Sensor> getConsequences() {
		return consequences;
	}


	public Vector<String> getDay_context() {
		return day_context;
	}

	public String getDelay() {
		return delay;
	}

	public Vector<Sensor> getConsequence() {
		return consequences;
	}


	public void setConsequence(Vector<Sensor> consequence) {
		this.consequences = consequence;
	}

	public String printPattern(){
		String pattern = "";

		if( ( outputs.size() > 0 ) && ( consequences.size()>0)){
			pattern = pattern + printActionRules();

		}
		return pattern;
	}
	
	private String printActionRules(){
		String act="";
		String pattern = "",auxiliar_pattern = "",final_pattern="", auxiliar_pattern_2="";;
		String auxiliar_comma[] = {""," , "};
		String auxiliar_and[] = {""," ^ "};
		Vector<String>multipleContext=new Vector<String>();
		Vector<Integer>positions=new Vector<Integer>();
		int k=0;
		int j = 0;
		int numb=0;
		auxiliar_pattern=auxiliar_pattern + " ssr( ( ";
				if(events.size()!=0){
					auxiliar_pattern=auxiliar_pattern+"[-]["+delay+"]";
				}
		for(int i=0;i<events.size();i++){
			auxiliar_pattern=auxiliar_pattern+auxiliar_and[j]+events.get(i).getStatus()+events.get(i).getId().substring(0, events.get(i).getId().length()-2);
			if(j==0){
				j++;
			}
		}
		for(int i=1;i<outputs.size();i++){
			if(outputs.get(i).contains("day_context")==false){
				auxiliar_pattern=auxiliar_pattern+auxiliar_and[j]+outputs.get(i);
				if(j==0){
					j++;
				}
			}
			else{
				positions.add(i);
				
			}
		}
		if(positions.size()!=0){
			for(int i=0;i<positions.size();i++){
				final_pattern=final_pattern+auxiliar_pattern+auxiliar_and[j]+outputs.get(positions.get(i))+" ) -> "+outputs.get(0)+" ); \n";
				
			}
		}
		else{
			final_pattern=auxiliar_pattern+" ) -> "+outputs.get(0)+" ); \n";
		}
		
			j=0;			
		if(actuator==true){
			for(int i=0;i<consequences.size();i++){
			pattern=pattern+" ssr( ("+outputs.get(0)+" ) ->"+consequences.get(i).getStatus()+consequences.get(i).getId().substring(0, consequences.get(i).getId().length()-2)+ " ); \n";;
			//pattern=pattern+consequences.get(i).getStatus()+consequences.get(i).getId().substring(0, consequences.get(i).getId().length()-2)+ " ) \n";
			}
			auxiliar_pattern=final_pattern+pattern;
		}
		else{
			auxiliar_pattern=final_pattern;
		}
		return auxiliar_pattern;	
		}
	
	}

