package game.scenes.round;

import game.Interlude;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.Scene;
import game.scenes.SceneManager;
import game.settings.Controls;
import game.settings.GameplayType;
import game.settings.GameplayTypeSetting;
import game.settings.Orientation;
import game.settings.Volume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import util.Pair;
import music.Handedness;
import music.Instrument;
import music.InstrumentType;
import music.Music;
import music.Note;
import music.Voice;

public class Round extends Scene {
    private final Music music;
    private final Instrument selectedInstrument;
    
    /** COLLECTIONS */
    private final List<Button> buttons = new ArrayList<Button>();
    /* Each note value and handedness corresponds to a notemarker */
    private final Map<Pair<Integer,Handedness>,NoteMarker> noteMarkers = new HashMap<Pair<Integer,Handedness>,NoteMarker>();
    private final Map<Pair<Integer,Handedness>,Queue<StackedMovingSound>> notesOnScreen = new HashMap<Pair<Integer,Handedness>,Queue<StackedMovingSound>>();
    private final List<Handedness> handednesses = new ArrayList<Handedness>();
    private final List<Voice> voices;
    /* restingTimes: Key is the voice index. The value is the resting time */
    private final Map<Integer,Integer> restingTimes = new HashMap<Integer,Integer>();

    /** IMMUTABLES */
    private final int endTime;
    
    /** LABELS */
    private Label<Integer> scoreLabel;
    private Label<Integer> timeElapsedLabel;
    private Label<Integer> endTimeLabel;
    
    /** GAME STATE PROPERTIES */
    private boolean musicStarted = false;
    private boolean paused = false;
    private int timeElapsed = 0;
    private int totalScore = 0;
    private boolean isAutoplay = false;
    
    public Round(Music music, Instrument selectedInstrument) {
        this.music = music;
        this.selectedInstrument = selectedInstrument;
        
        this.endTime = music.duration();
        
        if ( GameplayTypeSetting.gameplayType() == GameplayType.ONE_HANDED ) {
            handednesses.add(Handedness.SINGLE);
        } else {
            if ( selectedInstrument.type() == InstrumentType.SINGLE ) {
                handednesses.add(Handedness.SINGLE);
            } else {
                handednesses.add(Handedness.LEFT);
                handednesses.add(Handedness.RIGHT);
            }
        }
        
        for ( Handedness handedness : handednesses ) {
        	for ( Integer integer : Controls.notesInOrder() ) {
        		notesOnScreen.put(new Pair<Integer,Handedness>(integer,handedness),
        						  new LinkedList<StackedMovingSound>());
        	}
        }
        
        List<Integer> timesUntilVoicesStart = music.timesUntilVoicesStart();
        this.voices = music.voices();
        
        int initialDelay = 3000; // 3 seconds
        int timeToReachNoteMarker = (int) ( NoteMarker.FRACTION_X / (MovingSound.SPEED) );
        for ( int voiceIndex = 0; voiceIndex < voices.size(); voiceIndex++ ) {
            Voice voice = voices.get(voiceIndex);
            if ( !voice.instrument().equals(selectedInstrument) ) {
                restingTimes.put(voiceIndex, initialDelay + timeToReachNoteMarker + timesUntilVoicesStart.get(voiceIndex));
            } else {
                restingTimes.put(voiceIndex, initialDelay + timesUntilVoicesStart.get(voiceIndex));
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        noteMarkers.values().stream().forEach( noteMarker -> noteMarker.render(g) );
        
        notesOnScreen.values().stream().forEach( 
                notesOnScreen -> notesOnScreen.stream().forEach( 
                        movingSound -> movingSound.render(g) ) );
            
        buttons.stream().forEach( button -> button.render(g));
        
        scoreLabel.render(g);
        timeElapsedLabel.render(g);
        endTimeLabel.render(g);

        super.render(g);
    }

    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        updateSettings(input);
        
        // Update each button
        buttons.stream().forEach( button -> button.update(t) );
        
        // Update note markers
        noteMarkers.values().stream().forEach( noteMarker -> noteMarker.update(t) );
        
        if ( !paused ) {
            // Update timeElapsedLabel if music started
            if ( musicStarted ) {
                timeElapsed += t;
                timeElapsedLabel.updateValue(timeElapsed);
            }
            
            if ( timeElapsed > endTime + 4000 ) {
                SceneManager.setNewScene( Scene.results(music.title(), totalScore) );
            }
            
            // autoplay if game set to autoplay
            if ( isAutoplay ) {
                autoplay();
            }
            
            // Update resting times and put a sound element on screen if it is time.
            for ( int voiceIndex = voices.size(); --voiceIndex >= 0; ) {
                Voice voice = voices.get(voiceIndex);
                
                Instrument instrument = voice.instrument();
                instrument.update(t);
                
                int restingTime = restingTimes.get(voiceIndex);
                
                restingTime -= t;
                restingTimes.put(voiceIndex, restingTime);
                
                if (restingTime <= 0) { // pick out another note!
                    musicStarted = true;
                    if ( !voice.ended() ) {
                    	Map<Pair<Integer,Handedness>,StackedMovingSound> positionToStackedMovingSound = new HashMap<Pair<Integer,Handedness>,StackedMovingSound>();
                    	
                    	do {
	                        Note noteToInsert = voice.next();
	                        
	                        int integer = noteToInsert.integer();
	                        float offScreen = 0.0f;
	                        
	                        if ( instrument.equals(selectedInstrument) ) {
	                        	System.out.println(instrument.getInstrumentName());
	                        	Handedness handedness = voice.handedness();
	                        	Pair<Integer,Handedness> position = new Pair<Integer,Handedness>(integer,handedness);
	                        	MovingSound movingSound = new MovingSound( offScreen, noteMarkers.get(position).fractionY(), noteToInsert, handedness );
	                        	movingSound.init();
	                        	StackedMovingSound stackedMovingSound = positionToStackedMovingSound.get(position);
	                        	if ( stackedMovingSound == null ) {
	                        		stackedMovingSound = new StackedMovingSound();
	                        		stackedMovingSound.addMovingSound(movingSound);
	                        		positionToStackedMovingSound.put(position, stackedMovingSound);
	                        	} else {
	                        		stackedMovingSound.addMovingSound(movingSound);
	                        	}
	                        } else {
	                            System.out.println(instrument.getInstrumentName());
	                            noteToInsert.bePlayed(instrument);
	                        }
	                        if (!voice.ended()) {
	                            restingTime += voice.timeUntilNextElement();
	                            restingTimes.put(voiceIndex, restingTime);
	                        } else {
	                        	break;
	                        }
                    	} while ( voice.timeUntilNextElement() == 0 );
                    	
                    	for ( Pair<Integer,Handedness> position : positionToStackedMovingSound.keySet() ) {
                    		Queue<StackedMovingSound> correspondingNotesOnScreen = notesOnScreen.get(position);
                    		StackedMovingSound stackedMovingSound = positionToStackedMovingSound.get(position);
                    		correspondingNotesOnScreen.add(stackedMovingSound);
                    	}
                    }
                }
                
                // If a user plays a note, perform all necessary actions due to his input.
                // This includes playing a note and updating score.
                if ( instrument.equals(selectedInstrument) ) {
                    Handedness handedness = voice.handedness();
                    for ( int key : Controls.noteKeys( handedness ) ) {
                        if ( input.isKeyPressed(key) ) {
                        	int letter = Controls.correspondingNote(key, handedness );
                        	Pair<Integer,Handedness> pair = new Pair<Integer,Handedness>(letter,handedness);
                        	Queue<StackedMovingSound> correspondingNotesOnScreen = notesOnScreen.get(pair);
                            if ( !correspondingNotesOnScreen.isEmpty() ) {
                                StackedMovingSound stackedMovingSound = correspondingNotesOnScreen.remove();
                                stackedMovingSound.bePlayed(instrument);
//                                if ( letter != movingSound.soundElement().integer() ) {
//                                    totalScore -= 30;
//                                } else {
//                                    totalScore += (int) (100 * ( 1 - ( ( Math.abs( 0.85f - movingSound.fraction() ) ) / 0.85f ) ) );
//                                }
                            }
                        }
                    }
                }
            }
            
         // Remove any notes that are no longer on screen.
            for ( Pair<Integer,Handedness> pair : notesOnScreen.keySet() ) {
                Queue<StackedMovingSound> correspondingNotesOnScreen = notesOnScreen.get(pair);
                if ( !correspondingNotesOnScreen.isEmpty() ) { 
                    while ( correspondingNotesOnScreen.peek().offScreen() ) {
                        correspondingNotesOnScreen.remove();
                        if (correspondingNotesOnScreen.isEmpty()) {
                            break;
                        }
                    }
                }
            
            
                // Move each moving sound, also make the first three a particular color
                int counter = 0;
                for ( StackedMovingSound stackedMovingSound : correspondingNotesOnScreen ) {
                    switch (counter) {
                    case 0: stackedMovingSound.setFront(); break;
                    case 1: stackedMovingSound.setSecond(); break;
                    case 2: stackedMovingSound.setThird(); break;
                    default: break;
                    }
                    stackedMovingSound.update(t);
                    counter++;
                }
            }
        } else {
            input.clearKeyPressedRecord();
        }
        scoreLabel.updateValue( totalScore ); // have to update the score label here because of restarting
        
        super.update(t);
    }
    
    private void updateSettings( Input input ) {
     // toggle autoplay with left shift keys
        if ( input.isKeyPressed(Input.KEY_LSHIFT) ) {
            isAutoplay = !isAutoplay;
        }
        
        // Update the orientation if the user changes it
        if ( input.isKeyPressed(Input.KEY_LEFT) ) {
            Orientation.rotateClockwise();
        } else if ( input.isKeyPressed(Input.KEY_RIGHT) ) {
            Orientation.rotateCounterClockwise();
        }
        
        if ( input.isKeyPressed(Input.KEY_UP) ) {
            noteMarkers.keySet().stream().forEach( integerAndHand -> noteMarkers.get(integerAndHand).setDisplayType( DisplayType.KEY ) );
        } else if ( input.isKeyPressed(Input.KEY_DOWN) ) {
            noteMarkers.keySet().stream().forEach( integerAndHand -> noteMarkers.get(integerAndHand).setDisplayType( DisplayType.LETTER ) );
        }
    }
    
    private void autoplay() {
        for ( Pair<Integer,Handedness> pair : notesOnScreen.keySet() ) {
            Queue<StackedMovingSound> correspondingNotesOnScreen = notesOnScreen.get(pair);
            if ( !correspondingNotesOnScreen.isEmpty() ) {
                StackedMovingSound stackedMovingSound = correspondingNotesOnScreen.peek();
                float movingSoundFractionX = stackedMovingSound.fraction();
                float xDiff = movingSoundFractionX - NoteMarker.FRACTION_X;
                if ( Math.abs(xDiff) < 0.01f ) {
                    correspondingNotesOnScreen.remove();
                    stackedMovingSound.bePlayed(selectedInstrument);
                } else if ( xDiff > 0.01f ) {
                    while ( xDiff > 0.01f ) {
                        correspondingNotesOnScreen.remove();
                        if ( correspondingNotesOnScreen.isEmpty() ) { break; }
                        stackedMovingSound = correspondingNotesOnScreen.peek();
                        movingSoundFractionX = stackedMovingSound.fraction();
                        xDiff = movingSoundFractionX - NoteMarker.FRACTION_X;
                    }
                }
            }
        }
    }
    
    @Override
    public Scene parentScene() {
        return Scene.songSelection();
    }

    @Override
    public void cleanUp() {
        for ( Voice voice : voices ) {
            voice.instrument().clear();
        }
        Volume.reset();
        for ( Pair<Integer,Handedness> pair : notesOnScreen.keySet() ) {
            Queue<StackedMovingSound> correspondingNotesOnScreen = notesOnScreen.get(pair);
            correspondingNotesOnScreen.clear();
        }
    }

    @Override
    protected void layout() {
        // put in note markers onto the scene
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G };
        for (int note : notes) {
            for ( Handedness handedness : handednesses ) {
                NoteMarker noteMarker = new NoteMarker(note, handedness);
                noteMarker.init();
                noteMarkers.put( new Pair<Integer,Handedness>(note, handedness), noteMarker );
            }
        }
        
        // put in buttons
        buttons.add( Button.backButton(0.95f, 0.05f));
        buttons.add( Button.twoFaceButton( "Pause", "Resume", 0.3f, 0.05f, 
                (Runnable) () -> { paused = true; voices.stream().forEach( voice -> voice.instrument().pause() ); },
                (Runnable) () -> { paused = false; voices.stream().forEach( voice -> voice.instrument().resume() ); } ) );
        buttons.add( Button.textButton( "Restart", 0.85f, 0.05f,
                (Runnable) () -> {
                    music.restart();
                    for ( Pair<Integer,Handedness> pair : notesOnScreen.keySet() ) {
                        Queue<StackedMovingSound> correspondingNotesOnScreen = notesOnScreen.get(pair);
                        correspondingNotesOnScreen.clear();
                    }
                    int initialDelay = 100;
                    List<Integer> timesUntilVoicesStart = music.timesUntilVoicesStart();
                    int timeToReachNoteMarker = (int) ( NoteMarker.FRACTION_X / (MovingSound.SPEED) );
                    for ( int voiceIdx : restingTimes.keySet() ) {
                        Voice voice = voices.get(voiceIdx);
                        if ( !voice.instrument().equals(selectedInstrument) ) {
                            restingTimes.put(voiceIdx, initialDelay + timeToReachNoteMarker + timesUntilVoicesStart.get(voiceIdx));
                        } else {
                            restingTimes.put(voiceIdx, initialDelay + timesUntilVoicesStart.get(voiceIdx));
                        }
                    }
                    timeElapsed = 0;
                    totalScore = 0;
                }) );
        // put in options setter
        Set<Instrument> instruments = new HashSet<Instrument>();
        voices.stream().forEach( voice -> instruments.add(voice.instrument() ) );
        buttons.add( Button.textButton( "Options", 0.1f, 0.05f,
                (Runnable) () -> { this.addPopUp( OptionsSetter.optionsSetter(new ArrayList<Instrument>(instruments)) ); } ) );
        
        // put in labels
        this.scoreLabel = Label.textLabel( 0, 0.5f, 0.05f, Color.darkGray, GameFonts.ARIAL_PLAIN_32, (Function<Integer,String>) score -> score.toString() );
        this.timeElapsedLabel = Label.textLabel( 0, 0.65f, 0.05f, Color.darkGray, GameFonts.ARIAL_PLAIN_32, new TimeToString() );
        this.endTimeLabel = Label.textLabel( endTime, 0.75f, 0.05f, Color.darkGray, GameFonts.ARIAL_PLAIN_32, new TimeToString() );
    }

    @Override
    protected void handleServerMessages() {
        throw new RuntimeException("Not implemented yet");
    }
}
