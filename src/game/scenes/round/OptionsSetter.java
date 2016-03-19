package game.scenes.round;

import game.Interlude;
import game.GameObject;
import game.buttons.Button;
import game.pop_ups.PopUp;
import game.scenes.SceneManager;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;

import music.Instrument;

public class OptionsSetter extends PopUp {
	private final List<Instrument> instruments;
	private final TimeDilator timeDilator;
	private final List<GameObject> renderables = new ArrayList<GameObject>();
	private static final float VOLUME_SLIDER_FRACTION_Y = 0.5f;
	private static final float SPEED_UP_SLIDER_FRACTION_X = 0.5f;
	private static final float SPEED_UP_SLIDER_FRACTION_Y = 0.8f;

	public static PopUp optionsSetter(List<Instrument> instruments,
			TimeDilator timeDilator) {
		OptionsSetter optionsSetter = new OptionsSetter(instruments,
				timeDilator);
		optionsSetter.init();
		return optionsSetter;
	}

	public OptionsSetter(List<Instrument> instruments, TimeDilator timeDilator) {
		this.instruments = instruments;
		this.timeDilator = timeDilator;
	}

	@Override
	public void render(Graphics g) {
		int containerWidth = Interlude.GAME_CONTAINER.getWidth();
		int containerHeight = Interlude.GAME_CONTAINER.getHeight();
		float topFractionPadding = 0.1f;
		float bottomFractionPadding = 0.2f;

		final float sliderTopFractionY = VOLUME_SLIDER_FRACTION_Y
				- VolumeSlider.FRACTION_HEIGHT / 2;
		final float topFractionY = sliderTopFractionY - topFractionPadding;
		final float fractionHeight = SPEED_UP_SLIDER_FRACTION_Y - topFractionY
				+ bottomFractionPadding;
		RoundedRectangle mat = new RoundedRectangle(0,
				(int) (containerHeight * topFractionY), containerWidth,
				(int) (containerHeight * fractionHeight), 20);
		g.setColor(Color.orange);
		g.fill(mat);
		renderables.stream().forEach(renderable -> renderable.render(g));
	}

	@Override
	public void update(int t) {
		renderables.stream().forEach(
				renderable -> renderable.update(t));
	}

	@Override
	public void init() {
		int numInstruments = instruments.size();
		for (int i = 0; i < numInstruments; i++) {
			Instrument instrument = instruments.get(i);
			renderables.add(VolumeSlider.volumeSlider(instrument,
					((float) i + 1) / (numInstruments + 1),
					VOLUME_SLIDER_FRACTION_Y));
		}
		renderables.add(SpeedUpSlider.speedUpSlider(SPEED_UP_SLIDER_FRACTION_X,
				0.65f, timeDilator));

		Button okayButton = Button.textButton("OK", 0.5f, 0.7f,
				(Runnable) () -> {
					this.remove(SceneManager.currentScene());
				});
		Button resetVolumeButton = Button
				.textButton(
						"Reset volume",
						0.75f,
						0.7f,
						(Runnable) () -> {
							for (GameObject renderable : renderables) {
								if (renderable instanceof VolumeSlider) {
									VolumeSlider volumeSlider = (VolumeSlider) renderable;
									volumeSlider.reset();
								}
							}
						});
		Button resetSpeedButton = Button
				.textButton(
						"Reset speed",
						0.9f,
						0.7f,
						(Runnable) () -> {
							for (GameObject renderable : renderables) {
								if (renderable instanceof SpeedUpSlider) {
									SpeedUpSlider speedUpSlider = (SpeedUpSlider) renderable;
									speedUpSlider.reset();
								}
							}
						});
		renderables.add(okayButton);
		renderables.add(resetVolumeButton);
		renderables.add(resetSpeedButton);
	}
}
