import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
**/

/**
 * Handles most of the added data processing.
 */
public class Chordinator {
	Map<Integer, String> intervals;
	
	public Chordinator() {
		intervals = new HashMap<Integer, String>();
		populateIntervals(intervals);
	}

	/**
	 * populates given map with hard coded values for interval data.
	 */
	private void populateIntervals(Map<Integer, String> map) {
		map.put(1, "m2");
		map.put(2, "M2");
		map.put(3, "m3");
		map.put(4, "M3");
		map.put(5, "P4");
		map.put(6, "Tri");
		map.put(7, "P5");
		map.put(8, "m6");
		map.put(9, "M6");
		map.put(10, "m7");
		map.put(11, "M7");
		map.put(12, "Octave");
		map.put(13, "m9");
		map.put(14, "M9");
		map.put(15, "m10");
		map.put(16, "M10");
		map.put(17, "P11");
		map.put(18, "dim12");
		map.put(19, "P12");
	}

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
