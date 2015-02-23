package units;

import items.Lantern;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import audio.SoundClip;
import saving.SaveState;
import drawables.Background;
import drawables.Canvas;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;



// Global player data.
public class Player extends Unit  {
	
	// Sounds for dying 
	protected static SoundClip slash = new SoundClip("./../sounds/effects/slash.wav", true);
	protected static SoundClip groan = new SoundClip("./../sounds/effects/groan.wav", true);
	
	// Static stuff
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static int currPlayer = 0;
	
	// Dead?
	private static boolean dead = false;
	private static boolean deadAnimationPlayed = false;
	
	// Player constructor
	public Player(float x, float y) {
		super(20,64,new SpriteSheet("src/images/player/jack.png",
				64, 20, 64, 64, 20, 13), x, y); // Collision width/height.
		players.add(this);
		moveSpeed = 4;
		this.zIndex = 1;
		loadAnimations();
	}

	// Load all of the player's animations
	void loadAnimations() {
		walkingRight = new SpriteAnimation(spriteSheet, new int[] {
				11 * spriteSheet.getColsInSheet(),
				11 * spriteSheet.getColsInSheet() + 1,
				11 * spriteSheet.getColsInSheet() + 2,
				11 * spriteSheet.getColsInSheet() + 3,
				11 * spriteSheet.getColsInSheet() + 4,
				11 * spriteSheet.getColsInSheet() + 5,
				11 * spriteSheet.getColsInSheet() + 6}, 500);
		walkingRight.loop(true);
		walkingLeft = new SpriteAnimation(spriteSheet, new int[] {
				9 * spriteSheet.getColsInSheet(),
				9 * spriteSheet.getColsInSheet() + 1,
				9 * spriteSheet.getColsInSheet() + 2,
				9 * spriteSheet.getColsInSheet() + 3,
				9 * spriteSheet.getColsInSheet() + 4,
				9 * spriteSheet.getColsInSheet() + 5,
				9 * spriteSheet.getColsInSheet() + 6}, 500);
		walkingLeft.loop(true);
		jumpLeft = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet() + 6}, 500);
		jumpLeft.loop(false);
		jumpRight = new SpriteAnimation(spriteSheet, new int[] {
				3 * spriteSheet.getColsInSheet() + 6}, 500);
		jumpRight.loop(false);
		idleRight = new SpriteAnimation(spriteSheet, new int[] {
				7 * spriteSheet.getColsInSheet()}, 500);
		idleRight.loop(true);
		idleLeft = new SpriteAnimation(spriteSheet, new int[] {
				9 * spriteSheet.getColsInSheet()}, 500);
		idleLeft.loop(true);
		animate(jumpRight);
	}
	
	// Kill the player. This obviously loses the game.
	public void die() {
		dead = true;
		if(this == Unit.focusedUnit && dead && !deadAnimationPlayed) {
			playDeathAnimation();
		}
	}
	
	// Play death animation
	public void playDeathAnimation() {	
		//SaveState.purgeAll(); // Destroy everything on the screen.
		groan.start();
		slash.loop(3);
		Background.setBackground(Color.RED);
	}
	
	// Get the next player (tab through for now)
	public static void nextPlayer() {
		currPlayer++;
		if(currPlayer>=players.size()) {
			currPlayer = 0;
		}
		players.get(currPlayer).focus();
	}
	
	public static Player getCurrentPlayer() {
		if(players.contains(Unit.focusedUnit)) {
			return (Player)focusedUnit;
		}
		return null;
	}
}
