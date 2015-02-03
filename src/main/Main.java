package main;

import items.Lantern;

import java.util.Random;

import javax.swing.JFrame;

import terrain.PurpleHills;
import units.Nichols;
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
		
		// Create the new game.
		createNewGameScene();
		
		// Create the actual game frame on the computer screen.
		JFrame frame = new JFrame("SPOOKY GAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
		frame.setContentPane(Canvas.getGameCanvas());
		frame.setVisible(true);
	}
	
	public static void createNewGameScene() {
		// Create a character for testing.
		Player player = new Player();
		player.instantlyMove(Canvas.getDefaultWidth()/2,Canvas.getDefaultHeight()/2);
		PurpleHills f = new PurpleHills();
		Lantern l = new Lantern();
		Tabram s = new Tabram(1);
		Nichols n = new Nichols(1);
		s.instantlyMove(Canvas.getDefaultWidth()/2-(r.nextInt(1500)),Canvas.getDefaultHeight()/2);
	}
	
	public static long getGameTime() {
		return System.currentTimeMillis(); // To-do: Return game time instead of system time.
	}
}

