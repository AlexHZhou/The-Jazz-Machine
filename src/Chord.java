import java.util.HashSet;
import java.util.Set;

public class Chord {
	Set<Note> components = new HashSet<Note>();
	String chordName;
	int startTimestamp;
	int endTimestamp;
	
	public Chord(int startTimestamp, int endTimestamp) {
		this(new HashSet<Note>(), startTimestamp, endTimestamp);
	}
	
	/*
	 * POST: constructs a chord given components, a start time, and end time
	 */
	public Chord(Set<Note> components, int startTimestamp, int endTimestamp) {
		this.components = components;
		this.chordName = "";
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}
	
	//adds a given note to this chord (assumes that note follows
	//the start/end time conventions.
	public void addToChord(Note n) {
		components.add(n);
		//don't think overwriting is a problem...don't need contains check.
	}
	
	public void determineChord() {
		
	}
	
	public String toString() {
		return "";
	}
}
