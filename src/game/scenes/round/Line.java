package game.scenes.round;

import game.Interlude;
import game.settings.Orientation;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import util.Pair;

public class Line {

	private static final Color LINE_COLOR = Color.darkGray;

	private float fraction;

	public Line(float fraction) {
		this.fraction = fraction;
	}

	public void render(Graphics g) {
		int containerWidth = Interlude.GAME_CONTAINER.getWidth();
		int containerHeight = Interlude.GAME_CONTAINER.getHeight();

		Pair<Float, Float> mappedEndpoint1 = Orientation.getPosition(fraction,
				0);
		Pair<Float, Float> mappedEndpoint2 = Orientation.getPosition(fraction,
				1);

		g.setColor(LINE_COLOR);
		g.drawLine(containerWidth * mappedEndpoint1.getLeft(), containerHeight
				* mappedEndpoint1.getRight(),
				containerWidth * mappedEndpoint2.getLeft(), containerHeight
						* mappedEndpoint2.getRight());
	}

}
