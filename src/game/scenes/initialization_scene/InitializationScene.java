package game.scenes.initialization_scene;

import game.Interlude;
import game.scenes.Scene;
import game.scenes.SceneManager;
import music.LoadSynthesizer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class InitializationScene extends Scene {

	private static final int MY_SCREEN_WIDTH = 1920;
	private static final int MY_SCREEN_HEIGHT = 1080;
	private static final int MINIMUM_LOADING_TIME = 1250;
	private static final int FULLY_OPAQUE_IMAGES_TIME = 1000;

	private Image eighthNote;
	private Image interlude;
	private int remainingLoadingTime = MINIMUM_LOADING_TIME;
	private Thread synthesizerLoadingThread;

	@Override
	public Scene parentScene() {
		return null;
	}

	@Override
	public void render(Graphics g) {
		int containerWidth = Interlude.GAME_CONTAINER.getWidth();
		int containerHeight = Interlude.GAME_CONTAINER.getHeight();

		int eighthNoteWidth = eighthNote.getWidth();
		int eighthNoteHeight = eighthNote.getHeight();
		int eighthNoteCenterX = containerWidth / 2;
		int eighthNoteCenterY = 2 * containerHeight / 3;
		int interludeWidth = interlude.getWidth();
		int interludeHeight = interlude.getHeight();
		int interludeCenterX = containerWidth / 2;
		int interludeCenterY = containerHeight / 3;
		eighthNote.draw(eighthNoteCenterX - eighthNoteWidth / 2,
				eighthNoteCenterY - eighthNoteHeight / 2);
		interlude.draw(interludeCenterX - interludeWidth / 2, interludeCenterY
				- interludeHeight / 2);
	}

	@Override
	public void update(int t) {
		remainingLoadingTime -= t;
		if (remainingLoadingTime <= 0) {
			if (!synthesizerLoadingThread.isAlive()) {
				SceneManager.setNewScene(Scene.mainMenu());
			}
		}
		double timeElapsed = MINIMUM_LOADING_TIME - remainingLoadingTime;
		float alpha = (float) Math.min(1, timeElapsed / FULLY_OPAQUE_IMAGES_TIME);
		eighthNote.setAlpha(alpha);
		interlude.setAlpha(alpha);
	}

	@Override
	public void init() {
		super.init();
		synthesizerLoadingThread = new Thread(new Runnable() {
			public void run() {
				LoadSynthesizer.loadSynthesizer();
			}
		});
		synthesizerLoadingThread.start();
	}

	@Override
	public void cleanUp() {

	}

	@Override
	protected void layout() {
		int containerWidth = Interlude.GAME_CONTAINER.getWidth();
		int containerHeight = Interlude.GAME_CONTAINER.getHeight();

		try {
			// Set the images and scale them to the screen dimensions of the user
			eighthNote = new Image("images/eighth_note.png");
			interlude = new Image("images/interlude.png");
			double screenWidthRatio = (double) containerWidth / MY_SCREEN_WIDTH;
			double screenHeightRatio = (double) containerHeight
					/ MY_SCREEN_HEIGHT;
			eighthNote = eighthNote
					.getScaledCopy(
							(int) Math.ceil(eighthNote.getWidth()
									* screenWidthRatio),
							(int) Math.ceil(eighthNote.getHeight()
									* screenHeightRatio));
			interlude = interlude.getScaledCopy(
					(int) Math.ceil(interlude.getWidth() * screenWidthRatio),
					(int) Math.ceil(interlude.getHeight() * screenHeightRatio));
			// Ensure that they are initially transparent
			eighthNote.setAlpha(0);
			interlude.setAlpha(0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
