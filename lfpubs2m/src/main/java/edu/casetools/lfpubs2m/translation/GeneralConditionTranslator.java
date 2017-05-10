package edu.casetools.lfpubs2m.translation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.casetools.lfpubs2m.lfpubsdata.GeneralCondition;
import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeOfDay;
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
		TimeOfDay since=new TimeOfDay();
		p=Pattern.compile(Syntax.TIMEOFDAYSMALL+Syntax.TIMEOFDAYSMALL_STOP);
		m=p.matcher(line);
		if(m.find()){
			since.setTimeOFDaySince(m.group(1));
			generalCondition.setSince(since);
		}
		return generalCondition;
	}
	public GeneralCondition readTimeOfDayBig(String line, GeneralCondition generalCondition){
		TimeOfDay until=new TimeOfDay();
		p=Pattern.compile(Syntax.TIMEOFDAYBIG+Syntax.TIMEOFDAYBIG_STOP);
		m=p.matcher(line);
		if(m.find()){
			until.setTimeOFDayUntil(m.group(1));
			generalCondition.setUntil(until);
		}
		return generalCondition;
	}
	

}
