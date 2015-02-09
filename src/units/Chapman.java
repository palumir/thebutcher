package units;

import items.Lantern;

import java.awt.Color;

import javax.sound.sampled.Clip;

import main.Main;
import terrain.TerrainChunk;
import audio.SoundClip;
import drawables.Background;
import drawables.Canvas;
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
	private static double minSpawnInterval = 30000;
	private static double spawnCheck = 10000;
	
	// When we he last spawned?
	private static double lastSpawn = 0;
	
	// States
	private boolean closeToPlayer = false;
	private boolean chasingPlayer = false;
	private boolean passed = false; // Has the player actually passed chapman?
	
	// Sounds for Chapman
	protected static SoundClip slash = new SoundClip("./../sounds/effects/slash.wav", true);
	protected static SoundClip groan = new SoundClip("./../sounds/effects/groan.wav", true);
	
	// Meandering
	protected static double meanderTime = 0;
	
	// Player constructor
	public Chapman() {
		super(20,64,new SpriteSheet("src/images/characters/Chapman.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		chapman = this;
		zIndex = 0;
		passed = false;
		
		// Configure difficulty
		meanderSpeed = 1 + (float)AILevel/1.3f;
		chasingSpeed = 4f + (float)AILevel/2.3f;
		meanderStop = 3000 - 100*AILevel;
		chaseRange = 150+AILevel*4;
		closeRange = 230;
		killRange = 30+AILevel*2;
		moveSpeed = meanderSpeed;
		minSpawnInterval = 30000 + AILevel*6000;
		
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
		this.AI();
		this.gravity();
		this.move();
	}
	
	// What to do when Chapman isn't spawned?
	public static void spawnOrDespawn() {
		if(chapman==null) {
			
			// Spawn randomly. WIP: THIS SHOULD BE TIMER BASED TOO AT A POINT
			if(Main.r.nextInt(2) == 1) {
				spawn();
			}
		
		}
		
		// Delete Chapman if we move past and he goes off screen, or if we move 3 screens away from him.
		else if((chapman.passed && !chapman.close(Math.max(Canvas.getDefaultWidth()/2, Canvas.getDefaultHeight()/2),Player.getCurrentPlayer())) ||
				!chapman.close(Math.max(3*Canvas.getDefaultWidth()/2, 3*Canvas.getDefaultHeight()/2),Player.getCurrentPlayer())) {
			chapman.deleteUnit(); 
			chapman = null;
		}
	}
	
	// Spawn chapman
	public static void spawn() {
		Chapman c = new Chapman();
		Player p = Player.getCurrentPlayer();
		
		// Is the player moving left or right? Don't spawn Chapman behind the player, silly!
		int leftOrRight = 1;
		if(p.facingLeft) leftOrRight = -1;
		
		// Move to off screen. Don't care about terrain at this point
		c.instantlyMove((float)(p.trans.getTranslateX() + leftOrRight*(Canvas.getDefaultWidth()/2+100)),(float)(p.trans.getTranslateY()));
		
		// If we're in terrain, then move up until we're not.
		while(TerrainChunk.inTerrain(c)) {
			c.instantlyMove(0, -5);
		}
	}
	
	public void AI() {
		
		// STOP STATES
		// If the player has gotten 150 away, stop chasing.
		if(chasingPlayer && !Player.getCurrentPlayer().close(chaseRange,this)) {
			chasingPlayer = false;
			chasing.stop();
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
			chasing.loop(Clip.LOOP_CONTINUOUSLY);
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
		
		// Let the player know if Chapman has passed.
		if(Math.abs(Player.getCurrentPlayer().trans.getTranslateX() - this.trans.getTranslateX()) < 5) {
			passed = true;
		}
	}
	
	public void killPlayer() {
		Player.getCurrentPlayer().die();
		groan.start();
		slash.loop(3);
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