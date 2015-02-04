package drawables;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import main.Main;

public class Background {
	private static Color c = new Color(35,41,38);
	static Random r = Main.r;
	public static void paintBackground(Graphics2D g2, int width, int height) {
		g2.setColor(getC());
		g2.fillRect(0, 0, width, height);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public static void setBackground(Color co) {
		setC(co);
	}

	public static Color getC() {
		return c;
	}

	public static void setC(Color c) {
		Background.c = c;
	}
}