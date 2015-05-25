package music;

public class Test {
	public static void main(String[] args) throws Exception {
		//LoadSynthesizer.loadSynthesizer();
		Instrument instrument = new InstrumentTest(1);
		final int duration = 2000; // 5 second
		Note note = new Note(35,duration,127);
		note.bePlayed(instrument);
		note = new Note(70,duration,127);
		note.bePlayed(instrument);
		note = new Note(42,duration,127);
		note.bePlayed(instrument);
	}
}
