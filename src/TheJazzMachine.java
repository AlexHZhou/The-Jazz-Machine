import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Alex Zhou for Hack Lodge 2018
 * 
 * Uses Andrej Karparthy's RNN and lots of MIDI files
 * from miditune.com. Music is primarily jazz piano, and
 * most pieces are written by Art Tatum.
**/

public class TheJazzMachine {
	static Queue<String> data;
	//TODO: add function to concat csv files
	
	public static void main(String[] args) {
    	data = new LinkedList<String>();
    	
    	try {
    		String filePath = new File("").getAbsolutePath();
    		System.out.println("filePath is " + filePath);
    		BufferedReader reader = new BufferedReader(new FileReader(filePath + "/CSV Files/ArtTatum-AsTimeGoesBy.csv"));
    		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/Processed CSV Files/OUTPUT-ArtTatum-AsTimeGoesBy.csv"));
    		
    		read(reader);
            
    		Chordinator processor = new Chordinator();
            data = processor.processData(data);
            
            write(writer);
            
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            
         } catch(Exception e) {
            e.printStackTrace();
         }
    	
	}
	
	public static void read(BufferedReader reader) throws IOException {
		//reading in
		System.out.print("reading in data...");
        String s = reader.readLine();
        while (s != null) {
            data.add(s);
            s = reader.readLine();
        }
        System.out.println("done.");
	}
	
	public static void write(BufferedWriter writer) throws IOException{
		if (data == null) return;
		
		//writing out.
        System.out.println("printing out data...");
        while (data.size() > 0) {
			String[] arguments = data.remove().split(",");
				            
            for (int a = 0; a < arguments.length; a++) {
               System.out.print(arguments[a] + ", ");
               writer.write(arguments[a]);
               if (a != arguments.length - 1) writer.write(",");
            }
            writer.newLine();
            System.out.println();
        }
        System.out.println("...done");
	}

}
