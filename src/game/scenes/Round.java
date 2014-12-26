package game.scenes;

import game.Controls;
import game.Interlude;
import game.Orientation;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
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
import music.InstrumentType;
import music.Music;
import music.Simultaneous;
import music.SoundElement;
import music.Note;
import music.Voice;

public class Round extends Scene {
    private final Music music;
    private final boolean isMultiVoice;
    private final List<Button> buttons = new ArrayList<Button>();
    /** Each note value and voicetype corresponds to a notemarker */
    private final Map<Pair<Integer,Handedness>,NoteMarker> noteMarkers = new HashMap<Pair<Integer,Handedness>,NoteMarker>();
    private final Queue<MovingSound> notesOnScreen = new LinkedList<MovingSound>();
    private final List<Voice> voices;
    /** restingTimes: Key is the voice index. The value is the resting time */
    private final Map<Integer,Integer> restingTimes = new HashMap<Integer,Integer>();
    private final Label scoreLabel = Label.scoreLabel( 0.5f, 0.05f, GameFonts.ARIAL_PLAIN_32 );
    private int timeElapsed = 0;
    private boolean musicStarted = false;
    private final Label timeElapsedLabel = Label.timeElapsedLabel();
    private final int endTime;
    private final Label endTimeLabel;
    
    private final Instrument selectedInstrument;
    private final List<Handedness> handednesses = new ArrayList<Handedness>();
    private int totalScore = 0;
    
    public Round(Music music, Instrument selectedInstrument) {
        this.music = music;
        this.endTime = music.duration();
        this.endTimeLabel = Label.endTimeLabel( endTime );
        this.isMultiVoice = music.isMultiVoice();
        int initialDelay = 6000; // 6 seconds
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
        
        scoreLabel.render(g);
        timeElapsedLabel.render(g);
        endTimeLabel.render(g);
    }

    long oldTime = 0;
    long newTime = 0;
    
    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        // Update the orientation if the user changes it
        if ( input.isKeyPressed(Input.KEY_LEFT) ) {
            Orientation.rotateClockwise();
        } else if ( input.isKeyPressed(Input.KEY_RIGHT) ) {
            Orientation.rotateCounterClockwise();
        }
        
        // Update timeElapsedLabel if music started
        if ( musicStarted ) {
            timeElapsed += t;
            int timeElapsedInSeconds = timeElapsed / 1000;
            int minutes = timeElapsedInSeconds / 60;
            int remainingSeconds = timeElapsedInSeconds % 60;
            timeElapsedLabel.setText(minutes + ":" + String.format("%02d",remainingSeconds));
        }
        
        if ( timeElapsed > endTime + 4000 ) {
            SceneManager.setNewScene( Scene.results(music.title(), totalScore) );
        }
        
        // Update each button
        for ( Button button : buttons ) {
            button.update(t);
        }
        
        // Update note markers
        for ( Pair<Integer,Handedness> pair : noteMarkers.keySet() ) {
            NoteMarker noteMarker = noteMarkers.get(pair);
            noteMarker.update(t);
        }
        
        // Update resting times and put a sound element on screen if it is time.
        for ( int voiceIndex = 0; voiceIndex < voices.size(); voiceIndex++ ) {
            Voice voice = voices.get(voiceIndex);
            
            Instrument instrument = voice.instrument();
            instrument.update(t);
            
            int restingTime = restingTimes.get(voiceIndex);
            
            restingTime -= t;
            restingTimes.put(voiceIndex, restingTime);
            
            if (restingTime <= 0) { // pick out another note!
                musicStarted = true;
                if ( voice.ended() ) {
                    
                } else {
                    SoundElement soundElement = voice.next();
                    
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
                        soundElement.bePlayed(instrument);
                    }
                    if (!voice.ended()) {
                        restingTime += voice.timeUntilNextElement();
                        restingTimes.put(voiceIndex, restingTime);
                    }
                }
            }
            
            // Remove any notes that are no longer on screen.
            if ( !notesOnScreen.isEmpty() ) { 
                while ( notesOnScreen.peek().offScreen() ) {
                    notesOnScreen.remove();
                    if (notesOnScreen.isEmpty()) {
                        break;
                    }
                }
            }
            
            // Move each moving sound, also make the first third a particular color
            int counter = 0;
            for ( MovingSound movingSound : notesOnScreen ) {
                if ( counter == 0 ) {
                    movingSound.setFront();
                } else if ( counter == 1 ) {
                    movingSound.setSecond();
                } else if ( counter == 2 ) {
                    movingSound.setThird();
                }
                movingSound.update(t);
                counter++;
            }
            
            // If a user plays a note, perform all necessary actions due to his input.
            // This includes playing a note and updating score.
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
                                if ( letter != movingSound.soundElement().integer() ) {
                                    totalScore -= 30;
                                } else {
                                    totalScore += (int) (100 * ( 1 - ( ( Math.abs( 0.85f - movingSound.fraction() ) ) / 0.85f ) ) );
                                }
                                scoreLabel.setText( Integer.toString( totalScore ) );
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
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        for ( Voice voice : voices ) {
            voice.instrument().clear();
        }
    }
}
