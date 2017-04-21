package edu.casetools.lfpubs2m.translation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.casetools.lfpubs2m.lfpubsdata.GeneralCondition;
import edu.casetools.lfpubs2m.lfpubsdata.condition.IfContext;
import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.ContextSensor;
import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayOfWeek;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeOfDay;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.reader.Syntax;


public class ContextTranslator {

	
	private String[] separateDataOR = null;
	private String[] separateDataAND = null;
	private String   joinedData   = null;
	private Pattern p;
	private Matcher m;
	private boolean debug;
	
	public ContextTranslator(boolean debug){
		this.debug = debug;
	}
	
	public IfContext readContext(String line, IfContext ifContext){

			line = removeIfContext(line);
			line = removeBrackets(line);
			ifContext = checkOR(line,ifContext);  

		return ifContext;
	}
	
	private String removeIfContext(String line){		
		return line.replaceAll(Syntax.IF_CONTEXT_START, "");
	}
	
	public IfContext checkOR(String line,IfContext ifContext){
	    separateDataOR = line.split(Syntax.IF_CONTEXT_FIRST_SEPARATOR); 
	    if(separateDataOR.length > 0 && line.contains(Syntax.IF_CONTEXT_FIRST_SEPARATOR)) {
	    	ifContext = multipleOR( ifContext, separateDataOR );
	    	}
	    else {
	    	ifContext = hasNoOR( ifContext, line );	
	    	}
		return ifContext;
	}
	
	public IfContext hasNoOR( IfContext ifContext, String line ){
		
		//System.out.println("NO HAY OR:"+line);
		ifContext = checkAND(ifContext,line);
		return ifContext;
	}
	
	public IfContext multipleOR(IfContext ifContext, String[] separateOR){
		int max=-2;
		int pos=0;
	    for(int i = 0;i<separateOR.length;i++){	
	    	if(getPriority(separateOR[i])>max){
	    		max=getPriority(separateOR[i]);
	    		pos=i;
	    	}
	    }
	    	
	    	//System.out.println("LEO OR:"+separateOR[i]);
	    	if(debug)System.out.println("----TIME BOUND-----");
	    	ifContext = checkAND(ifContext,separateOR[pos]);
	    	if(debug)System.out.println("-------------------");
		return ifContext;
	}
	public IfContext checkAND(IfContext ifContext, String line){
		    separateDataAND = line.split(Syntax.IF_CONTEXT_SECOND_SEPARATOR); 
		    if(separateDataOR.length > 0 && line.contains(Syntax.IF_CONTEXT_SECOND_SEPARATOR)) ifContext = multipleAND( ifContext, separateDataAND );
		    else 					      ifContext = hasNoAND( ifContext, line );	
		return ifContext;
	}
	
	private IfContext hasNoAND(IfContext ifContext, String line) {
		if(isCalendar(line)){
			hasNoANDCalendar(ifContext,line);
		}else if(!line.isEmpty()){
			hasNoANDSensor(ifContext,line);
		}
		return ifContext;
	}
	
	private IfContext hasNoANDCalendar(IfContext ifContext, String line) {
		TimeBound timeBound = new TimeBound();
		timeBound.setPriority(getPriority(line));
		if(debug)System.out.println("PRIORITY: "+timeBound.getPriority());
		line = removePriority(timeBound.getPriority(),line);
		//System.out.println("VALUE:"+line);
		
		String boundData[] = line.split(Syntax.IF_CONTEXT_THIRD_SEPARATOR); 
		if(boundData.length == 2){
			if(boundData.length == 2){
				if(isHigherThan(boundData[0])==true){
				timeBound.setSinceString(boundData[1]);
				timeBound.getSince().setHigherThan(isHigherThan(boundData[0]));
				if(debug)System.out.println("HIGHER THAN: "+timeBound.getSince().isHigherThan());
				if(debug)System.out.println("CALENDAR VALUE: "+timeBound.getSince().getHour()+":"+timeBound.getSince().getMinute()+":"+timeBound.getSince().getSecond());
				if(debug)System.out.println("");
				}
				else{
					timeBound.setUntilString(boundData[1]);
					timeBound.getUntil().setHigherThan(isHigherThan(boundData[0]));
					if(debug)System.out.println("HIGHER THAN: "+timeBound.getUntil().isHigherThan());
					if(debug)System.out.println("CALENDAR VALUE: "+timeBound.getUntil().getHour()+":"+timeBound.getUntil().getMinute()+":"+timeBound.getUntil().getSecond());
					if(debug)System.out.println("");
				}
			}
		ifContext.addCalendarBound(timeBound);
		}
		return ifContext;
	}
	
	private boolean isHigherThan(String line){
		if(line.contains(">")) return true;
		else return false;
	}
	
	private String getSensorName(String line){
		int aux = 1;
		if(line.contains("=")) aux = 2;
		for(int i=0;i<aux;i++){
			line = line.substring(0, line.length() - 1);
		}
		return line;
	}
	
	private IfContext hasNoANDSensor(IfContext ifContext, String line) {
		SensorBound sensorBound = new SensorBound();
		sensorBound.setPriority(getPriority(line));
		if(debug)System.out.println("PRIORITY: "+sensorBound.getPriority());
		line = removePriority(sensorBound.getPriority(),line);
		//System.out.println("VALUE:"+line);
		
		String boundData[] = line.split(Syntax.IF_CONTEXT_THIRD_SEPARATOR); 
		if(boundData.length == 2){
			ContextSensor contextSensor= new ContextSensor();
			contextSensor.setHigherThan(isHigherThan(boundData[0]));
			contextSensor.setSensorName(getSensorName(boundData[0]));
			contextSensor.setValue(boundData[1]);
			sensorBound.setSince(contextSensor);

		
			if(debug)System.out.println("HIGHER THAN: "+sensorBound.getSince().isHigherThan());
			if(debug)System.out.println("SENSOR: "+sensorBound.getSince().getSensorName());
			if(debug)System.out.println("VALUE: "+sensorBound.getSince().getValue());
		ifContext.addSensorBound(sensorBound);
		}
		return ifContext;
	}
	
	private int getPriority(String line){
		line = line + Syntax.IF_CONTEXT_END;
		p = Pattern.compile(Syntax.IF_CONTEXT_PRIORITY_PATTERN);
			m = p.matcher(line);
			if( m.find()) {
				return Integer.parseInt( m.group(1) ); 
			}
			return 0;
	}
	
	private String removePriority(int priority,String line){
			line = line.replaceAll(Syntax.IF_CONTEXT_PRIORITY+priority, "");
			line = line.replaceAll(Syntax.IF_CONTEXT_PATTERN_END, "");
			return line;
	}

	public IfContext multipleAND(IfContext ifContext, String[] separateAND){
		
		//for(int i = 0;i<separateAND.length;i++){
			if(isCalendar(separateDataAND[0])&&isCalendar(separateDataAND[1])){
				ifContext = translateMultipleCalendar(ifContext,separateAND);
			}else if(( !separateDataAND[0].isEmpty() ) && ( !separateDataAND[1].isEmpty() )){ 
				ifContext = translateMultipleSensor(ifContext,separateAND);
			}
		 
		return ifContext;
	}



	private IfContext translateMultipleSensor(IfContext ifContext,
			String[] separateAND) {
		SensorBound sensorBound = new SensorBound();
		sensorBound.setPriority(getPriority(separateAND[1]));
		if(debug)System.out.println("PRIORITY: "+sensorBound.getPriority());
		separateAND[1] = removePriority(sensorBound.getPriority(),separateAND[1]);
		//System.out.println("VALUE:"+line);
		
		String boundDataSince[] = separateAND[0].split(Syntax.IF_CONTEXT_THIRD_SEPARATOR); 
		if(boundDataSince.length == 2){
			ContextSensor contextSensor= new ContextSensor();
			contextSensor.setHigherThan(isHigherThan(boundDataSince[0]));
			contextSensor.setSensorName(getSensorName(boundDataSince[0]));
			contextSensor.setValue(boundDataSince[1]);
			sensorBound.setSince(contextSensor);
		}
		
		if(debug)System.out.println("HIGHER THAN: "+sensorBound.getSince().isHigherThan());
		if(debug)System.out.println("SENSOR: "+sensorBound.getSince().getSensorName());
		if(debug)System.out.println("VALUE: "+sensorBound.getSince().getValue());
		if(debug)System.out.println("");
		String boundDataUntil[] = separateAND[1].split(Syntax.IF_CONTEXT_THIRD_SEPARATOR); 
		if(boundDataUntil.length == 2){
			ContextSensor contextSensor= new ContextSensor();
			contextSensor.setHigherThan(isHigherThan(boundDataUntil[0]));
			contextSensor.setSensorName(getSensorName(boundDataUntil[0]));
			contextSensor.setValue(boundDataUntil[1]);
			sensorBound.setUntil(contextSensor);
		}
		
		if(debug)System.out.println("HIGHER THAN: "+sensorBound.getUntil().isHigherThan());
		if(debug)System.out.println("SENSOR: "+sensorBound.getUntil().getSensorName());
		if(debug)System.out.println("VALUE: "+sensorBound.getUntil().getValue());
		ifContext.addSensorBound(sensorBound);
		return ifContext;
	}

	private IfContext translateMultipleCalendar(IfContext ifContext,String line[]) {	

		TimeBound timeBound = new TimeBound();
		timeBound.setPriority(getPriority(line[1]));	
		if(debug)System.out.println("PRIORITY: "+timeBound.getPriority());
		line[1] = removePriority(timeBound.getPriority(),line[1]);

			String boundDataSince[] = line[0].split(Syntax.IF_CONTEXT_THIRD_SEPARATOR); 
			if(boundDataSince.length == 2){
				if(isHigherThan(boundDataSince[0])==true){
				timeBound.setSinceString(boundDataSince[1]);
				timeBound.getSince().setHigherThan(isHigherThan(boundDataSince[0]));
				if(debug)System.out.println("HIGHER THAN: "+timeBound.getSince().isHigherThan());
				if(debug)System.out.println("CALENDAR VALUE: "+timeBound.getSince().getHour()+":"+timeBound.getSince().getMinute()+":"+timeBound.getSince().getSecond());
				if(debug)System.out.println("");
				}
				else{
					timeBound.setUntilString(boundDataSince[1]);
					timeBound.getUntil().setHigherThan(isHigherThan(boundDataSince[0]));
					if(debug)System.out.println("HIGHER THAN: "+timeBound.getUntil().isHigherThan());
					if(debug)System.out.println("CALENDAR VALUE: "+timeBound.getUntil().getHour()+":"+timeBound.getUntil().getMinute()+":"+timeBound.getUntil().getSecond());
					if(debug)System.out.println("");
				}
			}
			
			
			
		   String boundDataUntil[] = line[1].split(Syntax.IF_CONTEXT_THIRD_SEPARATOR); 
			if(boundDataUntil.length == 2){
				if(isHigherThan(boundDataSince[0])==true){
					timeBound.setSinceString(boundDataSince[1]);
					timeBound.getSince().setHigherThan(isHigherThan(boundDataSince[0]));
					if(debug)System.out.println("HIGHER THAN: "+timeBound.getSince().isHigherThan());
					if(debug)System.out.println("CALENDAR VALUE: "+timeBound.getSince().getHour()+":"+timeBound.getSince().getMinute()+":"+timeBound.getSince().getSecond());
					if(debug)System.out.println("");
					}
					else{
						timeBound.setUntilString(boundDataSince[1]);
						timeBound.getUntil().setHigherThan(isHigherThan(boundDataSince[0]));
						if(debug)System.out.println("HIGHER THAN: "+timeBound.getUntil().isHigherThan());
						if(debug)System.out.println("CALENDAR VALUE: "+timeBound.getUntil().getHour()+":"+timeBound.getUntil().getMinute()+":"+timeBound.getUntil().getSecond());
						if(debug)System.out.println("");
					}
			}
			ifContext.addCalendarBound(timeBound);
		
		
		return ifContext;
	}

	public String removeBrackets(String line){
		line = line.replaceAll("\\(", "");
		line = line.replaceAll("\\)", "");
		return line;
		
	}
	
	private boolean isCalendar(String string) {
		if(string.contains(Syntax.IF_CONTEXT_CALENDAR)){
			return true;
		}
		return false;
	}

	public IfContext setContext(IfContext ifContext, GeneralCondition generalCondition) {
		String smallTime=generalCondition.getTimeOfDaySmall();
		String bigTime=generalCondition.getTimeOfDayBig();
		int priority=2;
		
		TimeOfDay SmallTime=setTimeOfDayNotClockFormat(smallTime, String.valueOf(0));
		
		TimeOfDay BigTime=setTimeOfDayNotClockFormat(bigTime,String.valueOf(1));
		
		TimeBound timeBound=new TimeBound(SmallTime, BigTime, priority);
		ifContext.addCalendarBound(timeBound);
		
		String [] dayOfWeek=generalCondition.getDayOfWeek();
		String since=dayOfWeek[0];
		String until=dayOfWeek[dayOfWeek.length-1];
		DayBound week=new DayBound(since, until);
		ifContext.addDayBound(week);
		return ifContext;
	}
	public TimeOfDay setTimeOfDayNotClockFormat(String clockformat, String higher){
		TimeOfDay timeofDay=new TimeOfDay();
		String [] clockFormat=clockformat.split(":");
		timeofDay.setHour(clockFormat[0]);
		timeofDay.setMinute(clockFormat[1]);
		timeofDay.setSecond(clockFormat[2]);
		timeofDay.setMili(Integer.valueOf(timeofDay.getHour()), Integer.valueOf(timeofDay.getMinute()), Integer.valueOf(timeofDay.getSecond()));
		if(higher.compareTo("1")==0){
			timeofDay.setHigherThan(false);
		}
		else{
			timeofDay.setHigherThan(true);
		}
		return timeofDay;
	}
	
	
	
}
