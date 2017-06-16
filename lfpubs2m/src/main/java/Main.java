import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import edu.casetools.lfpubs2m.LFPUBS2MTranslator;
import GUI.lfpubs2m;

public class Main {
	static lfpubs2m gui= new lfpubs2m();
	public static void main(String[] args){
		gui.run();
		
	/*	System.out.println("Initializing...");
		LFPUBS2MTranslator translator = new LFPUBS2MTranslator(true);
		System.out.println("- - - - - - - - - - - - - - - - - - -");
		System.out.println(""+translator.getTranslation("/Users/mdx/git/lfpubs/lfpubs/result/resultReasoner.txt"));
		System.out.println("- - - - - - - - - - - - - - - - - - -");
		System.out.println("Ending...");*/
		
	}

}
