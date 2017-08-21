package edu.casetools.lfpubs2m.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.casetools.lfpubs2m.core.lfpubsdata.GeneralCondition;
import edu.casetools.lfpubs2m.core.lfpubsdata.LFPUBSPattern;
import edu.casetools.lfpubs2m.core.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.core.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.core.lfpubsdata.condition.time.TimeBound;
import edu.casetools.lfpubs2m.core.reader.LFPUBSPatternReader;
import edu.casetools.lfpubs2m.core.reader.Syntax;
import edu.casetools.lfpubs2m.core.reader.Syntax.CommandType;


public class LFPUBS2MTranslator {
	
//	private final String    			 path     ;= FilesConfigs.PATH;
	private 	  String    		     fileName ;//= FilesConfigs.INPUT_FILENAME;
	private 	  File 	     			 file;
	private 	  FileReader 		     fileReader;
	private 	  BufferedReader   		 bufferedReader;
	private 	  LFPUBSPatternReader 			 inputInterpreter;
	private 	  boolean				 debug;
	private 	  GeneralCondition generalCondition = new GeneralCondition();
	private  	  HashMap<String,Integer> states=new HashMap<String,Integer>();
	private  	  HashMap<String, Object> context=new HashMap<String, Object>();
	
//	private enum  STATES { ID, ON_OCCURS, IF_CONTEXT, THEN_DO };
	
	public LFPUBS2MTranslator(boolean debug){
		inputInterpreter = new LFPUBSPatternReader(debug);
		this.debug = debug;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	
	public void open (){
		try {
			file  		 		     = new File          (  fileName  );
			fileReader 				 = new FileReader 	 (     file	   );
			bufferedReader			 = new BufferedReader( fileReader );	
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileReader(FileReader fileReader) {
		this.fileReader = fileReader;
	}

	public String getFileName() {
		return fileName;
	}
	

	public File getFile() {
		return file;
	}

	public FileReader getFileReader() {
		return fileReader;
	}

	public LFPUBSPatternReader getInputInterpreter() {
		return inputInterpreter;
	}

	public HashMap<String, Integer> getStates() {
		return states;
	}
	

	public boolean isDebug() {
		return debug;
	}

	public Vector<LFPUBSPattern> readPatterns() {
		Vector<LFPUBSPattern> patterns = new Vector<LFPUBSPattern>();
	    LFPUBSPattern auxiliarPattern = new LFPUBSPattern();
		CommandType commandType = CommandType.EMPTY;
		
	
		String line;
		try {
			open();
			line = bufferedReader.readLine();
			
			while(line != null){
				line = line.replaceAll("\\s","");
				if(debug)System.out.println(""+line);
				if(line.contains(Syntax.DAYOFWEEK_START)){
					commandType=CommandType.DAYOFWEEK;
				}
				if( line.contains( Syntax.ACTION_PATTERN_ID_START ) ){
					commandType = CommandType.ACTION_PATTERN_ID;
				}
				if( line.contains( Syntax.ON_OCCURS_START ) ){
					commandType = CommandType.ON_OCCURS;
			    }
				if( line.contains( Syntax.IF_CONTEXT_START ) ){
					commandType = CommandType.IF_CONTEXT;
				}
				if( line.contains( Syntax.THEN_DO_START ) ){
					commandType = CommandType.THEN_DO;
				}
				generalCondition=inputInterpreter.interpretCommandGeneralCondition(line, commandType, generalCondition);
				auxiliarPattern   = inputInterpreter.interpretCommand(auxiliarPattern, line, commandType);
				if( commandType.equals(CommandType.THEN_DO) ){
					patterns.add(auxiliarPattern);
					auxiliarPattern = new LFPUBSPattern();
				}
				
				commandType = CommandType.EMPTY;
				line     = bufferedReader.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
		return patterns;
	}
	
	public String getTranslation(String filename){
		setFileName(filename);
		Vector<LFPUBSPattern> patterns = readPatterns();
		if(debug)System.out.println(patterns.size()+" patterns were detected.");
		patterns=defineStates(patterns);
		patterns=CreatingOutputRules(patterns, generalCondition);
		patterns=concatenatePatterns(patterns);
		return printPatterns(patterns);
		
	}
	
	private Vector<LFPUBSPattern> CreatingOutputRules(Vector<LFPUBSPattern> patterns,GeneralCondition generalCondition) {
		
		String time_context,day_bound, sensor_bound;
		TimeBound specificbound=new TimeBound();
		
		TimeBound narrowestTimeBound=DefineGeneralTimeContext(generalCondition.getTimebound());
		context.put( "actionMap_time_context", generalCondition.getTimebound());
		states.put("actionMap_time_context", 2);
		
		Vector<DayBound> narrowestDayBoundVector=DefineGeneralDayContext(generalCondition.getDayOfWeek());
		if(narrowestDayBoundVector.size()>1){
			for(int i=0;i<narrowestDayBoundVector.size();i++){
				context.put("actionMap_day_context_"+i, narrowestDayBoundVector.get(i));
				states.put("actionMap_day_context_"+i, 2);
			}
		}
		else{
			context.put("actionMap_day_context", narrowestDayBoundVector.get(0));
			states.put("actionMap_day_context", 2);
		}
		
		for(int i=0;i<patterns.size();i++){
			time_context="time_context_"+i;
			day_bound="day_context_+"+i;
			sensor_bound="sensor_context_"+i;
			if(patterns.get(i).getCalendar_context().size()!=0){
				for(int j=0; j<patterns.get(i).getCalendar_context().size();j++){
					GeneralCondition specificCondition=new GeneralCondition();
					specificbound=DefineCalendarContext(patterns.get(i).getCalendar_context().get(j), generalCondition.getTimebound(),narrowestTimeBound);
					specificCondition.setTimebound(specificbound);
					context.put(time_context,specificCondition.getTimebound());
					states.put(time_context, 2);
					
				}
				patterns.get(i).addOutput(time_context);
				
			}
			else{
					time_context="actionMap_time_context";
					patterns.get(i).addOutput(time_context);
				}
			//Approach if specific Conditions introduce Day Boundaries	
			if(patterns.get(i).getDay_context().size()!=0){
				//As no Specific Condition will have a day boundary not written in GeneralCondition, only the specific condition will be taken into account 
				Vector<DayBound>specificDays=DefineDayContext(patterns.get(i).getDay_context(), generalCondition.getDayOfWeek(), day_bound, narrowestDayBoundVector);
				if(specificDays.size()>1){
					for(int z=0;z<specificDays.size();z++){
						context.put("day_context_"+z, specificDays.get(z));
						states.put("day_context_"+z, 2);
						patterns.get(i).addOutput("day_context_"+z);
					}
				}
				else{
					context.put("day_context_"+i, specificDays.get(0));
					states.put("day_context_"+i, 2);
					patterns.get(i).addOutput("day_context_"+i);
				}
			}
			else{
				Iterator<String> it= context.keySet().iterator();
				while(it.hasNext()){
					String key= (String) it.next();
					if(key.contains("actionMap_day_context")==true){
					day_bound=key;
					patterns.get(i).addOutput(day_bound);
				}
				}
			}
			if(patterns.get(i).getSensor_context().size()!=0){
				for(int k=0;k<patterns.get(i).getSensor_context().size();k++){
				context.put(sensor_bound, patterns.get(i).getSensor_context().get(i));
				patterns.get(i).addOutput(sensor_bound);
				states.put(sensor_bound, 2);
				}
			}
		}
		return patterns;
	}

	private Vector<DayBound> DefineGeneralDayContext(Vector<String> dayOfWeek) {
		Vector<DayBound>generalDayBound=new Vector<DayBound>();
		DayBound generalCondition=new DayBound();
		String[] weekDays={"monday", "tuesday", "wednesday", "thursday", "friday"};
		String [] weekends={"saturday", "sunday"};
	
			if(dayOfWeek.size()==7){
				generalCondition.setSince("monday");
				generalCondition.setUntil("sunday");
				generalDayBound.addElement(generalCondition);
			}
			else if(dayOfWeek.containsAll(Arrays.asList(weekends))==true){
				generalCondition.setSince("saturday");
				generalCondition.setUntil("sunday");
				generalDayBound.addElement(generalCondition);
			}
			else if(dayOfWeek.containsAll(Arrays.asList(weekDays))==true){
				generalCondition.setSince("monday");
				generalCondition.setUntil("friday");
				generalDayBound.addElement(generalCondition);
			}
			else if(isSeparated(dayOfWeek)==true){
				for(int i=0;i<dayOfWeek.size();i++){
					DayBound generalConditions=new DayBound();
					generalConditions.setSince(dayOfWeek.get(i));
					generalDayBound.add(generalConditions);
					
				}
			}
			else if(isSeparated(dayOfWeek)==false){
				if(generalCondition.getSince()!=null) 
					generalCondition.setSince(dayOfWeek.get(0));
				if(generalCondition.getUntil()!=null) 
					generalCondition.setUntil(dayOfWeek.get(dayOfWeek.size()-1));
				generalDayBound.add(generalCondition);
			}
		return generalDayBound;
	}

	private boolean isSeparated(Vector<String> dayOfWeek) {
		String[] week={"monday", "tuesday", "wednesday", "thursday", "friday","saturday", "sunday"};
		int find=findDay(dayOfWeek);
		for(int i=find;i<dayOfWeek.size();i++){
			int a=i+1;
			if(a!=dayOfWeek.size()==true){
				if(dayOfWeek.contains(week[a])==false){
				return true;
					}
				}
			
			}
		return false;
		}

	private int findDay(Vector<String> dayOfWeek) {
		String[] week={"monday", "tuesday", "wednesday", "thursday", "friday","saturday", "sunday"};
		for(int i=0;i<week.length;i++){
			for(int j=0;j<dayOfWeek.size();j++){
				if(week[i].compareTo(dayOfWeek.get(j))==0){
					return i;
				}
				
			}
		}
		return -1;
	}

	private TimeBound DefineGeneralTimeContext(TimeBound timebound) {
		TimeBound narrowestTimeBound= new TimeBound();
		narrowestTimeBound.setUntil(generalCondition.getTimebound().getUntil());
		narrowestTimeBound.setSince(generalCondition.getTimebound().getSince());
		return narrowestTimeBound;
	}

	private Vector<DayBound> DefineDayContext(Vector<String> day_context, Vector<String> dayOfWeek, String day_bound, Vector<DayBound> narrowestDayBoundVector) {
		DayBound generalCondition= new DayBound();
		Vector<DayBound>generalDayBound= new Vector<DayBound>();
		
		if(day_context.size()>1){
			generalCondition.setSince(day_context.get(0));
			generalCondition.setUntil(day_context.get(day_context.size()-1));
			generalCondition.setPriority(1);
			generalDayBound.add(generalCondition);
		}
		else{
			generalCondition.setSince(day_context.get(0));
			generalCondition.setPriority(1);
			generalDayBound.add(generalCondition);
		}
		
		return generalDayBound;
	}

	private TimeBound DefineCalendarContext(TimeBound calendar_context, TimeBound timebound, TimeBound narrowestTimeBound) {
			if(calendar_context.getUntil()!=null && generalCondition.getTimebound().getUntil() != null){
				if(calendar_context.getUntil().getMiliseconds()<generalCondition.getTimebound().getUntil().getMiliseconds()){
					narrowestTimeBound.setUntil(calendar_context.getUntil());
					narrowestTimeBound.setPriority(0);
					}
				}
			if(calendar_context.getSince()!=null && generalCondition.getTimebound().getSince() != null){
				if(calendar_context.getSince().getMiliseconds()>generalCondition.getTimebound().getSince().getMiliseconds()){
					narrowestTimeBound.setSince(calendar_context.getSince());
					narrowestTimeBound.setPriority(0);
					}
				}
	
		return narrowestTimeBound;
	}

	private Vector<LFPUBSPattern> defineStates(Vector<LFPUBSPattern> patterns) {
		for(int i=0; i<patterns.size();i++){	
			String rule="Pattern_"+i;
			if(patterns.get(i).getActuator()==false){
			for(int j=0;j<patterns.get(i).getEvents().size();j++){
				if((patterns.get(i).getEvents().get(j).isNegative()==true)&&(states.containsKey(patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId())==false)){
					String event=patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId();
					String oposevent=patterns.get(i).getEvents().get(j).getNegatedStatus()+patterns.get(i).getEvents().get(j).getId();
					states.put(event,-3);
					if(states.containsKey(oposevent)==false){
					states.put(oposevent, 3);
				}
				}
				else if((patterns.get(i).getEvents().get(j).isNegative()==false)&&(states.containsKey(patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId())==false)){
					String event=patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId();
					String oposevent=patterns.get(i).getEvents().get(j).getNegatedStatus()+patterns.get(i).getEvents().get(j).getId();
					states.put(event,3);
					if(states.containsKey(oposevent)==false){
						states.put(oposevent, -3);
					}
					
				}
			}
			for(int j=0;j<patterns.get(i).getConsequences().size();j++){
				if((patterns.get(i).getConsequences().get(j).isNegative()==true)&&(states.containsKey(patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId())==false)){
					String event=patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId();
					String oposevent=patterns.get(i).getConsequences().get(j).getNegatedStatus()+patterns.get(i).getConsequences().get(j).getId();
					states.put(event,-3);
					if(states.containsKey(oposevent)==false){
						states.put(oposevent, 3);
					}
				}
				else if((patterns.get(i).getConsequences().get(j).isNegative()==false)&&(states.containsKey(patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId())==false)){
					String event=patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId();
					String oposevent=patterns.get(i).getConsequences().get(j).getNegatedStatus()+patterns.get(i).getConsequences().get(j).getId();
					states.put(event,3);
					if(states.containsKey(oposevent)==false){
						states.put(oposevent, -3);
					}
			}
				}
			patterns.get(i).addOutput(rule);
			states.put(rule, 1);
			}
			else{for(int j=0;j<patterns.get(i).getEvents().size();j++){
				if((patterns.get(i).getEvents().get(j).isNegative()==true)&&(states.containsKey(patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId())==false)){
					String event=patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId();
					states.put(event,-3);
				}
				else if((patterns.get(i).getEvents().get(j).isNegative()==false)&&(states.containsKey(patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId())==false)){
					String event=patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId();
					states.put(event,3);
					
				}
			}
			for(int j=0;j<patterns.get(i).getConsequences().size();j++){
				if((patterns.get(i).getConsequences().get(j).isNegative()==true)&&(states.containsKey(patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId())==false)){
					String event=patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId();
					states.put(event,-1);
				}
				else if((patterns.get(i).getConsequences().get(j).isNegative()==false)&&(states.containsKey(patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId())==false)){
					String event=patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId();
					states.put(event,1);
			}
			patterns.get(i).addOutput(rule);
			states.put(rule, 1);
			}
		}
				
			}

		return patterns;
	}

	private Vector<LFPUBSPattern> concatenatePatterns(Vector<LFPUBSPattern> patterns) {
		int pattern=0;
		for(int i=0;i<patterns.size();i++){
			String rule="Pattern_"+i;
			for(int j=0;j<patterns.get(i).getConsequences().size();j++){
				String consequence=patterns.get(i).getConsequences().get(j).getStatus()+patterns.get(i).getConsequences().get(j).getId();
				Vector<Integer>findPath=new Vector<Integer>();
				 pattern=i;
				findPath=findPattern(patterns, consequence,findPath, pattern);
				if(findPath.size()==1){
					patterns.get(findPath.get(0)).addOutput(rule);
				}
				else if (findPath.size()>1){
					for(int k=0;k<findPath.size();k++){
					/*	if((patterns.get(findPath.get(k)).getFreq()==0)||(patterns.get(findPath.get(k)).getFreq()>freq)){
							freq=patterns.get(findPath.get(k)).getFreq();
							pos=k;
						}*/
						patterns.get(findPath.get(k)).addOutput(rule);///freq haundixena daukena hartu
					}
					 
				}
			}
		}
		
		return patterns;
	}

	private Vector<Integer> findPattern(Vector<LFPUBSPattern> patterns, String consequence, Vector<Integer>findPath, int numb) {
		for(int i=0;i<patterns.size();i++){
			for(int j=0;j<patterns.get(i).getEvents().size();j++){
				String event=patterns.get(i).getEvents().get(j).getStatus()+patterns.get(i).getEvents().get(j).getId();
				if((consequence.compareTo(event)==0)&&(numb!=i)){
					findPath.add(i);
				}
			}
			
		}
		return findPath;
	}

	private String printPatterns(Vector<LFPUBSPattern> patterns){
		String result = "";
		result=printStates();
		result=result+printIndepentStates();
		result=result+printInitialStatus();
		result=result+printContext();
		
		for(int i=0;i<patterns.size();i++){
			result = result + " \n";
			//result = result + "Pattern ID: "+patterns.get(i).getId()+"\n";
			result = result + ""+patterns.get(i).printPattern()+"\n";
		}
		
		return result;
	}
	
	public String printIndepentStates(){
		Iterator<String> it=states.keySet().iterator();
		String pattern="";
		while(it.hasNext()){
			String key = (String) it.next();
			if(((states.get(key).intValue()==2)||(states.get(key).intValue()==3))&&((key.contains("context")==false))){
				pattern=pattern+" is( "+key.substring(0, key.length()-2)+" ); \n";
				
				}
			else if (key.contains("context")==true){
				pattern=pattern+" is( "+key+" ); \n";
				
			}
			}
		pattern=pattern+"\n";
		return pattern;
		}
	
	private String printInitialStatus(){
		String pattern="";
	Iterator<String> it=states.keySet().iterator();
	while(it.hasNext()){
		String key=(String) it.next();
		if((key.contains("Pat")==false)&&(key.contains("context")==false)){
			if(states.get(key).intValue()<0){
				key=key.substring(0,key.length()-2);
				pattern=pattern+" holdsAt( "+key+" ,0 ); \n";
			}
			else{
				key=key.substring(0,key.length()-2);
					pattern=pattern+" holdsAt( "+Syntax.NEGATIVE_SIGN+key+" ,0 ); \n";
				}
				
			}
	else{
		pattern=pattern+" holdsAt( "+Syntax.NEGATIVE_SIGN+key+" ,0 ); \n";
	}
	}
	pattern=pattern+"\n";
	return pattern;
	}
	private String printStates(){
		String auxiliar_comma[] = {""," , "};
		int j=0;
		String pattern=" states( ";
		Iterator<String> it=states.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			if(states.get(key).intValue()>0){
				if((key.contains("Pat")==false)&&(key.contains("context")==false)){
				pattern=pattern+auxiliar_comma[j]+key.substring(0,key.length()-2);
				if(j==0)j++;
			}
				else{
					pattern=pattern+auxiliar_comma[j]+key;
					if(j==0)j++;
				}
			}
		}
		pattern=pattern+" ); \n";
		return pattern;
	}
	
	String printContext(){
		String pattern="";
		Iterator<String> it=context.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			if(key.contains("time_context")==true){
				Object bound=context.get(key);
				pattern=pattern+printCalendarContext(key, bound);
			}
			else if (key.contains("day_context")==true){
				Object bound=context.get(key);
				pattern=pattern+printDayContext(key, bound);
			
			}
			else if( key.contains("sensor_context")==true){
				Object sensor=context.get(key);
				pattern=pattern+printSensorContext(key,sensor);
			}
		}
		return pattern;
		
	}
	public String printCalendarContext(String rule, Object Timebound){
		TimeBound bound=(TimeBound) Timebound;
		String pattern=" ssr( ( clockBetween("+bound.getSince().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+bound.getUntil().getTimeOfDayClockFormat()+" ) ) -> "+rule+" ); \n";
		pattern=pattern+" ssr( ( "+Syntax.NEGATIVE_SIGN+"clockBetween("+bound.getSince().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+bound.getUntil().getTimeOfDayClockFormat()+" ) ) -> "+Syntax.NEGATIVE_SIGN+rule+" ); \n";
		return pattern;
	}
	
	public String printDayContext(String rule, Object Daybound){
		String pattern="";
		DayBound bound=(DayBound) Daybound;
			if(bound.getUntil()==null){
				pattern=pattern+" ssr( ( weekDayAt("+bound.getSince()+" ) ) -> "+rule+" ); \n";
				pattern=pattern+" ssr( ("+Syntax.NEGATIVE_SIGN+"weekDayAt("+bound.getSince()+" ) ) -> "+Syntax.NEGATIVE_SIGN+rule+" ); \n";
				}
			else{
				pattern=pattern+" ssr( ( weekDayBetween("+bound.getSince()+Syntax.CLOCK_SEPARATOR+bound.getUntil()+" ) ) ->"+rule+" ) ;\n";
				pattern=pattern+" ssr( ( "+Syntax.NEGATIVE_SIGN+"weekDayBetween("+bound.getSince()+Syntax.CLOCK_SEPARATOR+bound.getUntil()+" ) ) ->"+Syntax.NEGATIVE_SIGN+rule+" ); \n";
			}
			
		return pattern;
	}
	public String printSensorContext(String rule, Object Sensorbound){
		SensorBound bound=(SensorBound) Sensorbound;
		String pattern=" ssr( ( sensorBetween("+ bound.getSince().getStatus()+bound.getSince().getId()+Syntax.CLOCK_SEPARATOR+bound.getUntil().getStatus()+bound.getUntil().getId()+" ) ->"+rule+"); \n";
		pattern=pattern+ " ssr( ( sensorBetween("+ bound.getSince().getNegatedStatus()+bound.getSince().getId()+Syntax.CLOCK_SEPARATOR+bound.getUntil().getNegatedStatus()+bound.getUntil().getId()+" ) ->"+rule+"); \n";
		return pattern;
	}

	public void close(){
		try {
			
			bufferedReader.close();
			fileReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	


}