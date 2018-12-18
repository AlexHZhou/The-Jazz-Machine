import java.util.HashSet;
import java.util.Set;

public class Chord {
	Set<Note> components = new HashSet<Note>();
	String chordName;
	int startTimestamp;
	int endTimestamp;
	
	public Chord(Set<Note> components, int startTimestamp, int endTimestamp) {
		this.components = components;
		this.chordName = "";
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}
	
	public void addToChord(Note n) {
		components.add(n);
		//don't think overwriting is a problem...don't need contains check.
	}
	
	public String toString() {
		return "";
		
		
	}
}
