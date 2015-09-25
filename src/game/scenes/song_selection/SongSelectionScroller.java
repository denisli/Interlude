package game.scenes.song_selection;

import game.Interlude;
import game.buttons.Button;
import game.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

import music.MusicFile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionScroller implements Scroller {
	private static final String[] songTitles = new String[] {
			"Pokemon - Lugia's Song", "Bleach - Houki Boushi (Piano)",
			"Bleach - Houki Boushi (Full Instrumental)",
			"Bleach - Rolling Star", "Pokemon Medley", "Hiiro no Kakera - Nee",
			"Higurashi no Naku Koro Ni - Dear You", "Owl City - Fireflies",
			"Tina Turner - Simply the Best",
			"Pokemon Trainer Battle (Orchestrated)",
			"Fort Minor - Where'd You Go", "Fort Minor - Remember the Name",
			"The Garden of Words - Rain", "Clannad Medley",
			"Littleroot Town 2", "Inuyasha - Dearest", "Pokemon Center Theme",
			"Full Metal Alchemist - I Will", "Naruto - Wind",
			"Linkin Park - What I've Done", "Linkin Park - Numb",
			"Green Day - Boulevard of Broken Dreams",
			"Parasyte - It's the Right Time", "Inuyasha - Every Heart",
			"Durarara!!! - Trust me", "Final Fantasy IX - Melodies of Life",
			"Suzumiya Haruhi no Yuuutsu - Lost My Music (Piano)",
			"Emma - A Victorian Romance - Silhouette of a Breeze",
			"Tasogare Otome x Amnesia - Choir Jail", "Arrietty - Theme",
			"Shigatsu wa Kimi no Uso - Hikaru Nara",
			"Byousoku 5 centimeter - One More Time, One More Chance",
			"Kuroko no Basket - Can Do", "Oreimo - Irony",
			"Katy Perry - Firework", "Full Metal Panic - Sore ga Ai Desho",
			"Okami - Shinshuu Plains", "Little Busters! - Little Busters!",
			"Suzumiya Haruhi no Yuuutsu - Lost My Music",
			"Hunter X Hunter - Ohayou",
			"Suzumiya Haruhi no Yuuutsu - God Knows",
			"Gundam 00 - Ash Like Snow", "Guilty Crown - My Dearest",
			"Suzumiya Haruhi no Yuuutsu - Bouken Desho Desho",
			"Angel Beats - My Soul Your Beats",
			"Bakemonogatari - Kimi no Shiranai Monogatari",
			"Anime Piano Nantoka 2012", "Sword Art Online - Friendly Feelings",
			"Butterfly Lovers Concerto", "Spirited Away - Itsumo Nando Demo",
			"Card Captor Sakura - Arigatou",
			"Sword Art Online - Crossing Field", "Rurouni Kenshin - Sobakasu",
			"Detective Conan - Mune ga Doki Doki",
			"Ano Hana - Secret Base (10 Years After)", "Bokurano - Uninstall",
			"Gin no Saji - Hello Especially", "Digimon - Butterfly",
			"Kill la Kill - Sirius",
			"Ouran High School Host Club - Sakura Kiss",
			"Toradora - Lost My Pieces",
			"Whisper of the Heart - Country Roads",
			"Card Captor Sakura - Catch You Catch Me",
			"Haruhi Suzumiya no Yuuutsu - Hare Hare Yukai",
			"Air - Tori no Uta", "Card Captor Sakura - Platinum",
			"Beethoven's 5th Symphony", "THE iDOLM@STER - Here We Go!!",
			"Amagi Brillliant Park - Extra Magic Hour", "Code Geass - COLORS",
			"Frozen - Let It Go", "The World God Only Knows - God Only Knows",
			"White Album - Powdered Snow", "True Tears - Reflectia",
			"Ouran High School Host Club - Sakura Kiss (Full)",
			"Slam Dunk - Sekai ga Owaru Made wa", "Pachelbel Canon",
			"Girls Generation - Gee",
			"Slam Dunk - Kimi ga Suki Da to Sakebitai", };
	private static final String[] fileNames = new String[] {
			"pokemon-lugias-song.mid",
			"bleach-houki-boshi-piano-solo.mid",
			"bleach-houki-boshi.mid",
			"bleach-rolling-star.mid",
			"Pokemon-Medley.mid",
			"Hiiro no Kakera - Nee (AnimeMidi).mid",
			"you_original.mid",
			"Fireflies - Owl City.mid",
			"Tina_Turner_-_Simply_the_Best.mid",
			"trainer-battle-orchestrated-.mid",
			"WheredYouGo.mid",
			"RememberTheName.mid",
			"The Garden of Words ED - Rain.mid",
			"clannad-medley.mid",
			"littleroot-town-2-.mid",
			"22648_Dearest.mid",
			"pokemon-center.mid",
			"fmaiwill.mid",
			"23219_Wind.mid",
			"26799_What-Ive-Done.mid",
			"27553_Numb.mid",
			"26885_Boulevard-of-Broken-Dreams.mid",
			"Kiseijuu Sei no Kakuritsu (Parasyte) ED - It's The Right Time [Final] v3.mid",
			"every_heart.mid",
			"Trust me.mid",
			"melodies-of-life-final-fantasy.mid",
			"The Melancholy of Haruhi Suzumiya - Lost my music (piano).mid",
			"Emma - A Victorian Romance - Silhouette of a Breeze - anime-tomodachi.blogspot.com.mid",
			"tasogare otome x amnesia - Choir Jail.mid", "arrietty.mid",
			"Hikaru Nara.mid",
			"Byousoku 5 centimeter - One More Time, One More Chance.mid",
			"Kuroko no Basket - Can Do.mid",
			"Ore no Imouto ga Konanni Kawaii Wake ga Nai - Irony.mid",
			"katy_perry-firework.mid",
			"full-metal-panic-fumoffu-sore-ga-ai-deshou.mid",
			"Okami - Shinshuu Plains.mid", "Little Busters!.mid",
			"lostmymusic.mid", "Hunter X Hunter - Ohayou .mid",
			"God Knows....mid", "Gundam 00 - Ash Like Snow.mid",
			"My Dearest.mid",
			"suzumiya-haruhi-no-yuuutsu-bouken-desho-desho.mid",
			"angel-beats-my-soul-your-beats.mid",
			"Bakemonogatari - Kimi no Shiranai Monogatari.mid",
			"Anime Piano Nantoka 2012.mid",
			"Sword Art Online - Friendly Feelings.mid", "vmysan27.mid",
			"Spirited Away - Itsumo Nando Demo.mid",
			"Card Captor Sakura - Arigatou.mid",
			"Sword Art Online - Crossing Field.mid",
			"Rurouni Kenshin - Sobakasu.mid", "Mune ga Doki Doki.mid",
			"secret base ~Kimi ga Kureta Mono~.mid",
			"Bokurano - Uninstall.mid", "Gin no Saji - Hello Especially.mid",
			"Butterfly.mid", "Sirius.mid",
			"Ouran High School Host Club - Sakura Kiss - string version.mid",
			"Toradora - Lost My Pieces.mid",
			"Whisper of the Heart - Country Roads.mid", "catch.mid",
			"Haruhi_ed.mid", "Air_op.mid", "Platinum.mid",
			"beethoven_symphony_5_1_(c)galimberti.mid", "Here We Go!!.mid",
			"Extra Magic Hour.mid", "COLORS.mid", "Let It Go.mid",
			"godonlyknows.mid", "powdersnow_live.mid", "omoiwooitekite.mid",
			"ouran-high-school-host-club-sakura-kiss-full-version.mid",
			"sdsekai.mid", "canon4.mid", "Girls Generation - Gee[ic3zz86].mid",
			"sdopening.mid" };

	private List<Button> songSelectionButtons = new ArrayList<Button>();
	private int firstIndex = 0; // index of first song to include
	private int lastIndex = Math.min(8, songTitles.length - 1);; // index of
																	// last song
																	// to
																	// include

	private Shape downArrow = Shape.downArrow(0.5f, 0.95f);
	private Shape upArrow = Shape.upArrow(0.5f, 0.05f);

	private static SongSelectionScroller songSelectionScroller;

	private SongSelectionScroller() {

	}

	public static SongSelectionScroller getSongSelectionScroller() {
		if (songSelectionScroller == null) {
			songSelectionScroller = new SongSelectionScroller();
		}
		return songSelectionScroller;
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		for (Button songSelectionButton : songSelectionButtons) {
			songSelectionButton.render(g);
		}

		if (lastIndex < songTitles.length - 1) {
			downArrow.render(g);
		}

		if (firstIndex > 0) {
			upArrow.render(g);
		}
	}

	@Override
	public void update(int t) {
		for (Button songSelectionButton : songSelectionButtons) {
			songSelectionButton.update(t);
		}

		Input input = Interlude.GAME_CONTAINER.getInput();

		if (input.isKeyPressed(Input.KEY_UP)) {
			if (firstIndex > 0) {
				firstIndex--;
				lastIndex--;
			}
			updateSongSelectionButtons();
		} else if (input.isKeyPressed(Input.KEY_DOWN)) {
			if (lastIndex < songTitles.length - 1) {
				firstIndex++;
				lastIndex++;
			}
			updateSongSelectionButtons();
		}
	}

	@Override
	public void init() {
		updateSongSelectionButtons();
	}

	private void updateSongSelectionButtons() {
		songSelectionButtons.clear();
		for (int index = firstIndex; index <= lastIndex; index++) {
			int windowIndex = index - firstIndex + 1;
			float increment = 0.1f;
			String songTitle = songTitles[index];
			String fileName = fileNames[index];
			MusicFile musicFile = new MusicFile(songTitle, "midi/" + fileName);
			Button songSelectionButton = Button.textButton(songTitle, 0.5f,
					windowIndex * increment, new SongSelectEffect(musicFile));
			songSelectionButtons.add(songSelectionButton);
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		change /= 120; // scale down the change. I believe they are in
						// increments of 120.
		// System.out.println(change);
		while (change > 0 && firstIndex > 0) {
			change--;
			firstIndex--;
			lastIndex--;
		}
		while (change < 0 && lastIndex < songTitles.length - 1) {
			change++;
			firstIndex++;
			lastIndex++;
		}
		updateSongSelectionButtons();
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
