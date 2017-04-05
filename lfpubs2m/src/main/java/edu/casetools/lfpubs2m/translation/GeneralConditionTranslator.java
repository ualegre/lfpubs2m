package edu.casetools.lfpubs2m.translation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.casetools.lfpubs2m.lfpubsdata.GeneralCondition;
import edu.casetools.lfpubs2m.reader.Syntax;

public class GeneralConditionTranslator {

	String[] separatedData=null;
	String joinedData=null;
	Pattern p;
	Matcher m;
	public GeneralConditionTranslator(){
		
	}
	
	public GeneralCondition readDayOfWeek(String line, GeneralCondition generalCondition){
		p=Pattern.compile(Syntax.DAYOFWEEK+Syntax.DAYOFWEEK_STOP);
		m=p.matcher(line);
		if(m.find()){
			generalCondition.setDayOfWeek(m.group(1));
		}
		return generalCondition;
	}
	public GeneralCondition readTimeOfDaySmall(String line, GeneralCondition generalCondition){
		p=Pattern.compile(Syntax.TIMEOFDAYSMALL+Syntax.TIMEOFDAYSMALL_STOP);
		m=p.matcher(line);
		if(m.find()){
			generalCondition.setTimeDaySmall(m.group(1));
		}
		return generalCondition;
	}
	public GeneralCondition readTimeOfDayBig(String line, GeneralCondition generalCondition){
		p=Pattern.compile(Syntax.TIMEOFDAYBIG+Syntax.TIMEOFDAYBIG_STOP);
		m=p.matcher(line);
		if(m.find()){
			generalCondition.setTimeDayBig(m.group(1));
		}
		return generalCondition;
	}
	

}
