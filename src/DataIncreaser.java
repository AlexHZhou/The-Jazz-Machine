import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 *
**/

/**
 * This program combines a bunch of csv files into one larger file.
 * Additionally, for each file read in it transposes it into the 11 other
 * possible notes.
 */
public class DataIncreaser {
	static Queue<String> data;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		data = new LinkedList<String>();
		Scanner scan = new Scanner(System.in);
		String filePath = new File("").getAbsolutePath();
		//System.out.println("filePath is " + filePath);
		
		System.out.println("Submit files to be combined into one big file and transposed");
		System.out.println(" - Note that file must be under /CSV Files/[your file] - ");
		System.out.println();
		
		boolean moreFiles = true;
		while (moreFiles) {
			System.out.print("Enter name of csv file or 'n' to stop: ");
			String name = scan.next();
			if (name.equalsIgnoreCase("n")) moreFiles = false;
			else {
				BufferedReader reader = new BufferedReader(new FileReader(filePath + "/CSV Files/" + name));
				read(reader);
				
				reader.close();
			}
		}
		
		System.out.print("Output file name? ");
		String outputFileName = scan.next();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/Processed CSV Files/" + outputFileName));
		write(writer);
		writer.close();
		
		
	}
	public static void read(BufferedReader reader) throws IOException {
		System.out.print("reading in data...");
		Queue<String> temp = new LinkedList<String>();
		String s = reader.readLine();
		while (s != null) {
			String[] parts = s.split(",");
			String note = parts[2];
			if (note.equals(" Note_off_c")
		        	|| note.equals(" Note_on_c") 
		        	|| note.equals(" Control_c")) { 
				temp.add(s);
			}
            s = reader.readLine();
        }
		System.out.println("temp now holds " + temp.size() + " elements");
        
		int offSet = -5; //-5 -> +6
		for (int i = 0; i < 12; i++) { //each of the 11 transpositions
			
			data.add("iteration " + i);
			int tempLength = temp.size();
			for (int t = 0; t < tempLength; t++) { //for each iteration of a transposition
				s = temp.remove();
				temp.add(s);
				
				String[] parts = s.split("[, ]+");
	        	
	        	if (!parts[2].equals(" Control_c")) {
		        	int noteValue = Integer.parseInt(parts[4]);
		        	parts[4] = (noteValue + offSet) + "";
	        	}
	        	s = "";
		        s += parts[0] + "," + parts[1] + ", ";
		        for (int p = 2; p < parts.length; p++) {
		        	s += parts[p] + ",";
		        }
		        
		        data.add(s);
		              
			}
			data.add(""); //adds an empty space between each iteration.
			offSet++;	        
		}
        System.out.println("done. data contains " + data.size() + " elements.");
	}
	
	public static void write(BufferedWriter writer) throws IOException{
		if (data == null) return;
		
		//writing out.
        System.out.print("writing out data...");
        while (data.size() > 0) {
			writer.write(data.remove());
			writer.newLine();
        }
        System.out.println("...done");
	}

}
