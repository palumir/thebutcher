package main;

import java.awt.Color;
import java.awt.Frame;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;

import terrain.Forest;
import units.Player;
import units.Unit;
import drawables.Canvas;
import drawables.Node;

/**
 * The main class to run the program.
 */
public class Main {
	
	// Fucking remove this shit
	public static final GeneralPath heart = Main.makeHeart();
	
	// Initialize the game. Stuff that MUST be done.
	public static void initGame() {
		Canvas.initCanvas();
	}
	
	public static void main(String[] args) {
		
		// Initialize the game. This is stuff that has to be done for
		// anything else to work, and things not to crash.
		initGame();
		
		// Create a character for testing.
		Player player = new Player();
		player.instantlyMove(Canvas.getDefaultWidth()/2,Canvas.getDefaultHeight()/2);
		Forest forest = new Forest();
		
		// Create the actual game frame on the computer screen.
		JFrame frame = new JFrame("The Butcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
		frame.setContentPane(Canvas.getGameCanvas());
		frame.setVisible(true);
	}

	/**
	 * Make a heart-shaped shape at the origin.
	 */
	private static GeneralPath makeHeart() {
		GeneralPath heart = new GeneralPath();
		heart.moveTo(0, 0);
		heart.curveTo(0, -5, 5, -10, 10, -10);
		heart.curveTo(15, -10, 20, -5, 20, 0);
		heart.curveTo(20, 10, 5, 25, 0, 30);
		heart.curveTo(-5, 25, -20, 10, -20, 0);
		heart.curveTo(-20, -5, -15, -10, -10, -10);
		heart.curveTo(-5, -10, 0, -5, 0, 0);
		return heart;
	}
	
	public static long getGameTime() {
		
		return System.currentTimeMillis(); // To-do: Return game time instead of system time.
	}
}

