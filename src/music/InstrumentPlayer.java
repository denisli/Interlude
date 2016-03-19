package music;

public abstract class InstrumentPlayer {
	private Instrument instrument;
	
	public abstract void update(int t);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public void play(Note note) {
		instrument.play(note);
	}
}
