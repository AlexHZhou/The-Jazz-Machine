import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
**/

/**
 * Handles most of the added data processing.
 */
public class Chordinator {
	
	public Chordinator() {}

	/**
	 * @return
	 */
	public Queue<String> processData(Queue<String> data) {
		if (data == null) return null;
		
		Queue<String> processedData = new LinkedList<String>();
		Set<String[]> notesOn = new HashSet<String[]>();
		
		//skips the header
		String[] args = data.remove().split(",");
		while (data.size() > 0 && (!args[0].equals("NoteOn") || !args[0].equals("NoteOff") || !args[0].equals("Controller"))) {
			args = data.remove().split(",");
		}
		
		//starts looking through data
		while (data.size() > 0) {
			args = data.remove().split(",");
			notesOn.add(args);
			//TODO: Resume fancy stuff here.
		}
		
		return processedData;
	}

}
