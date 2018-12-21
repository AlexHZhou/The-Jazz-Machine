import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Alex Zhou for Hack Lodge 2018
 * 
 * Uses Andrej Karparthy's RNN and lots of MIDI files
 * from miditune.com. Music is primarily jazz piano, and
 * most pieces are written by Art Tatum.
**/

public class TheJazzMachine {
	public static final int TIME_INCREMENT = 15;
	
	/**
	 * POST: read from file(s), processes file(s), and prints 
	 * 		 a modified version to new file.
	 */
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		Queue<Note> data = new LinkedList<Note>();
		
		System.out.print("What file format do you want to convert to (CUSTOM/CSV)? ");
		boolean toCustom = true;
		if (scan.next().equalsIgnoreCase("CSV")){
			toCustom = false;
		}
		
		introMessage(toCustom);
			
		String filePath = new File("").getAbsolutePath();
		//Default path is CSV -> Processed CSV Files
		//Reverse path is Generated Text Files -> Generated CSV Files
		String adjustedReadPath = "/CSV Files/"; 
		String adjustedWritePath = "/Processed CSV Files";
		if (!toCustom) {
			adjustedReadPath = "/Generated Text Files/";
			adjustedWritePath = "/Generated CSV Files/";
		}
		
		boolean moreFiles = true;
		while (moreFiles) {
			System.out.print("Enter name of file or 'n' to stop: ");
			
			String name = scan.next();
			if (name.equalsIgnoreCase("n")) moreFiles = false;
			else {
				BufferedReader reader = new BufferedReader(new FileReader(filePath + adjustedReadPath + name));
				if (toCustom) {
					Queue<Note> file = readMIDI(reader);
					normalizeFile(file);
					data.addAll(file);
				} else {
					Queue<Note> file = new LinkedList<Note>();
					file.addAll(readCSV(reader));
					data.addAll(file);
					System.out.println("file length is " + file.size() + " while data length is " + data.size());
				}
				
				reader.close();
			}
		}

		if (toCustom) {
			//temp = processData(temp); //add in further info (chords)
			data = transpose(data); //transpose data 11 times
		}
		
		//Writing operations
		System.out.print("Output file name? ");
		String outputFileName = scan.next();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + adjustedWritePath + outputFileName));
		if (toCustom) {
			writeToTxt(data, writer);
			writer.close();	
		} else {
			writeToCSV(data, writer);
			writer.close();
		}
	}

	private static List<Note> readCSV(BufferedReader reader) throws IOException{
		List<Note> q = new ArrayList<Note>();
		Map<Integer, Note> isOn = new HashMap<Integer, Note>();
		int timestamp = 0;
		
		String s = reader.readLine();
		while (s != null) {
			Map<Integer, Note> cur = new HashMap<Integer, Note>();
			
			String[] data = s.split(" ");
			for (String d : data) {
				if (!d.equals(" ") && !d.equals("")) {
					int temp = Integer.parseInt(d);
					cur.put(temp, new Note(2, timestamp, "Note_on_c", 2, temp, 0));
				}
			}
			
			Iterator<Integer> itr = isOn.keySet().iterator();
			while (itr.hasNext()) {
				int n = itr.next();
				if (!cur.containsKey(n)) {
					q.add(isOn.get(n));
					q.add(new Note(2, timestamp, "Note_off_c", 2, n, 0));
					itr.remove();
				} else {
					Note newN = isOn.get(n);
					newN.velocity += TIME_INCREMENT;
					isOn.put(n, newN);
					//n.velocity += TIME_INCREMENT;
				}
			}
			
			timestamp += TIME_INCREMENT;
			s = reader.readLine();
			for (int i : cur.keySet()) {
				if (!isOn.containsKey(i)) {
					isOn.put(i, cur.get(i));
				}
			}
		}
		
		Collections.sort(q, new TimeComparator());
		return q;
		
	}
	
	/**
	 * @param reader
	 * @return
	 * @throws IOException 
	 */
	/*
	private static List<Note> readCSV(BufferedReader reader) throws IOException {
		List<Note> q = new ArrayList<Note>();
		Map<Integer, Note> wasOn = new HashMap<Integer, Note>();
		int timeStamp = 0;
		String s = reader.readLine();
		
		while (s != null) {
			Map<Integer, Note> curLine = new HashMap<Integer, Note>();
		
			//adding to the tracked current list (isOn)
			String[] data = s.split(" ");
			for (String d : data) {
				if (!d.equals(" ") && !d.equals("")) {
					if (!curLine.containsKey(d)) {
						int temp = Integer.parseInt(d);
						curLine.put(temp, new Note(2, timeStamp, "Note_on_c", 2, temp, 0));
						//System.out.println("curLine added " + temp);
					
					}
				}
			}

			System.out.println("curLine: " + curLine.keySet().toString());
			System.out.println("wasOn: " + wasOn.keySet().toString());
			System.out.println();
			
			//check differece between isOn and wasOn
			//if isOn doesn't contain something in wasOn, 
			//it must have been removed. 
			Iterator<Note> itr = wasOn.values().iterator();
			while (itr.hasNext()) {
				Note n = itr.next();
				//System.out.println("Looking at " + n.note + " from wasOn.");
				if (!curLine.containsKey(n.note)) {
					System.out.println("curLine doesnt contain " + n.note);
					q.add(n);
					q.add(new Note(2, timeStamp, "Note_off_c", 2, n.note, 0));
					//System.out.println("added " + n.note + " to the overall q!");
					
					itr.remove();
				}
			}
			
			s = reader.readLine();
			timeStamp += TIME_INCREMENT;
			for (int n : curLine.keySet()) {
				if (!wasOn.containsKey(n)) {
					//System.out.println("wasOn added " + n);
					wasOn.put(n, curLine.get(n));
				} else {
					Note note = curLine.get(n);
					
					note.velocity += TIME_INCREMENT;
					wasOn.put(n, note);
				}
			}
		}
		Collections.sort(q, new TimeComparator());
				
		return q;
	}
	*/
	
	/**
	 * @param data
	 * @param writer
	 * @throws IOException 
	 */
	private static void writeToCSV(Queue<Note> data, BufferedWriter writer) throws IOException {
		System.out.print("writing to csv...");
		while (!data.isEmpty()) {
			Note n = data.remove();
			writer.write(n.defaultToString());
			writer.newLine();
		}
		
		System.out.println("done.");
	}

	public static void introMessage(boolean toCustom) {
		if (toCustom) { //CSV -> Custom
			System.out.println("----------------------------------------------------------");
			System.out.println("Submit files to be combined into one big file and transposed");
			System.out.println(" - Note that file must be in /CSV Files/[your file] - ");
			System.out.println(" - Something may be broken w/ concatenating files. - ");
			System.out.println();
		} else { //Custom -> CSV
			System.out.println("----------------------------------------------------------");
			System.out.println("Submit file to be converted from custom back to CSV.");
			System.out.println(" - Note that file must be in /Generated Text Files/[your file] - ");
			System.out.println();
		}
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
		
		return changed;
	}

	/*
	 * PRE: takes in a reader
	 * POST: returns a Queue that contains the notes read in the file.
	 * 		The resulting queue ONLY contains notes on
	 * 		off, control values, headers and misc info is ignored);
	 */
	public static Queue<Note> readMIDI(BufferedReader reader) throws IOException {
		Queue<Note> thisSong = new LinkedList<Note>();
		
		System.out.print("reading in data...");
		String s = reader.readLine();
		while (s != null) {
			String delimiters = "((, )|,)";
			String[] parts = s.split(delimiters);
			String note = parts[2];
			try {
				if (note.equals("Note_on_c")) { //NOT SURE IF I WANT TO GET RID OF NOTE OFF.
					Note n = new Note(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 
							parts[2], Integer.parseInt(parts[3]), 
							Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
					thisSong.add(n);
				}
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
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
	public static void writeToTxt(Queue<Note> data, BufferedWriter writer) throws IOException{
		if (data == null) return;
		
		//writing out.
        System.out.print("writing out data...");
        
        Set<Note> isOn = new TreeSet<Note>();
        int trueTime = 0;
        while (data.size() > 0) {
        	int peekTime = data.peek().time; 
        	//need to get around nullpointer error from removing, then checking peek;
        	
        	while (trueTime > peekTime) {
        		//System.out.println("peek: " + peekTime + " vs. true: " + trueTime);
        		
        		if (!data.isEmpty()) {
        			isOn.add(data.remove());
        			if (!data.isEmpty()) peekTime = data.peek().time;
        		}
        		else peekTime = Integer.MAX_VALUE;
        	}     	
            
        	if (!isOn.isEmpty()) {
        		//System.out.println("There are currently " + isOn.size() + " in isOn");
        		Iterator<Note> itr = isOn.iterator();
        		while (itr.hasNext()) {
        			Note n = itr.next();
        			if (n.time + n.velocity < trueTime) itr.remove();
        			else {
        				writer.write(n.note + " ");
        			}
        		}
        	}
        	
        	writer.newLine();
            trueTime += TIME_INCREMENT;
        }
        System.out.println("...done");
	}

	static class TimeComparator implements Comparator<Note>{
		public int compare(Note arg0, Note arg1) {
			return arg0.time - arg1.time;
		}
		
	}

}
