package units;

import items.Lantern;

import java.awt.Color;

import javax.sound.sampled.Clip;

import main.Main;
import terrain.TerrainChunk;
import audio.SoundClip;
import drawables.Background;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;

public class Chapman extends Unit {
	
	// Make sure there's only one Chapman at once.
	public static Chapman chapman = null;
	
	// How hard are we?
	public static int AILevel = 0; 
	
	// Static variables
	private static float meanderSpeed = 1;
	private static float chasingSpeed = 4;
	private static float meanderStop = 3000;
	private static int chaseRange = 150;
	private static int closeRange = 230;
	private static int killRange = 30;
	
	// States
	private boolean closeToPlayer = false;
	private boolean chasingPlayer = false;
	
	// Sounds for Chapman
	protected static SoundClip slash = new SoundClip("./../sounds/effects/slash.wav", true);
	protected static SoundClip groan = new SoundClip("./../sounds/effects/groan.wav", true);
	
	// Meandering
	protected static double meanderTime = 0;
	
	// Player constructor
	public Chapman(int difficulty) {
		super(20,64,new SpriteSheet("src/images/characters/Chapman.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		AILevel = difficulty;
		chapman = this;
		zIndex = 0;
		
		// Configure difficulty
		meanderSpeed = 1 + (float)AILevel/1.3f;
		chasingSpeed = 4f + (float)AILevel/2.3f;
		meanderStop = 3000 - 100*AILevel;
		chaseRange = 150+AILevel*4;
		closeRange = 230;
		killRange = 30+AILevel*2;
		moveSpeed = meanderSpeed;
		
		// Load animations
		loadAnimations();
	}
	
	// Load all of the animations
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
	
	// Obviously, update the unit every frame.
	public void updateUnit() {
		this.chapmanAI();
		this.gravity();
		this.move();
	}
	
	public void chapmanAI() {
		
		// STOP STATES
		// If the player has gotten 150 away, stop chasing.
		if(chasingPlayer && !Player.getCurrentPlayer().close(chaseRange,this)) {
			chasingPlayer = false;
			chasing.getClip().stop();
			movingRight = false;
			movingLeft = false;
			moveSpeed = meanderSpeed;
		}
		
		// INITIATE STATES
		// Kill player!
		if(Player.getCurrentPlayer().close(killRange,this)) {
			killPlayer();
		}
		// Chase player!
		else if(!chasingPlayer && Player.getCurrentPlayer().close(chaseRange,this) && Lantern.isToggle()) {
			chasingPlayer = true;
			chasing.getClip().loop(Clip.LOOP_CONTINUOUSLY);
			moveSpeed = chasingSpeed;
		}
		// Play noise if we're close to player!
		else if(!closeToPlayer && Player.getCurrentPlayer().close(closeRange,this)) {
			closeToPlayer = true;
			//close.getClip().start(); 
		}
		
		// STATES
		// Start chasing the player if he's close and has his lantern on.
		if(chasingPlayer) {
			follow(Player.getCurrentPlayer());
		}
		else {
			meander();
		}
		
		// Don't let him walk into walls.
		if(movingRight && TerrainChunk.touchingTerrain(this, "Right", -this.moveSpeed, 0)) {
			movingLeft = false;
			movingRight = false;
		}
		if(movingLeft && TerrainChunk.touchingTerrain(this, "Left", this.moveSpeed, 0)) {
			movingLeft = false;
			movingRight = false;
		}
	}
	
	public void killPlayer() {
		Player.getCurrentPlayer().die();
		groan.getClip().start();
		slash.getClip().loop(3);
		Background.setBackground(Color.RED);
	}
	
	public void meander() {
		if(Main.getGameTime() - meanderTime > meanderStop) {
			meanderTime = Main.getGameTime();
			if(!movingRight && !movingLeft) {
				if(TerrainChunk.touchingTerrain(this, "Right", -this.moveSpeed, 0)) {
					movingLeft = true;
					movingRight = false;
				}
				else if(TerrainChunk.touchingTerrain(this, "Left", this.moveSpeed, 0)) {
					movingLeft = false;
					movingRight = true;
				}
				else {
					if(Main.r.nextInt(2)==1) {
						if(AILevel > 4) if(Main.r.nextInt(5)==1) jump();
						movingRight = true;
						movingLeft = false;
					}
					else {
						if(AILevel > 4) if(Main.r.nextInt(5)==1) jump();
						movingLeft = true;
						movingRight = false;
					}
				}
			}
			else {
				movingRight = false;
				movingLeft = false;
			}
		}
	}
	
	public void follow(Unit u) {
		if(this.trans.getTranslateX() + this.moveSpeed <= u.trans.getTranslateX()) {
			this.movingRight = true;
			this.movingLeft = false;
			if(TerrainChunk.touchingTerrain(this, "Right", -this.moveSpeed, 0)) this.jump();
		}
		if(this.trans.getTranslateX() - this.moveSpeed >= u.trans.getTranslateX()) {
			this.movingRight = false;
			this.movingLeft = true;
			if(TerrainChunk.touchingTerrain(this, "Left", this.moveSpeed, 0)) this.jump();
		}
	}
}