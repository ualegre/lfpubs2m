package edu.casetools.lfpubs2m.reader;

public class Syntax {
	
	public final static boolean SYNTAX_DEBUG = true;
	/*General Signs declaration*/
	public final static String NEGATIVE_SIGN       = "#";
	public final static String AND_SIGN      	   = "&";
	public final static String GENERIC_SEPARATOR   = ",";
	public final static String GENERIC_STOP        = ".";
	public final static String TRUE			       = "1";
	public final static String FALSE	           = "0";

	
	public static enum CommandType { EMPTY,ACTION_PATTERN_ID,ON_OCCURS,IF_CONTEXT,THEN_DO};

	public static enum RuleType {SAME_TIME_RULE,NEXT_TIME_RULE};
	/*Elements declaration syntax*/
	
	public final static String ACTION_PATTERN_ID_START       = "(ActionPattern";
	public final static String ACTION_PATTERN_ID_STOP        = ")";
	public final static String ACTION_PATTERN_ID_PATTERN     = "\\(ActionPattern(.*?)\\)";	

	
	public final static String ON_OCCURS_START       		 	  = "ONoccurs(";
	public final static String ON_OCCURS_STOP       		 	  = ")Frequency";
	public final static String ON_OCCURS_FIRST_ELEMENT_PATTERN    = "ONoccurs\\((.*?),";
	public final static String ON_OCCURS_SECOND_ELEMENT_PATTERN   = "ONoccurs\\(";
	public final static String ON_OCCURS_SECOND_ELEMENT_SEPARATOR = "&";
	public final static String ON_OCCURS_SENSOR_SEPARATOR = ",";
	public final static String ON_OCCURS_SENSOR_FIRST_ELEMENT_PATTERN = "\\((.*?),";
	public final static String ON_OCCURS_THIRD_ELEMENT_PATTERN    = ",(.*)\\)Frequency";
	public final static String ON_OCCURS_FORTH_ELEMENT_PATTERN    = "Frequency\\:(.*?)";
	public final static String ON_OCCURS_FORTH_ELEMENT_STOP   	  = ")";
	
	public final static String IF_CONTEXT_START      		 = "IFcontext";
	public final static String IF_CONTEXT_STOP      		 = ")";
	public final static String IF_CONTEXT_PATTERN            = "IFcontext\\((.*?)";
	public final static String IF_CONTEXT_FIRST_SEPARATOR    = "OR";
	public final static String IF_CONTEXT_SECOND_SEPARATOR   = "AND";
	public final static String IF_CONTEXT_THIRD_SEPARATOR    = ",";
	public static final String IF_CONTEXT_CALENDAR 	 		 = "time";
	public static final String IF_CONTEXT_END 		 		 = ")";
	public static final String IF_CONTEXT_PATTERN_END 		 = "\\)";
	public static final String IF_CONTEXT_PRIORITY_PATTERN 	 = "Priority\\:(.*?)"+IF_CONTEXT_PATTERN_END;
	public static final String IF_CONTEXT_PRIORITY 	 		 = "Priority:";

	
	public final static String THEN_DO_START      		   	 		= "THENdo(";
	public final static String THEN_DO_STOP      			 		= ")when";
	public final static String THEN_DO_FIRST_ELEMENT_PATTERN    	= "THENdo\\((.*?),";
	public final static String THEN_DO_SECOND_ELEMENT_PATTERN  		= "THENdo\\(";
	public final static String THEN_DO_SECOND_ELEMENT_SEPARATOR 	= "&";
	public final static String THEN_DO_SENSOR_SEPARATOR 			= ",";
	public final static String THEN_DO_SENSOR_FIRST_ELEMENT_PATTERN = "\\((.*?),";
	public final static String THEN_DO_THIRD_ELEMENT_PATTERN    	= ",(.*)\\)when";
	public final static String THEN_DO_FORTH_ELEMENT_PATTERN   	    = "Frequency\\:(.*?)";
	public final static String THEN_DO_FORTH_ELEMENT_STOP   	 	= ")";
	
	
	public final static String CLOCK_SEPARATOR   	 	= "-";

}
