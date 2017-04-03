package edu.casetools.lfpubs2m.lfpubsdata;

import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.action.ThenDo;
import edu.casetools.lfpubs2m.lfpubsdata.condition.IfContext;
import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;
import edu.casetools.lfpubs2m.lfpubsdata.events.Occurs;
import edu.casetools.lfpubs2m.lfpubsdata.events.Sensor;
import edu.casetools.lfpubs2m.reader.Syntax;


public class LFPUBSPattern {
	String id;
	
	Vector<Sensor> events;
	Vector<TimeBound> calendar_context;
	Vector<SensorBound> sensor_context;
	Vector<Sensor> consequences;
	Vector<DayBound> day_context;
	
	String delay;
	
	public LFPUBSPattern(){
		events   = new Vector<Sensor>();
		calendar_context	 = new Vector<TimeBound>();
		sensor_context		 = new Vector<SensorBound>();
		consequences  		 = new Vector<Sensor>();
		day_context			 = new Vector<DayBound>();
	//	actions 	= new Vector<Action>();
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
	}

	public void setAction(ThenDo thenDo) {
		this.consequences = thenDo.getConsequence();
		delay = thenDo.getWhenPlus();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Vector<Sensor> getConsequence() {
		return consequences;
	}


	public void setConsequence(Vector<Sensor> consequence) {
		this.consequences = consequence;
	}

	public String printPattern(){
		String pattern = "";

		if( ( events.size() > 0 ) || ( sensor_context.size() > 0 ) || ( calendar_context.size() > 0 ) ){
			
			pattern = printEventRules();
			pattern = pattern + printContextRules();
			pattern = pattern + printActionRules();

		}
		return pattern;
	}
	
	private String printContextRules() {
		String pattern = "";
		pattern = printCalendarContextRules(pattern);
		pattern = printCalendarContextRulesNegation(pattern);
		pattern = printDayContextRules(pattern);
		pattern = printDayContextRulesNegation(pattern);
//		if(sensor_context.size()>0){
//			for(int i=0;i<sensor_context.size();i++){
//				if(i!=0) pattern = pattern +" , ";
//				if(sensor_context.get(i).getUntil() != null){
//					pattern = printFullSensorContextRules(pattern,sensor_context.get(i));
//				}else{
//				//	pattern = printHalfSensorContextRules(pattern,sensor_context.get(i));
//				}
//				if(i == (sensor_context.size()-1) ) pattern = pattern + " -> calendar_context"+id+"\n";
//			}
//			return pattern;
//		}
		return pattern;
	}
	private String printDayContextRules(String pattern){
		for(int i=0;i<day_context.size();i++){
			pattern=pattern+ " ssr( ( ";
			pattern=printFullDayContext(pattern,day_context.get(i));
			pattern = pattern + " ) => day_context_"+id+" ). \n";
		}
		return pattern;
	}
	private String printDayContextRulesNegation(String pattern){
		if(day_context.size()>0) pattern = pattern + " ssr( ( ";
		for(int i=0;i<day_context.size();i++){
			pattern = pattern + Syntax.NEGATIVE_SIGN;
			pattern=printFullDayContext(pattern,day_context.get(i));
		
		if(i == (day_context.size()-1) ) pattern = pattern + " ) => "+Syntax.NEGATIVE_SIGN+"day_context"+id+" ). \n";
		}
		return pattern;
	}

	private String printCalendarContextRules(String pattern) {
		for(int i=0;i<calendar_context.size();i++){
			pattern = pattern + " ssr( ( ";
			if(calendar_context.get(i).getUntil() != null){
				pattern = printFullCalendarContextRules(pattern,calendar_context.get(i));
			}else{
				pattern = printHalfCalendarContextRules(pattern,calendar_context.get(i));
			}
			pattern = pattern + " ) => calendar_context_"+id+" ). \n";
		}
		return pattern;
	}

	private String printCalendarContextRulesNegation(String pattern){
		if(calendar_context.size()>0) pattern = pattern + " ssr( ( ";
		for(int i=0;i<calendar_context.size();i++){
			if(i!=0) pattern = pattern +" , ";
			if(calendar_context.get(i).getUntil() != null){
				pattern = pattern + Syntax.NEGATIVE_SIGN;
				pattern = printFullCalendarContextRules(pattern,calendar_context.get(i));
			}else{
				pattern = pattern + Syntax.NEGATIVE_SIGN;
				pattern = printHalfCalendarContextRules(pattern,calendar_context.get(i));
			}
			if(i == (calendar_context.size()-1) ) pattern = pattern + " ) => "+Syntax.NEGATIVE_SIGN+"calendar_context"+id+" ). \n";
		}
		return pattern;
	}
	
		
		private String printFullCalendarContextRules(String pattern,TimeBound bound){
			if(bound.getSince().isHigherThan()){
			pattern = pattern  +"clockBetween("+bound.getSince().getTimeOfDayClockFormat()+
					Syntax.CLOCK_SEPARATOR+bound.getUntil().getTimeOfDayClockFormat()+")";
			}else{
				pattern = pattern  +"clockBetween("+bound.getUntil().getTimeOfDayClockFormat()+
						Syntax.CLOCK_SEPARATOR+bound.getSince().getTimeOfDayClockFormat()+")";
			}
			return pattern;
		}
		private String printFullDayContext(String pattern, DayBound bound){
			pattern=pattern+"weekDayBetween("+bound.getSince() +Syntax.CLOCK_SEPARATOR+bound.getUntil()+")";
			return pattern;
		}
		
		private String printHalfCalendarContextRules(String pattern,TimeBound bound){
			if(bound.getSince().isHigherThan()){
			pattern = pattern  +"clockBetween("+bound.getSince().getTimeOfDayClockFormat()+
					Syntax.CLOCK_SEPARATOR+"23:59:5)";
			}else{
				pattern = pattern  +"clockBetween(00:00:00"+
						Syntax.CLOCK_SEPARATOR+bound.getSince().getTimeOfDayClockFormat()+")";
			}
			return pattern;
		}

	private String printEventRules(){
		String auxiliar_comma[] = {"",","};
		String pattern = "";
		int j = 0;
		if(events.size()>0) pattern = pattern +" ssr( ( ";
		for(int i=0;i<events.size();i++){
			if(events.size()==1){
				pattern = pattern + auxiliar_comma[j]+events.get(i).getStatus()+events.get(i).getId()+" ) =>EPAS_"+id+" ). \n";
			}
				else if(i!=events.size()-1){
					pattern = pattern + auxiliar_comma[j]+events.get(i).getStatus()+events.get(i).getId()+" ) =>EPAS_"+id+" ). \n";
					pattern = pattern +" ssr( ( ";
				}
				else{
					pattern = pattern + auxiliar_comma[j]+events.get(i).getStatus()+events.get(i).getId()+" ) =>EPAS_"+id+" ). \n";
				}
		}
		if(events.size()>0) pattern = pattern +" ssr( ( ";
		for(int i=0;i<events.size();i++){
			if(events.size()==1){
			pattern = pattern + events.get(i).getNegatedStatus()+events.get(i).getId() +" ) =>"+Syntax.NEGATIVE_SIGN+"EPAS_"+id+" ). \n";
		}
			else if(i!=events.size()-1){
				pattern = pattern + events.get(i).getNegatedStatus()+events.get(i).getId() +" ) => "+Syntax.NEGATIVE_SIGN+"EPAS_"+id+" ). \n";
				pattern = pattern +" ssr( ( ";
			}
			else{
				pattern = pattern + events.get(i).getNegatedStatus()+events.get(i).getId() +" ) => "+Syntax.NEGATIVE_SIGN+"EPAS_"+id+" ). \n";
			}
			
		}
		return pattern;
}


	
	private String printActionRules(){
		String pattern = "",auxiliar_pattern = "";
		String auxiliar_comma[] = {""," , "};
		int j = 0;
		auxiliar_pattern = auxiliar_pattern + " ssr( ( ";
		if( events.size() > 0 ){
			auxiliar_pattern = auxiliar_pattern + "[-]["+delay+"]EPAS_"+id;
			j++;
		}
		if( sensor_context.size() > 0 ){
			auxiliar_pattern = auxiliar_pattern + auxiliar_comma[j]+"[-]["+delay+"]sensor_context_"+id;
			if(j==0)j++;
		}
		if( calendar_context.size() > 0 ){
			auxiliar_pattern = auxiliar_pattern + auxiliar_comma[j]+"calendar_context_"+id;
		}
		if(	day_context.size() >0){
			auxiliar_pattern = auxiliar_pattern + auxiliar_comma[j]+"day_context_"+id;
		}

		for(int i=0;i<consequences.size();i++){
			
			pattern = pattern + auxiliar_pattern + " ) => " + consequences.get(i).getStatus()+consequences.get(i).getId() +" ). \n";
			pattern = pattern + auxiliar_pattern + " ) => Pattern " +id+" ).\n";
		}
		
		return pattern;
	}
	
}
