package game.scenes;

import game.Controls;
import game.Interlude;
import game.Orientation;
import game.InstrumentType;
import game.buttons.Button;
import game.moving_sound.MovingSound;
import game.note_marker.NoteMarker;
import game.pop_ups.PopUp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import util.Pair;
import music.Handedness;
import music.Instrument;
import music.Music;
import music.MusicElement;
import music.Simultaneous;
import music.SoundElement;
import music.Note;
import music.Voice;

public class Round implements Scene {
    private final Music music;
    private final boolean isMultiVoice;
    private final List<Button> buttons = new ArrayList<Button>();
    /** Each note value and voicetype corresponds to a notemarker */
    private final Map<Pair<Integer,Handedness>,NoteMarker> noteMarkers = new HashMap<Pair<Integer,Handedness>,NoteMarker>();
    private final Queue<MovingSound> notesOnScreen = new LinkedList<MovingSound>();
    private final Queue<SoundElement> automaticNotes = new LinkedList<SoundElement>();
    private final List<Voice> voices;
    /** restingTimes: Key is the voice index. The value is the resting time */
    private final Map<Integer,Integer> restingTimes = new HashMap<Integer,Integer>();
    
    private final Instrument selectedInstrument;
    private final List<Handedness> handednesses = new ArrayList<Handedness>();
    
    public Round(Music music, Instrument selectedInstrument) {
        this.music = music;
        this.isMultiVoice = music.isMultiVoice();
        int initialDelay = 4000; // 4 seconds
        List<Integer> timesUntilVoiceStart = music.timesUntilVoiceStarts();
        this.voices = music.voices();

        for ( int voiceIndex = 0; voiceIndex < voices.size(); voiceIndex++ ) {
            restingTimes.put(voiceIndex, initialDelay + timesUntilVoiceStart.get(voiceIndex));
        }
        this.selectedInstrument = selectedInstrument;
        if ( selectedInstrument.type() == InstrumentType.SINGLE ) {
            handednesses.add(Handedness.SINGLE);
        } else {
            handednesses.add(Handedness.LEFT);
            handednesses.add(Handedness.RIGHT);
        }
    }
    
    @Override
    public void render(Graphics g) {
        for ( Pair<Integer,Handedness> pair : noteMarkers.keySet() ) {
            NoteMarker noteMarker = noteMarkers.get(pair);
            noteMarker.render(g);
        }
        
        for ( MovingSound movingSound : notesOnScreen ) {
            movingSound.render(g);
        }
        for (Button button : buttons) {
            button.render(g);
        }
    }

    long oldTime = 0;
    long newTime = 0;
    
    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        if ( input.isKeyPressed(Input.KEY_LEFT) ) {
            Orientation.rotateClockwise();
        } else if ( input.isKeyPressed(Input.KEY_RIGHT) ) {
            Orientation.rotateCounterClockwise();
        }
        
        for ( Button button : buttons ) {
            button.update(t);
        }
        
        for ( Pair<Integer,Handedness> pair : noteMarkers.keySet() ) {
            NoteMarker noteMarker = noteMarkers.get(pair);
            noteMarker.update(t);
        }
        
        for ( int voiceIndex = 0; voiceIndex < voices.size(); voiceIndex++ ) {
            Voice voice = voices.get(voiceIndex);
            
            Instrument instrument = voice.instrument();
            instrument.update(t);
            
            int restingTime = restingTimes.get(voiceIndex);
            //System.out.println(restingTime);
            
            restingTime -= t;
            restingTimes.put(voiceIndex, restingTime);
            
            if (restingTime <= 0) { // pick out another note!
                if (voice.ended()) {
                    // go to a different scene?
                } else {
                    /////////// IGNORE THIS PART FOR NOW. /////////////////
                    MusicElement element = voice.next();
                    if ( element.isRest() ) {
//                        restingTime += voice.timeUntilNextElement();
//                        restingTimes.put(voiceIndex, restingTime);
                        if (!voice.ended()) {
                            restingTime += voice.timeUntilNextElement();
                            restingTimes.put(voiceIndex, restingTime);
                        }
                    /////////// IGNORE THE PART ABOVE FOR NOW ///////////
                    } else {
                        SoundElement soundElement = (SoundElement) element;
                        int soundElementLetter = soundElement.integer();
                        float offScreen = 0.0f;
                        //TODO: Change handedness!
                        if ( instrument.equals(selectedInstrument) ) {
                            System.out.println(instrument.getInstrumentName());
                            Pair<Integer,Handedness> pair = new Pair<Integer,Handedness>(soundElementLetter, Handedness.SINGLE);
                            MovingSound movingSound = new MovingSound( offScreen, noteMarkers.get(pair).fractionY(), soundElement, Handedness.SINGLE );
                            movingSound.init();
                            notesOnScreen.add( movingSound );
                        } else {
                            System.out.println(instrument.getInstrumentName());
                            automaticNotes.add( soundElement );
                        }
                        if (!voice.ended()) {
                            restingTime += voice.timeUntilNextElement();
                            restingTimes.put(voiceIndex, restingTime);
                        }
                    }
                }
            }
            
            if ( !automaticNotes.isEmpty() ) {
                SoundElement soundElement = automaticNotes.remove();
                soundElement.bePlayed(instrument);
            }
            
            if ( !notesOnScreen.isEmpty() ) { 
                while ( notesOnScreen.peek().offScreen() ) {
                    notesOnScreen.remove();
                    if (notesOnScreen.isEmpty()) {
                        break;
                    }
                }
            }
            
            for ( MovingSound movingSound : notesOnScreen ) {
                movingSound.update(t);
            }
            
            if ( instrument.equals(selectedInstrument) ) {
                for ( Handedness handedness : handednesses ) {
                    for ( int key : Controls.noteKeys( handedness ) ) {
                        //if ( input.isKeyPressed(key) ) {
                            if ( !notesOnScreen.isEmpty() ) {
                                int letter = Controls.correspondingNote(key, handedness );
                                MovingSound movingSound = notesOnScreen.remove();
                                SoundElement soundElement = movingSound.soundElement();
                                SoundElement correspondingSoundElement = soundElement.correspondingSoundElement(letter);
                                soundElement.bePlayed(instrument);
                            }
                        //}
                    }
                }
            }
        }
    }
    
    @Override
    public void init() {
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
        for (int note : notes) {
            for ( Handedness handedness : handednesses ) {
                NoteMarker noteMarker = new NoteMarker(note, handedness);
                noteMarker.init();
                noteMarkers.put( new Pair<Integer,Handedness>(note, handedness), noteMarker );
            }
        }
        
        buttons.add( Button.backButton(0.95f, 0.05f));
    }
    
    public Music music() {
        return music;
    }
    
    public boolean isMultiVoice() {
        return isMultiVoice;
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.songSelection();
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }
}
