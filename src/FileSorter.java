import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 *
**/

/**
  Alex Zhou
 * -DATE-
 * CSE143X
 * TA: Ayaz
 * Assignment #
 *
 * This program ...
 */
public class FileSorter {
	static Queue<Note> data;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		data = new LinkedList<Note>();
		Scanner scan = new Scanner(System.in);
		String filePath = new File("").getAbsolutePath();
		
		introMessage();
		
		//Reading operations
		
		System.out.print("Enter name of txt file: ");
		String name = scan.next();
		BufferedReader reader = new BufferedReader(new FileReader(filePath + "/Generated Text Files/" + name));
		List<Note> temp = read(reader);
		reader.close();
		
		Collections.sort(temp, new TimeComparator());
		data.addAll(temp);
		System.out.println("done. data contains " + data.size() + " elements.");
        
		//Writing operations
		System.out.print("Output file name? ");
		String outputFileName = scan.next();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/Generated Text Files/" + outputFileName));
		write(writer);
		writer.close();
		
		
	}

	public static void introMessage() {
		System.out.println("Sorts a machine generated file by note timestamp.");
		System.out.println("Because the RNN sucks with time apparently");
		System.out.println("------------------------------------------------");
	}

	public static List<Note> read(BufferedReader reader) throws IOException {
		System.out.print("reading in data...");
		List<Note> temp = new ArrayList<Note>();
		String s = reader.readLine();
		while (s != null) {
			String[] parts = s.split(",");
			System.out.println("s is " + s);
			if (parts.length > 5) {
				try {
					Note n = new Note(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 
										parts[2], Integer.parseInt(parts[3]), 
										Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
		            temp.add(n);
		            System.out.println("added " + n.note + " to temp.");
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
			s = reader.readLine();
        }
		System.out.println("temp now holds " + temp.size() + " elements");
      
        
        return temp;
	}
		
	public static void write(BufferedWriter writer) throws IOException{
		if (data == null) return;
		
		//writing out.
        System.out.print("writing out data...");
        while (data.size() > 0) {
			writer.write(data.remove().defaultToString());
			writer.newLine();
        }
        System.out.println("...done");
	}

	static class TimeComparator implements Comparator<Note>{
		public int compare(Note arg0, Note arg1) {
			return arg0.time - arg1.time;
		}
		
	}
}
