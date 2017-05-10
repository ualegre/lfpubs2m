import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import GUI.lfpubs2m;
import edu.casetools.lfpubs2m.LFPUBS2MTranslator;

public class Main {
	static lfpubs2m gui= new lfpubs2m();
	public static void main(String[] args){
		gui.run();
		LFPUBS2MTranslator translator = new LFPUBS2MTranslator(true);
		try{
			File file=new File("./results/lfpubs2mes.mtpl");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			PrintWriter writer = new PrintWriter(bw);
			writer.println("");
			writer.print(""+translator.getTranslation("./examples/LFPUBS_Output_9.txt"));
			writer.print("");
			writer.close();
		}
		catch(Exception error){	
			System.out.println("Error Message: " + error.getMessage());
		}
		
	}

}
