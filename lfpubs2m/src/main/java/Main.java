import java.io.IOException;
import java.text.ParseException;
import edu.casetools.lfpubs2m.LFPUBS2MTranslator;

public class Main {

	public static void main(String[] args){
		//gui.run();
		System.out.println("Initializing...");
		LFPUBS2MTranslator translator = new LFPUBS2MTranslator(true);
		System.out.println("- - - - - - - - - - - - - - - - - - -");
		System.out.println(""+translator.getTranslation("./examples/LFPUBS_Output_0.txt")); //Select an LFPUBS output file
		System.out.println("- - - - - - - - - - - - - - - - - - -");
		System.out.println("Ending...");
	}


}
