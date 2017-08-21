package edu.casetools.lfpubs2m.core.translation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.casetools.lfpubs2m.core.lfpubsdata.events.Occurs;
import edu.casetools.lfpubs2m.core.lfpubsdata.events.Sensor;
import edu.casetools.lfpubs2m.core.reader.Syntax;


public class OccursTranslator {
	String[] separateData = null;
	String   joinedData   = null;
	Pattern p;
	Matcher m;
	
	public OccursTranslator(){
		
	}
	public Occurs readFirstElementOfOccurs(String line, Occurs occurs){
		p = Pattern.compile(Syntax.ON_OCCURS_FIRST_ELEMENT_PATTERN);
		m = p.matcher(line);
		if( m.find()) occurs.setType( m.group(1) );//pattern.s(m.group(1));	
		return occurs;
	}
	
	public Occurs readSecondElementOfOccurs(String line, Occurs occurs){	
		
		p = Pattern.compile(Syntax.ON_OCCURS_SECOND_ELEMENT_PATTERN+occurs.getType()+",\\((.*?)\\)"+","+occurs.getTime()+"\\"+Syntax.ON_OCCURS_STOP);
		m = p.matcher(line);
		if( m.find()) {

			joinedData   = m.group(1); 
		    separateData = joinedData.split(Syntax.ON_OCCURS_SECOND_ELEMENT_SEPARATOR); 
		    for(int i = 0;i<separateData.length;i++){

			    	if(separateData.length > 1){
			    		occurs = readMultipleSensors(separateData[i],occurs);
			    	}else{
			    		occurs = readSensor(separateData[i],occurs);
			    	}

		    }
		}
		return occurs;
	}
	
	public Occurs readThirdElementOfOccurs(String line, Occurs occurs){
	
		p = Pattern.compile(Syntax.ON_OCCURS_THIRD_ELEMENT_PATTERN);
		m = p.matcher(line);
		if(m.find() ){
	
			joinedData   = m.group(1); 
		    separateData = joinedData.split(","); 
		    for(int i = 0;i<separateData.length;i++) if(i == separateData.length-1) {
		    	occurs.setTime(separateData[i]);
		    }
		
		}
		return occurs;
	}
	
	private Occurs readMultipleSensors(String data, Occurs occurs){
	    String[] separateData = data.split(Syntax.ON_OCCURS_SENSOR_SEPARATOR); 
		Sensor sensor = new Sensor();
	    
	    for(int i = 0;i<separateData.length;i++){
	    	switch(i){
	    	case 0:
	    		sensor.setStatus( separateData[i].substring(1) );
	    		break;
	    	case 1:
	    		String id=separateData[i].substring(0, separateData[i].length() - 1).replace("(", "_");
	    		String id_f=id.replace(")","");
	    		sensor.setId(id_f);
	    		break;
	    	default:
	    		break;
	    	}
	    }

	    occurs.setSensor(sensor);
		
		return occurs;
	}
	
	private Occurs readSensor(String data, Occurs occurs){
	    String[] separateData = data.split(Syntax.ON_OCCURS_SENSOR_SEPARATOR); 
		Sensor sensor = new Sensor();
	    
	    for(int i = 0;i<separateData.length;i++){
	    	switch(i){
	    	case 0:
	    		sensor.setStatus( separateData[i] );
	    		break;
	    	case 1:
	    		String id=separateData[i].substring(0, separateData[i].length() - 1).replace("(", "_");
	    		String id_f=id.replace(")","");
	    		sensor.setId(id_f);
	    		break;
	    	default:
	    		break;
	    	}
	    }

	   occurs.setSensor(sensor);
		
		return occurs;
	}
	
	
	
	public Occurs readForthElementOfOccurs(String line, Occurs occurs){
		line = line +Syntax.ON_OCCURS_FORTH_ELEMENT_STOP;
		p = Pattern.compile(Syntax.ON_OCCURS_FORTH_ELEMENT_PATTERN+"\\"+Syntax.ON_OCCURS_FORTH_ELEMENT_STOP);
		m = p.matcher(line);
		if(m.find()){
			occurs.setFrequency(Double.parseDouble( m.group(1) ) );
		}
		return occurs;
	}
	
}
