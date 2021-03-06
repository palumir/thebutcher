package main;

import items.Lantern;
import items.LanternFuel;

import java.util.Random;

import javax.swing.JFrame;

import terrain.Maps.Forest;
import units.Chapman;
import units.Nichols;
import units.Player;
import units.Smith;
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
		JFrame frame = new JFrame("Monk");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
		frame.setContentPane(Canvas.getGameCanvas());
		frame.setVisible(true);
	}
	
	public static void createNewGameScene() {
		// Spawn our map first.
		Forest f = new Forest();
		
		// Create the player. Move him to the middle of the screen.
		Player player1 = new Player(Canvas.getDefaultWidth()/2, Canvas.getDefaultHeight()/2);
		Lantern lantern1 = new Lantern(Canvas.getDefaultWidth()/2,Canvas.getDefaultHeight()/2,200);
		player1.setLantern(lantern1);
		player1.focus();
	
		Player player2 = new Player(Canvas.getDefaultWidth()/2, Canvas.getDefaultHeight()/2);
		Lantern lantern2 = new Lantern(Canvas.getDefaultWidth()/2,Canvas.getDefaultHeight()/2,200);
		player2.setLantern(lantern2);
		
		// Configure enemy AI
		Smith.setAI(3);
		Nichols.setAI(3);
		Chapman.setAI(3);
		
		// Always spawn nichols.
		Nichols nichols = new Nichols();

	}
	
	public static long getGameTime() {
		return System.currentTimeMillis(); // To-do: Return game time instead of system time.
	}
}

