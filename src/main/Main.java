package main;

import items.Lantern;

import java.util.Random;

import javax.swing.JFrame;

import terrain.PurpleHills;
import terrain.TerrainChunk;
import units.Player;
import units.Tabram;
import units.Unit;
import audio.SoundClip;
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
		PurpleHills f = new PurpleHills();
		Lantern l = new Lantern();
		Tabram s = new Tabram();
		s.instantlyMove(Canvas.getDefaultWidth()/2-(r.nextInt(1500)),Canvas.getDefaultHeight()/2);
		
		// Create the actual game frame on the computer screen.
		JFrame frame = new JFrame("SPOOKY GAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
		frame.setContentPane(Canvas.getGameCanvas());
		frame.setVisible(true);
	}
	
	// Clear everything from the game to move on to another phase.
	public static void purge() {
		// Clear everything.
		Unit.units.clear();
		TerrainChunk.getTerrain().clear();
		Canvas.getGameCanvas().getNodes().clear();
		SoundClip.stopSounds();
	}
	
	public static long getGameTime() {
		return System.currentTimeMillis(); // To-do: Return game time instead of system time.
	}
}

