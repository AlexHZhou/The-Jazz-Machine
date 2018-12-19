import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by Alex Zhou for Hack Lodge 2018
 * 
 * Uses Andrej Karparthy's RNN and lots of MIDI files
 * from miditune.com. Music is primarily jazz piano, and
 * most pieces are written by Art Tatum.
**/

public class TheJazzMachine {
	public static final int TIME_INCREMENT = 10;
	
	/**
	 * POST: read from file(s), processes file(s), and prints 
	 * 		 a modified version to new file.
	 */
	public static void main(String[] args) throws IOException {
		Queue<Note> data = new LinkedList<Note>();
		Scanner scan = new Scanner(System.in);
		String filePath = new File("").getAbsolutePath();
		
		introMessage();
		
		//Reading operations
		boolean moreFiles = true;
		while (moreFiles) {
			System.out.print("Enter name of csv file or 'n' to stop: ");
			
			String name = scan.next();
			if (name.equalsIgnoreCase("n")) moreFiles = false;
			else {
				BufferedReader reader = new BufferedReader(new FileReader(filePath + "/CSV Files/" + name));
				Queue<Note> oneFile = read(reader);
				normalizeFile(oneFile);
				data.addAll(oneFile);
				
				reader.close();
			}
			
		}

		//temp = processData(temp); //add in further info (chords)
		//data = transpose(data); //transpose data 11 times
		
		//Writing operations
		System.out.print("Output file name? ");
		String outputFileName = scan.next();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/Processed CSV Files/" + outputFileName));
		write(data, writer);
		writer.close();
	}

	/**
	 * PRE: takes in file to modify
	 * POST: normalizes the time stamp and duration to
	 * 		 the constant value TIME_INCREMENT
	 */
	private static void normalizeFile(Queue<Note> oneFile) {
		System.out.print("Normalizing file...");
		int deltaT = TIME_INCREMENT;
		int iter = oneFile.size();
		for (int i = 0; i < iter; i++) {
			Note cur = oneFile.remove();
			cur.time = roundToNearest(cur.time, TIME_INCREMENT);
			cur.velocity = roundToNearest(cur.velocity, TIME_INCREMENT);
			oneFile.add(cur);
		}		
		System.out.println("done.");
	}
	
	/*
	 * POST: rounds number x to the nearest 'round'
	 */
	private static int roundToNearest(int x, int round) {
		System.out.print("    rounding " + x + " to ");
		int changed = x;
		int lowBound = (x / TIME_INCREMENT) * TIME_INCREMENT;
		int highBound = lowBound + TIME_INCREMENT;
		int low = Math.abs(x - lowBound);
		int high = Math.abs(x - highBound);
		
		
		if (low < high) {
			changed = lowBound;
		} else {
			changed = highBound;
		}
		
		System.out.println(changed);
		return changed;
	}

	/*
	 * PRE: takes in a reader
	 * POST: returns a Queue that contains the notes read in the file.
	 * 		The resulting queue ONLY contains notes on
	 * 		off, control values, headers and misc info is ignored);
	 */
	public static Queue<Note> read(BufferedReader reader) throws IOException {
		Queue<Note> thisSong = new LinkedList<Note>();
		
		System.out.print("reading in data...");
		String s = reader.readLine();
		while (s != null) {
			String[] parts = s.split(",");
			String note = parts[2];
			if (note.equals(" Note_on_c")) { 
				Note n = new Note(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 
						parts[2], Integer.parseInt(parts[3]), 
						Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
				thisSong.add(n);
			}
			
            s = reader.readLine();
        }
		System.out.println("thisSong now holds " + thisSong.size() + " elements");
       
		return thisSong;
	}
	
	public static Queue<Note> processData(Queue<Note> temp){
		return null;
	}
	
	/*
	 * PRE: takes in Queue of notes (temp) to transpose
	 * POST: returns the all possible transposed notes.
	 */
	public static Queue<Note> transpose(Queue<Note> temp) {
		System.out.print("Starting transpose...");
		Queue<Note> data = new LinkedList<Note>();
		int offSet = -5; //-5 -> +6
		for (int i = 0; i < 12; i++) { //each of the 11 transpositions
			
			int tempLength = temp.size();
			for (int t = 0; t < tempLength; t++) { //for each iteration of a transposition
				Note n = temp.remove();
				temp.add(n);
				
	        	if (!n.lineType.equals(" Control_c")) {
		        	n.note = n.note + offSet;
	        	}
	        	
	        	data.add(n);
			}
			offSet++;	        
		}
		
		System.out.println("after transpose, data has " + data.size() + " elements.");
		return data;
	}
	
	/*
	 * PRE: Takes in the queue holding data and mechanism to write to file
	 * POST: Prints out a modified version of the data to file
	 */
	public static void write(Queue<Note> data, BufferedWriter writer) throws IOException{
		if (data == null) return;
		
		//writing out.
        System.out.print("writing out data...");
        
        
        while (data.size() > 0) {
			writer.write(data.remove().defaultToString());
			writer.newLine();
        }
        System.out.println("...done");
	}

	//POST: prints the intro to the program
	public static void introMessage() {
		System.out.println("Submit files to be combined into one big file and transposed");
		System.out.println(" - Note that file must be under /CSV Files/[your file] - ");
		System.out.println();
	}
}
