/**
 *
**/

public class Note {
	int track;
	int time;
	int channel;
	boolean isOn;
	int note;
	int moddedValue;
	int velocity;
	
	double intervalUp;
	String chord;
		
	/*
	 * POST: construct a Node.
	 * TODO(?): Do I need other constructors? Maybe.
	 */
	public Note(int track, int time, int channel, boolean isOn, 
					int note, int velocity, double intervalUp, String chord) {
		
		this.track = track;
		this.time = time;
		this.channel = channel;
		this.isOn = isOn;
		this.note = note;
		this.moddedValue = (note + 5) % 12; //makes C == 1 (music convention) and each half step +1
		this.velocity = velocity;
		this.intervalUp = intervalUp;
		this.chord = chord;
	}
	
	/*
	 * POST: prints out the entire contents to a string.
	 */
	public String toString() {
		String noteOn = "On";
		if (!isOn) noteOn = "Off";
		
		return track + "," 
				+ time + "," 
				+ noteOn +"," 
				+ note + "," 
				+ velocity + "," 
				+ intervalUp + "," 
				+ chord;
	}
}
