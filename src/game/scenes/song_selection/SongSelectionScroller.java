package game.scenes.song_selection;

import game.Interlude;
import game.buttons.Button;
import game.shapes.Shape;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import music.MusicFile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionScroller implements Scroller {
    private final String[] songTitles = new String[] {
            "Arrietty - Theme",
            "Shigatsu wa Kimi no Uso - Hikaru Nara",
            "Byousoku 5 centimeter - One More Time, One More Chance",
            "Kuroko no Basket - Can Do",
            "Oreimo - Irony",
            "Katy Perry - Firework",
            "Full Metal Panic - Sore ga Ai Desho",
            "Okami - Shinshuu Plains",
            "Little Busters! - Little Busters!",
            "Suzumiya Haruhi no Yuuutsu - Lost My Music",
            "Hunter X Hunter - Ohayou",
            "Suzumiya Haruhi no Yuuutsu - God Knows",
            "Gundam 00 - Ash Like Snow",
            "Guilty Crown - My Dearest",
            "Suzumiya Haruhi no Yuuutsu - Bouken Desho Desho",
            "Angel Beats - My Soul Your Beats",
            "Bakemonogatari - Kimi no Shiranai Monogatari",
            "Anime Piano Nantoka 2012",
            "Sword Art Online - Friendly Feelings",
            "Butterfly Lovers Concerto",
            "Spirited Away - Itsumo Nando Demo",
            "Card Captor Sakura - Arigatou",
            "Sword Art Online - Crossing Field",
            "Rurouni Kenshin - Sobakasu",
            "Detective Conan - Mune ga Doki Doki",
            "Ano Hana - Secret Base (10 Years After)",
            "Bokurano - Uninstall",
            "Gin no Saji - Hello Especially",
            "Digimon - Butterfly",
            "Kill la Kill - Sirius",
            "Ouran High School Host Club - Sakura Kiss",
            "Toradora - Lost My Pieces",
            "Whisper of the Heart - Country Roads",
            "Card Captor Sakura - Catch You Catch Me",
            "Haruhi Suzumiya no Yuuutsu - Hare Hare Yukai",
            "Air - Tori no Uta",
            "Card Captor Sakura - Platinum",
            "Beethoven's 5th Symphony",
            "THE iDOLM@STER - Here We Go!!",
            "Amagi Brillliant Park - Extra Magic Hour",
            "Code Geass - COLORS",
            "Frozen - Let It Go",
            "The World God Only Knows - God Only Knows",
            "White Album - Powdered Snow",
            "True Tears - Reflectia",
            "Ouran High School Host Club - Sakura Kiss (Full)",
            "Slam Dunk - Sekai ga Owaru Made wa",
            "Pachelbel Canon",
            "Girls Generation - Gee",
            "Slam Dunk - Kimi ga Suki Da to Sakebitai",
    };
    private final String[] fileNames = new String[] {
            "arrietty.mid",
            "Hikaru Nara.mid",
            "Byousoku 5 centimeter - One More Time, One More Chance.mid",
            "Kuroko no Basket - Can Do.mid",
            "Ore no Imouto ga Konanni Kawaii Wake ga Nai - Irony.mid",
            "katy_perry-firework.mid",
            "full-metal-panic-fumoffu-sore-ga-ai-deshou.mid",
            "Okami - Shinshuu Plains.mid",
            "Little Busters!.mid",
            "lostmymusic.mid",
            "Hunter X Hunter - Ohayou .mid", 
            "God Knows....mid",
            "Gundam 00 - Ash Like Snow.mid", 
            "My Dearest.mid", 
            "suzumiya-haruhi-no-yuuutsu-bouken-desho-desho.mid",
            "angel-beats-my-soul-your-beats.mid", 
            "Bakemonogatari - Kimi no Shiranai Monogatari.mid",
            "Anime Piano Nantoka 2012.mid", 
            "Sword Art Online - Friendly Feelings.mid",
            "vmysan27.mid",
            "Spirited Away - Itsumo Nando Demo.mid", 
            "Card Captor Sakura - Arigatou.mid", 
            "Sword Art Online - Crossing Field.mid",
            "Rurouni Kenshin - Sobakasu.mid",
            "Mune ga Doki Doki.mid",
            "secret base ~Kimi ga Kureta Mono~.mid",
            "Bokurano - Uninstall.mid",
            "Gin no Saji - Hello Especially.mid",
            "Butterfly.mid",
            "Sirius.mid",
            "Ouran High School Host Club - Sakura Kiss - string version.mid",
            "Toradora - Lost My Pieces.mid",
            "Whisper of the Heart - Country Roads.mid",
            "catch.mid",
            "Haruhi_ed.mid",
            "Air_op.mid",
            "Platinum.mid",
            "beethoven_symphony_5_1_(c)galimberti.mid",
            "Here We Go!!.mid",
            "Extra Magic Hour.mid",
            "COLORS.mid",
            "Let It Go.mid",
            "godonlyknows.mid",
            "powdersnow_live.mid",
            "omoiwooitekite.mid",
            "ouran-high-school-host-club-sakura-kiss-full-version.mid",
            "sdsekai.mid",
            "canon4.mid",
            "Girls Generation - Gee[ic3zz86].mid",
            "sdopening.mid"
            };
    
    private List<Button> songSelectionButtons = new ArrayList<Button>();
    private int firstIndex; // index of first song to include
    private int lastIndex; // index of last song to include
    
    private Shape downArrow = Shape.downArrow(0.5f,0.95f);
    private Shape upArrow = Shape.upArrow(0.5f,0.05f);

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for ( int index = firstIndex; index <= lastIndex; index++ ) {
            Button songSelectionButton = songSelectionButtons.get(index);
            songSelectionButton.render(g);
        }
        
        if ( lastIndex < songSelectionButtons.size() - 1 ) {
            downArrow.render(g);
        }
        
        if ( firstIndex > 0 ) {
            upArrow.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for ( Button songSelectionButton : songSelectionButtons ) {
            songSelectionButton.update(t);
        }
        
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        if ( input.isKeyPressed(Input.KEY_UP) ) {
            if ( firstIndex > 0 ) {
                firstIndex--;
                lastIndex--;
                for ( Button songSelectionButton : songSelectionButtons ) {
                    songSelectionButton.moveDown( 0.1f );
                }
            }
        } else if ( input.isKeyPressed(Input.KEY_DOWN) ) {
            if ( lastIndex < songSelectionButtons.size() - 1 ) {
                firstIndex++;
                lastIndex++;
                for ( Button songSelectionButton : songSelectionButtons ) {
                    songSelectionButton.moveUp( 0.1f );
                }
            }
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        float fractionY = 0.1f;
        for ( int i = 0; i < fileNames.length; i++ ) {
            String songTitle = songTitles[i];
            String fileName = fileNames[i];
            MusicFile musicFile = new MusicFile( songTitle, "midi/" + fileName );
            Button songSelectionButton = Button.textButton( songTitle, 0.5f, fractionY, new SongSelectEffect( musicFile ) );
            songSelectionButtons.add( songSelectionButton );
            fractionY += 0.1;
        }
        firstIndex = 0;
        lastIndex = Math.min(8, songSelectionButtons.size() - 1);
    }
    
    @Override
    public void mouseWheelMoved(int change) {
        change /= 120; // scale down the change. I believe they are in increments of 120.
        while ( change > 0 && firstIndex > 0 ) {
            change--;
            firstIndex--;
            lastIndex--;
            for ( Button songSelectionButton : songSelectionButtons ) {
                songSelectionButton.moveDown( 0.1f );
            }
        }
        while ( change < 0 && lastIndex < songSelectionButtons.size() - 1 ) {
            change++;
            firstIndex++;
            lastIndex++;
            for ( Button songSelectionButton : songSelectionButtons ) {
                songSelectionButton.moveUp( 0.1f );
            }
        }
    }
    
    @Override
    public void inputEnded() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inputStarted() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void setInput(Input arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }
}
