package game.scenes;

import game.Controls;
import game.Interlude;
import game.Orientation;
import game.Reflection;
import game.VoiceType;
import game.buttons.Button;
import game.moving_sound.MovingSound;
import game.note_marker.NoteMarker;
import game.pop_ups.PopUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import util.Pair;
import music.Instrument;
import music.Music;
import music.MusicElement;
import music.Simultaneous;
import music.SoundElement;
import music.Rest;
import music.Note;
import music.Voice;

public class Round implements Scene {
    private final Music music;
    private final boolean isMultiVoice;
    private final List<Button> buttons = new ArrayList<Button>();
    private final Set<VoiceType> voiceTypes = new HashSet<VoiceType>();
    /** Each note value and voicetype corresponds to a notemarker */
    private final Map<Pair<Integer,VoiceType>,NoteMarker> noteMarkers = new HashMap<Pair<Integer,VoiceType>,NoteMarker>();
    private final Map<VoiceType,Queue<MovingSound>> notesOnScreen = new HashMap<VoiceType,Queue<MovingSound>>();
    private final Map<VoiceType, Voice> voices = new HashMap<VoiceType, Voice>();
    private final Map<VoiceType,Integer> restingTimes = new HashMap<VoiceType,Integer>();
    
    private int orientation;
    
    public Round(Music music) {
        this.music = music;
        this.isMultiVoice = music.isMultiVoice();
        int initialDelay = 4000; // 4 seconds
        for ( Voice voice : music.voices() ) {
            VoiceType voiceType = voice.voiceType();
            voiceTypes.add(voiceType);
            voices.put(voiceType, voice);
            restingTimes.put(voiceType, initialDelay);
            notesOnScreen.put(voiceType, new LinkedList<MovingSound>());
        }
        
        this.orientation = 180;
    }
    
    @Override
    public void render(Graphics g) {
        for ( VoiceType voiceType : notesOnScreen.keySet() ) {
            Queue<MovingSound> notesOnScreenOfVoice = notesOnScreen.get(voiceType);
            for (MovingSound movingSound : notesOnScreenOfVoice) {
                movingSound.render(g);
            }
        }
        for (Button button : buttons) {
            button.render(g);
        }
        
        for ( Pair<Integer,VoiceType> pair : noteMarkers.keySet() ) {
            NoteMarker noteMarker = noteMarkers.get(pair);
            noteMarker.render(g);
        }
    }

    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        if ( input.isKeyPressed(Input.KEY_LEFT) ) {
            Orientation.rotateClockwise();
        } else if ( input.isKeyPressed(Input.KEY_RIGHT) ) {
            Orientation.rotateCounterClockwise();
        } else if ( input.isKeyPressed(Input.KEY_UP) ) {
            Reflection.verticallyMirror();
        } else if ( input.isKeyPressed(Input.KEY_DOWN) ) {
            Reflection.horizontallyMirror();
        }
        
        for ( Pair<Integer,VoiceType> pair : noteMarkers.keySet() ) {
            NoteMarker noteMarker = noteMarkers.get(pair);
            noteMarker.update(t);
        }
        
        for ( VoiceType voiceType : voiceTypes ) {
            int restingTime = restingTimes.get(voiceType);
            restingTime = Math.max(restingTime - t, 0);
            restingTimes.put(voiceType, restingTime);
            
            Voice voice = voices.get(voiceType);
            Queue<MovingSound> notesOnScreenOfVoice = notesOnScreen.get(voiceType);
            if (restingTime == 0) { // pick out another note!
                if (voice.ended()) {
                    // go to a different scene?
                } else {
                    MusicElement element = voice.next();
                    if ( element.isRest() ) {
                        restingTime = voice.timeUntilNextElement();
                        restingTimes.put(voiceType, restingTime);
                        if (!voice.ended()) {
                            restingTime = voice.timeUntilNextElement();
                            restingTimes.put(voiceType, restingTime);
                        }
                    } else {
                        SoundElement soundElement = (SoundElement) element;
                        int soundElementLetter = soundElement.letter();
                        float offScreen = 0.0f;
                        Pair<Integer,VoiceType> pair = new Pair<Integer,VoiceType>(soundElementLetter,voiceType);
                        if ( !isMultiVoice ) { 
                            MovingSound movingSound = new MovingSound( offScreen, noteMarkers.get(pair).fractionY(), soundElement, voiceType );
                            movingSound.init();
                            notesOnScreenOfVoice.add( movingSound );
                        } else {
                            MovingSound movingSound = new MovingSound( offScreen, noteMarkers.get(pair).fractionY(), soundElement, voiceType );
                            movingSound.init();
                            notesOnScreenOfVoice.add( movingSound );
                        }
                        if (!voice.ended()) {
                            restingTime = voice.timeUntilNextElement();
                            restingTimes.put(voiceType, restingTime);
                        }
                    }
                }
            }
            
            if ( !notesOnScreenOfVoice.isEmpty() ) { 
                while ( notesOnScreenOfVoice.peek().offScreen() ) {
                    notesOnScreenOfVoice.remove();
                    if (notesOnScreenOfVoice.isEmpty()) {
                        break;
                    }
                }
            }
            
            for ( MovingSound movingSound : notesOnScreenOfVoice ) {
                movingSound.update(t);
            }
        }
        
        for ( VoiceType voiceType : voiceTypes ) {
            for ( int key : Controls.noteKeys( voiceType ) ) {
                if ( input.isKeyPressed(key) ) {
                    Queue<MovingSound> notesOnScreenOfVoice = notesOnScreen.get(voiceType);
                    if ( !notesOnScreenOfVoice.isEmpty() ) {
                        int letter = Controls.correspondingNote(key, voiceType );
                        MovingSound movingSound = notesOnScreenOfVoice.remove();
                        SoundElement soundElement = movingSound.soundElement();
                        SoundElement correspondingSoundElement = soundElement.correspondingSoundElement(letter);
                        soundElement.bePlayed(voices.get(voiceType).instrument());
                    }
                }
            }
        }
    }
    
    @Override
    public void init() {
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
        for (int note : notes) {
            for ( VoiceType voiceType : voiceTypes ) {
                NoteMarker noteMarker = new NoteMarker(note, voiceType);
                noteMarker.init();
                noteMarkers.put( new Pair<Integer,VoiceType>(note,voiceType), noteMarker );
            }
        }
    }
    
    public Music music() {
        return music;
    }
    
    public boolean isMultiVoice() {
        return isMultiVoice;
    }

    @Override
    public void fireActivatedButtons() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return null;
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
