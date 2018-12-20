import java.util.Comparator;

/**
 *
**/

public class Note implements Comparable<Note>{
	int track;
	int time;
	String lineType;
	int channel;
	int note;
	int velocity;

	int relativePitch;
	double intervalUp;
	String chord;
		
	public Note(int track, int time, String lineType, int channel,
					int note, int velocity) {
		this(track, time, lineType, channel, note, velocity, 0, "");
	}
	
	/*
	 * POST: construct a Node.
	 * TODO(?): Do I need other constructors? Maybe.
	 */
	public Note(int track, int time, String lineType, int channel, 
					int note, int velocity, double intervalUp, String chord) {
		
		this.track = track;
		this.time = time;
		this.channel = channel;
		this.lineType = lineType;
		this.note = note;
		this.relativePitch = (note) % 12;
		//C == 60, so this makes C == 0 (music convention)
		//Note that full step = 2 and half step = 1
		this.velocity = velocity;
		this.intervalUp = intervalUp;
		this.chord = chord;
	}
	
	/*
	 * POST: prints out the entire contents to a string.
	 */
	public String defaultToString() {
		
		return track + "," 
				+ time + "," 
				+ lineType +"," 
				+ channel + ","
				+ note + "," 
				+ velocity + ",";
	}
	
	public int compareTo(Note other) {
		return this.note - other.note;
	}
	
}

