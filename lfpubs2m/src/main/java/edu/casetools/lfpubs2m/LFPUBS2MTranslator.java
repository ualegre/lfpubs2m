package edu.casetools.lfpubs2m;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import edu.casetools.lfpubs2m.lfpubsdata.LFPUBSPattern;
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
		String result = "";
		for(int i=0;i<patterns.size();i++){
			result = result + "___________________________________\n";
			result = result + "Pattern ID: "+patterns.get(i).getId()+"\n";
			result = result + ""+patterns.get(i).printPattern()+"\n";
		}
		return result;
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
