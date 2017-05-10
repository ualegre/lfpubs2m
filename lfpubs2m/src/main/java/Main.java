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
