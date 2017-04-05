package edu.casetools.lfpubs2m;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.LFPUBSPattern;
import edu.casetools.lfpubs2m.lfpubsdata.condition.sensor.SensorBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.DayBound;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;
import edu.casetools.lfpubs2m.lfpubsdata.events.Sensor;
//import edu.casetools.lfpubs2m.lfpubsdata.LFPUBSPattern;
import edu.casetools.lfpubs2m.reader.LFPUBSPatternReader;
import edu.casetools.lfpubs2m.reader.Syntax;
import edu.casetools.lfpubs2m.reader.Syntax.CommandType;
import edu.casetools.lfpubs2m.lfpubsdata.GeneralCondition;


public class LFPUBS2MTranslator {
	
//	private final String    			 path     ;= FilesConfigs.PATH;
	private 	  String    		     fileName ;//= FilesConfigs.INPUT_FILENAME;
	private 	  File 	     			 file;
	private 	  FileReader 		     fileReader;
	private 	  BufferedReader   		 bufferedReader;
	private 	  LFPUBSPatternReader 			 inputInterpreter;
	private 	  boolean				 debug;
	private final static String newline = "\n";
	private HashMap<String,Integer> states=new HashMap<String,Integer>();
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
	
	public Vector<LFPUBSPattern> readPatterns() {
		Vector<LFPUBSPattern> patterns = new Vector<LFPUBSPattern>();
	    LFPUBSPattern auxiliarPattern = new LFPUBSPattern();
		CommandType commandType = CommandType.EMPTY;
		GeneralCondition generalCondition=new GeneralCondition();
	
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
				auxiliarPattern   = inputInterpreter.interpretCommand(auxiliarPattern, line, commandType, generalCondition);
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
		return printPatterns(patterns);
		
	}
	
	public String getTranslation(Vector<LFPUBSPattern> patterns){
		if(debug)System.out.println(patterns.size()+" patterns were detected.");
		return printPatterns(patterns);
		
	}
	
	private String printPatterns(Vector<LFPUBSPattern> patterns){
		String actuator="Kettle";
		writeResults(patterns, actuator);
		String result = "";
		String states="";
		for(int i=0;i<patterns.size();i++){
			result = result + "___________________________________\n";
			result = result + "Pattern ID: "+patterns.get(i).getId()+"\n";
			result = result + ""+patterns.get(i).printPattern()+"\n";
			states=states+""+patterns.get(i).writeStructure(states);
		}
		result= result+ states;
		return result;
	}
	private void writeResults(Vector<LFPUBSPattern> patterns, String actuator){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("lfpubs2m.txt"));
			PrintWriter writer = new PrintWriter(bw);	
			for(int i=0;i<patterns.size();i++){
				writedoc(patterns.get(i),writer, actuator);
			}
			writeStates(writer);
			writeIndependentStates(writer);
			InitialStatus(writer);
			writer.close();
		}
		catch(Exception error){	
			System.out.println("Error Message: " + error.getMessage());
		}
	}
	public void writedoc(LFPUBSPattern pattern, PrintWriter writer, String actuator){
		if((pattern.getEvents().size()>0)&&(pattern.getConsequence().size()>0)){
			writeEvents(pattern.getEvents(), pattern.getId(),writer);
			writeContext(pattern.getCalendar_context(),pattern.getId(), writer);
			writer.println();
			writeContextNegative(pattern.getCalendar_context(),pattern.getId(), writer);
			writer.println();
			writeDayContextRules(pattern.getDay_context(), pattern.getId(),writer);
			writeDayContextRulesNegatives(pattern.getDay_context(),pattern.getId(),writer);
			writeAction(pattern.getDelay(),pattern.getId(), pattern.getCalendar_context(),pattern.getEvents(),pattern.getSensor_context(),pattern.getDay_context(),pattern.getConsequences(), writer, actuator);
			writer.println(newline);
		}
	}
	private void writeAction(String delay, String id, Vector<TimeBound> calendar_context,Vector<Sensor> events,Vector<SensorBound>sensor_context, Vector<DayBound>day_context, Vector<Sensor> consequences,PrintWriter writer, String actuator) {
		int pat=0;
		writer.print("ssr( (");
		if(events.size()>0){
			writer.print("[-]["+delay+"]EPAS_"+id+",");
		}
		if(calendar_context.size()>0){
			writer.print("calendar_context_"+id+",");
		}
		if(sensor_context.size()>0){
			writer.print("[-]["+delay+"]sensor_context_"+id+",");
		}
		if(day_context.size()>0){
			writer.print("day_context_"+id);
		}
		writer.print(" )->");
		for(int i=0;i<consequences.size();i++){
			writer.print(consequences.get(i).getStatus()+consequences.get(i).getId());
			if(states.containsKey(consequences.get(i).getId())==false){
				states.put(consequences.get(i).getId(), 0);
			}
			if(consequences.get(i).getId().contains(actuator)==true){
				pat=-1;
			}
		}
		writer.print(" );");
		if(pat==0){
			writer.println();
			writefinalPattern(delay, calendar_context, id, consequences, sensor_context, day_context, consequences, writer);
		}
		else{
			writeFinalPattern(writer, actuator);
		}
		
		
	}
	public void writeFinalPattern(PrintWriter writer, String actuator){
		writer.println();
		writer.print("ssr( (");
		Iterator it=states.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			if(key.contains("Pattern")==true){
				writer.print(key+",");
			}
		}
		writer.print(" )->Kettle(0) )");
	
	}
	

	public void writeStates( PrintWriter writer){
		String auxiliar_comma[] = {",",","};
		int j=0;
		Iterator it=states.keySet().iterator();
		writer.print("states( ");
		while(it.hasNext()){
			String key = (String) it.next();
			if(states.get(key).intValue()>=0){
			writer.print(key+auxiliar_comma[j]);
			if(j==0)j++;
			
			}
		}
		//writer.print(states.get("Kettle(0),"));
		//writer.print(states.get(Syntax.NEGATIVE_SIGN+"Kettle(0)"));
		writer.print(" );");
	}
	public void writeIndependentStates(PrintWriter writer){
		Iterator it=states.keySet().iterator();
		writer.println(newline);
		while(it.hasNext()){
			String key = (String) it.next();
			if(states.get(key).intValue()!=1){
				writer.print("is( "+key+" );");
				writer.println();
				
				}
			}
		}
	public void InitialStatus(PrintWriter writer){
		Iterator it=states.keySet().iterator();
		writer.println(newline);
		while(it.hasNext()){
			String key = (String) it.next();
			//if(states.get(key).intValue()!=1){
				writer.print("holdsAt( "+key+",0 );");
				writer.println();
				
			}
		}
	
			

	private void writefinalPattern(String delay, Vector<TimeBound> calendar_context, String id,Vector<Sensor> events,Vector<SensorBound>sensor_context, Vector<DayBound>day_context, Vector<Sensor> consequences,PrintWriter writer) {
		writer.print("");
		writer.print("ssr( (");
		if(events.size()>0){
			writer.print("[-]["+delay+"]EPAS_"+id+",");
		}
		if(calendar_context.size()>0){
			writer.print("calendar_context_"+id+",");
		}
		if(sensor_context.size()>0){
			writer.print("[-]["+delay+"]sensor_context_"+id+",");
		}
		if(day_context.size()>0){
			writer.print("day_context_"+id);
		}
		writer.print(" )-> Pattern_"+id);
		writer.print(" );");
		String Pat="Pattern_"+id;
		if(states.containsKey(Pat)==false){
			states.put(Pat, +1);
		}
		
	}

	public void writeEvents(Vector<Sensor>events, String id,PrintWriter writer){
		if(events.size()>0){
			for(int i=0;i<events.size();i++){
				writer.println("ssr( ("+events.get(i).getStatus()+events.get(i).getId()+" )->EPAS_"+id+" );");
				writer.println("ssr( ("+events.get(i).getNegatedStatus()+events.get(i).getId()+" )->"+Syntax.NEGATIVE_SIGN+"EPAS_"+id+" );");
				String negindep=events.get(i).getNegatedStatus()+events.get(i).getId();
				if(states.containsKey(events.get(i).getId())==false){
					states.put(events.get(i).getId(), 0);
				}
				
				else if (states.containsKey(negindep)==false){
					states.put(negindep, -1);
				}
				String val="EPAS_"+id;
				String val_neg=Syntax.NEGATIVE_SIGN+"EPAS_"+id;
				if(states.containsKey(val)==false){
					states.put(val, 0);
				}
				else if(states.containsKey(val_neg)==false){
					states.put(val_neg, -1);
				}
			}
		}
	}
	public void writeContext(Vector<TimeBound>calendar_context,String id, PrintWriter writer){
		if(calendar_context.size()>0){
			for(int i=0;i<calendar_context.size();i++){
				if(calendar_context.get(i).getUntil()!=null){
					TimeBound timeBound=calendar_context.get(i);
					if(timeBound.getSince().isHigherThan()){
						writer.print("ssr( ( clockBetween("+timeBound.getSince().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+timeBound.getUntil().getTimeOfDayClockFormat()+")");
						
					}
					else{
						writer.print("ssr( ( clockBetween("+timeBound.getUntil().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+timeBound.getSince().getTimeOfDayClockFormat()+")");
					}
					
				}
				else{
					TimeBound timeBound=calendar_context.get(i);
					if(timeBound.getSince().isHigherThan()){
						writer.print(" ssr( ( clockBetween("+timeBound.getSince().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+"23:59:59)");
						
					}
					else{
						writer.print(" ssr( ( clockBetween(00:00:00"+Syntax.CLOCK_SEPARATOR+timeBound.getSince().getTimeOfDayClockFormat()+")");
					}
			}
			}
			writer.print(" ) ->calendar_context_"+id+" );");
			String context="calendar_context_"+id;
			//System.out.println(context);
			if(states.containsKey(context)==false){
				states.put(context, +2);
			}
		}
		}
	public void writeContextNegative(Vector<TimeBound>calendar_context, String id, PrintWriter writer){
		if(calendar_context.size()>0){
		for(int i=0;i<calendar_context.size();i++){
			if(calendar_context.get(i).getUntil()!=null){
				TimeBound timeBound=calendar_context.get(i);
				if(timeBound.getSince().isHigherThan()){
					writer.print("ssr( ( "+Syntax.NEGATIVE_SIGN+"clockBetween("+timeBound.getSince().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+timeBound.getUntil().getTimeOfDayClockFormat()+")");
					
				}
				else{
					writer.print("ssr( ( "+Syntax.NEGATIVE_SIGN+"clockBetween("+timeBound.getUntil().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+timeBound.getSince().getTimeOfDayClockFormat()+")");
				}
			}
			else{
				TimeBound timeBound=calendar_context.get(i);
				if(timeBound.getSince().isHigherThan()){
					writer.print("ssr( ( "+Syntax.NEGATIVE_SIGN+"clockBetween("+timeBound.getSince().getTimeOfDayClockFormat()+Syntax.CLOCK_SEPARATOR+"23:59:59)");
					
				}
				else{
					writer.print("ssr( ( "+Syntax.NEGATIVE_SIGN+"clockBetween(00:00:00"+Syntax.CLOCK_SEPARATOR+timeBound.getSince().getTimeOfDayClockFormat()+")");
				}	
		}
		}
		writer.print(" ) ->"+Syntax.NEGATIVE_SIGN+"calendar_context_"+id+" );");
		String context=Syntax.NEGATIVE_SIGN+"calendar_context_"+id;
		if(states.containsKey(context)==false){
			states.put(context, -2);
		}
		}
	}
	public void writeDayContextRules(Vector<DayBound>day_context, String id, PrintWriter writer){
		writer.print("ssr( ( ");
		for(int i=0;i<day_context.size();i++){
			DayBound bound=day_context.get(i);
				writer.print("weekDayBetween("+bound.getSince() +Syntax.CLOCK_SEPARATOR+bound.getUntil()+")");
				if(i == (day_context.size()-1) ){
					writer.println( " ) -> day_context_"+id+" );" );
			}
	}
	}
	public void writeDayContextRulesNegatives(Vector<DayBound>day_context, String id, PrintWriter writer){
		writer.print("ssr( ( ");
		for(int i=0;i<day_context.size();i++){
			DayBound bound=day_context.get(i);
				writer.print(Syntax.NEGATIVE_SIGN+"weekDayBetween("+bound.getSince() +Syntax.CLOCK_SEPARATOR+bound.getUntil()+")");
				if(i == (day_context.size()-1) ){
					writer.println( " ) -> "+Syntax.NEGATIVE_SIGN+"day_context_"+id+" );" );
				}
			}
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
