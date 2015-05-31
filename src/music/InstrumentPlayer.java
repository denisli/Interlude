package music;

import game.Updateable;

public abstract class InstrumentPlayer implements Updateable {
	private Instrument instrument;
	
	public abstract void update(int t);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public void play(Note note) {
		instrument.play(note);
	}
}
