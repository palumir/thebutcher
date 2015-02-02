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

public class Tabram extends Unit {
	
	// Make sure there's only one tabram at once.
	public static Tabram tabram = null;
	
	// How hard are we?
	public static int AILevel = 3; 
	
	// Static variables
	public float meanderSpeed = 1 + (float)AILevel/4;
	public float chasingSpeed = 4f + (float)AILevel/2f;
	
	// States
	private boolean closeToPlayer = false;
	private boolean chasingPlayer = false;
	
	// Sounds for Tabram
	protected static SoundClip slash = new SoundClip("./../sounds/effects/slash.wav", true);
	protected static SoundClip groan = new SoundClip("./../sounds/effects/groan.wav", true);
	
	// Meandering
	protected static double meanderTime = 0;
	
	// Player constructor
	public Tabram() {
		super(20,64,new SpriteSheet("src/images/characters/smith.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		tabram = this;
		zIndex = 0;
		tabram = this;
		moveSpeed = meanderSpeed;
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
		this.tabramAI();
		this.gravity();
		this.move();
	}
	
	public void tabramAI() {
		
		// Constants
		int chaseRange = 150+AILevel*3;
		int closeRange = 230;
		int killRange = 30;
		
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
		if(chasingPlayer && Player.getCurrentPlayer().close(killRange,this)) {
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
			close.getClip().start(); 
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
		slash.getClip().loop(4);
		Background.setBackground(Color.RED);
	}
	
	public void meander() {
		if(Main.getGameTime() - meanderTime > 3000) {
			System.out.println("Go");
			meanderTime = Main.getGameTime();
			if(!movingRight && !movingLeft) {
				System.out.println("Idle");
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
						movingRight = true;
						movingLeft = false;
					}
					else {
						movingLeft = true;
						movingRight = false;
					}
				}
			}
			else {
				System.out.println("Moving");
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