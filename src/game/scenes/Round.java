package game.scenes;

import game.Controls;
import game.Hand;
import game.buttons.Button;
import game.moving_sound.MovingSound;
import game.moving_sound.OneVoiceMovingSound;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import music.Instrument;
import music.Music;
import music.MusicElement;
import music.Simultaneous;
import music.SoundElement;
import music.Rest;
import music.Note;
import music.Voice;

public abstract class Round implements Scene {
    private final Music music;
    private final boolean isMultiVoice;
    
    public Round(Music music) {
        this.music = music;
        this.isMultiVoice = music.isMultiVoice();
    }
    
    public Music music() {
        return music;
    }
    
    public boolean isMultiVoice() {
        return isMultiVoice;
    }
}
