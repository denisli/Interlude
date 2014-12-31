package game.scenes.round;

import game.Interlude;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.Scene;
import game.scenes.SceneManager;
import game.server_client.Client;
import game.settings.Controls;
import game.settings.GameplayType;
import game.settings.GameplayTypeSetting;
import game.settings.Orientation;
import game.settings.Volume;

import java.util.ArrayList;
import java.util.Arrays;
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
import music.Simultaneous;
import music.SoundElement;
import music.Note;
import music.Voice;

public class Round extends Scene {
    private final Music music;
    private final Instrument selectedInstrument;
    
    /** COLLECTIONS */
    private final List<Button> buttons = new ArrayList<Button>();
    /* Each note value and voicetype corresponds to a notemarker */
    private final Map<Pair<Integer,Handedness>,NoteMarker> noteMarkers = new HashMap<Pair<Integer,Handedness>,NoteMarker>();
    private final Map<Handedness,Queue<MovingSound>> notesOnScreenOfHand = new HashMap<Handedness,Queue<MovingSound>>();
    private final List<Handedness> handednesses = new ArrayList<Handedness>();
    private final List<Voice> voices;
    /* restingTimes: Key is the voice index. The value is the resting time */
    private final Map<Integer,Integer> restingTimes = new HashMap<Integer,Integer>();
    private final Map<String,Instrument> instrumentNameToInstrument = new HashMap<String,Instrument>();

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
            notesOnScreenOfHand.put(handedness,new LinkedList<MovingSound>());
        }
        
        List<Integer> timesUntilVoicesStart = music.timesUntilVoicesStart();
        this.voices = music.voices();
        
        int initialDelay = 3000; // 3 seconds
        int timeToReachNoteMarker = (int) ( 0.85f / (MovingSound.speed()) );
        for ( int voiceIndex = 0; voiceIndex < voices.size(); voiceIndex++ ) {
            Voice voice = voices.get(voiceIndex);
            if ( !voice.instrument().equals(selectedInstrument) ) {
                restingTimes.put(voiceIndex, initialDelay + timeToReachNoteMarker + timesUntilVoicesStart.get(voiceIndex));
            } else {
                restingTimes.put(voiceIndex, initialDelay + /*debugging*/ timeToReachNoteMarker + timesUntilVoicesStart.get(voiceIndex));
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        noteMarkers.keySet().stream().forEach( integerAndHand -> noteMarkers.get(integerAndHand).render(g) );
        
        handednesses.stream().forEach( handedness -> {
            notesOnScreenOfHand.get(handedness).stream().forEach( movingSound -> movingSound.render(g) );
        });
            
        buttons.stream().forEach( button -> button.render(g));
        
        scoreLabel.render(g);
        timeElapsedLabel.render(g);
        endTimeLabel.render(g);
        
        super.render(g);
    }

    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        
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
        
        // Update each button
        buttons.stream().forEach( button -> button.update(t) );
        
        // Update note markers
        noteMarkers.keySet().stream().forEach( integerAndHand -> noteMarkers.get(integerAndHand).update(t) );
        
        if ( !paused ) {
            // Update timeElapsedLabel if music started
            if ( musicStarted ) {
                timeElapsed += t;
                timeElapsedLabel.updateValue(timeElapsed);
            }
            
            if ( timeElapsed > endTime + 4000 ) {
                SceneManager.setNewScene( Scene.results(music.title(), totalScore) );
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
                    if ( !voice.ended() ) {
                        SoundElement soundElementToInsert = voice.next();
                        
                        int soundElementLetter = soundElementToInsert.integer();
                        float offScreen = 0.0f;
                        
                        if ( instrument.equals(selectedInstrument) ) {
                            Handedness handedness = voice.handedness();
                            System.out.println(instrument.getInstrumentName());
                            Queue<MovingSound> notesOnScreen = notesOnScreenOfHand.get(handedness);
                            Pair<Integer,Handedness> pair = new Pair<Integer,Handedness>(soundElementLetter, handedness);
                            MovingSound movingSound = new MovingSound( offScreen, noteMarkers.get(pair).fractionY(), soundElementToInsert, handedness );
                            movingSound.init();
                            notesOnScreen.add( movingSound );
                        } else {
                            System.out.println(instrument.getInstrumentName());
                            soundElementToInsert.bePlayed(instrument);
                        }
                        if (!voice.ended()) {
                            restingTime += voice.timeUntilNextElement();
                            restingTimes.put(voiceIndex, restingTime);
                        }
                    }
                }
                
                // If a user plays a note, perform all necessary actions due to his input.
                // This includes playing a note and updating score.
                if ( instrument.equals(selectedInstrument) ) {
                    Handedness handedness = voice.handedness();
                    for ( int key : Controls.noteKeys( handedness ) ) {
                        if ( input.isKeyPressed(key) ) {
                            Queue<MovingSound> notesOnScreen = notesOnScreenOfHand.get(handedness);
                            if ( !notesOnScreen.isEmpty() ) {
                                int letter = Controls.correspondingNote(key, handedness );
                                MovingSound movingSound = notesOnScreen.remove();
                                SoundElement soundElementToPlay = movingSound.soundElement();
                                SoundElement correspondingSoundElementToPlay = soundElementToPlay.correspondingSoundElement(letter);
                                correspondingSoundElementToPlay.bePlayed(instrument);
                                if ( letter != movingSound.soundElement().integer() ) {
                                    totalScore -= 30;
                                } else {
                                    totalScore += (int) (100 * ( 1 - ( ( Math.abs( 0.85f - movingSound.fraction() ) ) / 0.85f ) ) );
                                }
                                scoreLabel.updateValue( totalScore );
                            }
                        }
                    }
                }
            }
            
         // Remove any notes that are no longer on screen.
            for ( Handedness handedness : handednesses ) {
                Queue<MovingSound> notesOnScreen = notesOnScreenOfHand.get(handedness);
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
            }
        } else {
            input.clearKeyPressedRecord();
        }
        
        super.update(t);
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
    }

    @Override
    protected void layout() {
        // put in note markers onto the scene
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
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
        while ( !Client.noMoreMessagesToProcess() ) {
            String message = Client.removeMessageToProcess();
            Queue<String> tokens = new LinkedList<String>(Arrays.asList(message.split(" ")));
            String firstToken = tokens.remove();
            if ( firstToken.equals("play") ) {
                String instrumentName = tokens.remove();
                Instrument instrument = instrumentNameToInstrument.get(instrumentName);
                List<Note> notes = new ArrayList<Note>();
                while ( !tokens.isEmpty() ) {
                    int pitch = Integer.parseInt(tokens.remove());
                    int duration = Integer.parseInt(tokens.remove());
                    int volume = Integer.parseInt(tokens.remove());
                    Note note = new Note( pitch, duration, volume );
                    notes.add(note);
                }
                if ( notes.size() == 1 ) {
                    notes.get(0).bePlayed( instrument );
                } else {
                    Simultaneous simultaneous = new Simultaneous( notes );
                    simultaneous.bePlayed( instrument );
                }
            }
        }
    }
}
