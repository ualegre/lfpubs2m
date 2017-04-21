package edu.casetools.lfpubs2m.lfpubsdata;

import java.util.HashMap;

import edu.casetools.lfpubs2m.lfpubsdata.condition.time.TimeBound;

public class Storage {

	public static HashMap<String,String>Context;
	public static HashMap<String,String>EventsName=new HashMap<String,String>();
	
	static{
		if(Context==null){
			Context=new HashMap<String,String>();
		}
	
	}
	
}
