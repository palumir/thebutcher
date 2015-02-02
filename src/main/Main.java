package main;

import items.Lantern;

import java.awt.geom.GeneralPath;
import java.util.Random;

import javax.swing.JFrame;

import terrain.Forest;
import units.Player;
import units.Tabram;
import drawables.Canvas;

/**
 * The main class to run the program.
 */
public class Main {
	
	// Global stuff we'll need for everything. Has to be done.
	public static Random r = new Random();
	
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
		Forest f = new Forest();
		Lantern l = new Lantern();
		Tabram s = new Tabram();
		s.instantlyMove(Canvas.getDefaultWidth()/2-400,Canvas.getDefaultHeight()/2);
		
		// Create the actual game frame on the computer screen.
		JFrame frame = new JFrame("The Butcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
		frame.setContentPane(Canvas.getGameCanvas());
		frame.setVisible(true);
	}
	
	public static long getGameTime() {
		return System.currentTimeMillis(); // To-do: Return game time instead of system time.
	}
}

