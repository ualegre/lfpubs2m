import java.io.IOException;
import java.text.ParseException;

import GUI.lfpubs2m;
import edu.casetools.lfpubs2m.LFPUBS2MTranslator;

public class Main {
	
	static lfpubs2m gui= new lfpubs2m();
	

	public static void main(String[] args){
		gui.run();
		//System.out.println("Initializing...");
		LFPUBS2MTranslator translator = new LFPUBS2MTranslator(true);
		System.out.println("- - - - - - - - - - - - - - - - - - -");
		//System.out.println(""+translator.getTranslation("/Users/mdx/git/lfpubs2m/lfpubs2m/examples/LFPUBS_Output_0.txt"));
		System.out.println(""+translator.getTranslation("./examples/LFPUBS_Output_2.txt")); //Select an LFPUBS output file
		System.out.println("- - - - - - - - - - - - - - - - - - -");
		System.out.println("Ending...");
	}

}
