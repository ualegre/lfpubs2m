package edu.casetools.lfpubs2m.core.translation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.casetools.lfpubs2m.core.lfpubsdata.action.ThenDo;
import edu.casetools.lfpubs2m.core.lfpubsdata.events.Sensor;
import edu.casetools.lfpubs2m.core.reader.Syntax;


public class DoTranslator {
	String[] separateData = null;
	String   joinedData   = null;
	Pattern p;
	Matcher m;
	
	
	public ThenDo readFirstElementOfDo(String line, ThenDo thenDo){
		p = Pattern.compile(Syntax.THEN_DO_FIRST_ELEMENT_PATTERN);
		m = p.matcher(line);
		if( m.find()) {
			thenDo.setType( m.group(1) );
		}
		return thenDo;
	}
	
	public ThenDo readSecondElementOfDo(String line, ThenDo thenDo){
		p = Pattern.compile(Syntax.THEN_DO_SECOND_ELEMENT_PATTERN+thenDo.getType()+",\\((.*?)\\)"+","+thenDo.getTime()+"\\"+Syntax.THEN_DO_STOP);
		m = p.matcher(line);
		if( m.find()) {

			joinedData   = m.group(1); 
		    separateData = joinedData.split(Syntax.ON_OCCURS_SECOND_ELEMENT_SEPARATOR); 
		    for(int i = 0;i<separateData.length;i++){

			    	if(separateData.length > 1){
			    		thenDo = readMultipleSensors(separateData[i],thenDo);
			    	}else{
			    		thenDo = readSensor(separateData[i],thenDo);
			    	}

		    }
		}
		return thenDo;
	}
	
	public ThenDo readThirdElementOfDo(String line, ThenDo thenDo){
		p = Pattern.compile(Syntax.THEN_DO_THIRD_ELEMENT_PATTERN);
		m = p.matcher(line);
		if(m.find() ){
			joinedData   = m.group(1); 
		    separateData = joinedData.split(","); 
		    for(int i = 0;i<separateData.length;i++) if(i == separateData.length-1) {
		    	thenDo.setTime(separateData[i]);
		    }
		
		}
		return thenDo;
	}
	
	public ThenDo readForthElementOfDo(String line, ThenDo thenDo){
		p = Pattern.compile("when"+thenDo.getTime()+"\\=(.*?)\\+");
		m = p.matcher(line);
		if(m.find() ) thenDo.setWhenBase(m.group(1));
		return thenDo;
	}
	
	public ThenDo readFifthElementOfDo(String line, ThenDo thenDo){
		String auxiliar = null;
		p = Pattern.compile("\\+[0-9](.*?)s\\.");
		m = p.matcher(line);
		if(m.find() ){
			auxiliar = m.group(0).substring(0, m.group(0).length() - 2);
			thenDo.setWhenPlus(auxiliar.substring(1));
		}
		return thenDo;
	}
	
	private ThenDo readMultipleSensors(String data, ThenDo thenDo){
	    String[] separateData = data.split(Syntax.THEN_DO_SENSOR_SEPARATOR); 
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
	   thenDo.setConsequence(sensor);
		
		return thenDo;
	}
	
	private ThenDo readSensor(String data, ThenDo thenDo){
	    String[] separateData = data.split(Syntax.THEN_DO_SENSOR_SEPARATOR); 
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

	   thenDo.setConsequence(sensor);
		
		return thenDo;
	}
	
}
