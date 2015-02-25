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


// Chapman is fairly basic. Turn light off to run by him. Move him off screen, he disappears. Spawns randomly, very fast.
public class Chapman extends Unit {
	
	// Make sure there's only one Chapman at once.
	public static Chapman chapman = null;
	
	// How hard are we?
	public static int AILevel = 1; 
	
	// Static variables
	private static float meanderSpeed = 0.5f;
	private static float chasingSpeed = 0.5f;
	private static float meanderStop = 3000;
	private static int chaseRange = 150;
	private static int closeRange = 230;
	private static int killRange = 30;
	
	// Spawning. This is very random. 
	// Start by checking every spawnCheck seconds, but for every time we don't spawn, reduce spawnCheck by
	// AILevel*3000, lowest possible is minSpawnCheck. Every spawnCheck ms, we have a 1 in spawnChance
	// chance of spawning.
	private static double minSpawnCheck = 1000;
	private static double spawnCheck = 30000;
	private static double defaultSpawnCheck = 30000;
	private static double spawnChance = 10;
	
	// When we he last spawned?
	private static double lastSpawn = 0;
	
	// States
	private boolean closeToPlayer = false;
	private boolean chasingPlayer = false;
	private boolean passed = false; // Has the player actually passed chapman?
	
	// Sounds for Chapman
	protected static SoundClip pulsing = new SoundClip("./../sounds/effects/pulse.wav", true);
	
	// Meandering
	protected static double meanderTime = 0;
	
	// Player constructor
	public Chapman(float x, float y) {
		super(20,64,new SpriteSheet("src/images/characters/Chapman.png",
				59, 69, 84, 86, 4, 4), x, y); // Collision width/height.
		chapman = this;
		zIndex = 0;
		passed = false;
		setAI(AILevel);
		moveSpeed = meanderSpeed;
		
		// Load animations
		loadAnimations();
	}
	
	// Load all of the animations
	void loadAnimations() {
		walkingRight = new SpriteAnimation(spriteSheet, new int[] {
				2 * spriteSheet.getColsInSheet(),
				2 * spriteSheet.getColsInSheet() + 1,
				2 * spriteSheet.getColsInSheet() + 2,
				2 * spriteSheet.getColsInSheet() + 3}, 1000);
		walkingRight.loop(true);
		walkingLeft = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet(),
				1 * spriteSheet.getColsInSheet() + 1,
				1 * spriteSheet.getColsInSheet() + 2,
				1 * spriteSheet.getColsInSheet() + 3}, 1000);
		walkingLeft.loop(true);
		jumpLeft = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet() + 0}, 500);
		jumpLeft.loop(false);
		jumpRight = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet() + 0}, 500);
		jumpRight.loop(false);
		idleRight = new SpriteAnimation(spriteSheet, new int[] {
				2 * spriteSheet.getColsInSheet()}, 500);
		idleRight.loop(true);
		idleLeft = new SpriteAnimation(spriteSheet, new int[] {
				2 * spriteSheet.getColsInSheet()}, 500);
		idleLeft.loop(true);
		animate(jumpRight);
	}
	
	// Obviously, update the unit every frame.
	public void updateUnit() {
		this.AI();
		this.gravity();
		this.move();
	}
	
	// Set AI
	public static void setAI(int i) {
		AILevel = i;
		
		// Configure difficulty
		meanderSpeed = 1.5f + AILevel;
		chasingSpeed = meanderSpeed;
		meanderStop = 3000 - 100*AILevel;
		chaseRange = Math.max(Canvas.getDefaultWidth()/2+100,Canvas.getDefaultHeight()/2+100);
		closeRange = 230;
		killRange = 60;
		minSpawnCheck = 1700;
		defaultSpawnCheck = 29000 - AILevel*2000;
		spawnChance = 11/AILevel + 1; // 1/spawnChance is the spawnChance, every 10 seconds or so.
		spawnCheck = defaultSpawnCheck;
	}
	
	// What to do when Chapman isn't spawned?
	public static void spawnOrDespawn() {
		if(chapman==null) {
			
			// Only check to spawn every 10 seconds
			if(Main.getGameTime() - lastSpawn > Math.max(spawnCheck,minSpawnCheck)) {
				
				// If we haven't spawned him, check to spawn him faster and faster.
				spawnCheck -= AILevel*3000;
				lastSpawn = Main.getGameTime();
				
				// Chance to spawn.
				if(Main.r.nextInt((int)spawnChance) == 1) {
					spawnCheck = defaultSpawnCheck;
					spawn();
				}
			}
		
		}
		
		// Delete Chapman if we move past and he goes off screen, or if we move 3 screens away from him.
		else if((chapman.passed && !chapman.close(Math.max(Canvas.getDefaultWidth()/2, Canvas.getDefaultHeight()/2),Player.getCurrentPlayer())) ||
				!chapman.close(Math.max(4*Canvas.getDefaultWidth()/2, 3*Canvas.getDefaultHeight()/2),Player.getCurrentPlayer())) {
			pulsing.stop();
			chapman.deleteUnit(); 
			chapman = null;
		}
	}
	
	// Spawn chapman
	public static void spawn() {
		Player p = Player.getCurrentPlayer();
		if(p!=null) {
			
			// Is the player moving left or right? Don't spawn Chapman behind the player, silly!
			int leftOrRight = 1;
			if(p.facingLeft) leftOrRight = -1;
			
			Chapman c = new Chapman(0,0);
			
			// Move to off screen. Don't care about terrain at this point
			c.spawnAt(p, (float)(leftOrRight*(Canvas.getDefaultWidth()/2+100)),0);
			if(leftOrRight==1) {
				c.movingLeft = true;
			}
			if(leftOrRight==-1) {
				c.movingRight =true;
			}
		}
	}
	
	public void AI() {
		if(Player.getCurrentPlayer() != null) {
			// STOP STATES
			// If the player has gotten 150 away, stop chasing.
			if(chasingPlayer && !Player.getCurrentPlayer().close(chaseRange,this)) {
				chasingPlayer = false;
				pulsing.stop();
				movingRight = false;
				movingLeft = false;
				moveSpeed = meanderSpeed;
			}
			
			// INITIATE STATES
			// Kill player!
			if(Player.getCurrentPlayer().close(killRange,this)) {
				this.attack(Player.getCurrentPlayer(),5);
			}
			// Chase player!
			else if(!chasingPlayer && Player.getCurrentPlayer().close(chaseRange,this) && Lantern.isToggle()) {
				chasingPlayer = true;
				pulsing.restart();
				pulsing.loop(Clip.LOOP_CONTINUOUSLY);
				moveSpeed = chasingSpeed;
			}
			// Play noise if we're close to player!
			else if(!closeToPlayer && Player.getCurrentPlayer().close(closeRange,this)) {
				closeToPlayer = true;
			}
			
			// STATES
			// Start chasing the player if he's close and has his lantern on.
			meander();
			
			// Don't let him walk into walls.
			if(movingRight && TerrainChunk.touchingTerrain(this, "Right", -this.moveSpeed, 0)) {
				movingLeft = false;
				movingRight = false;
			}
			if(movingLeft && TerrainChunk.touchingTerrain(this, "Left", this.moveSpeed, 0)) {
				movingLeft = false;
				movingRight = false;
			}
			
			// Let the player know if Smith has passed.
			if(Math.abs(Player.getCurrentPlayer().trans.getTranslateX() - this.trans.getTranslateX()) < 5) {
				passed = true;
			}
		}
	}
	
	public void meander() {
		if(TerrainChunk.touchingTerrain(this, "Right", -this.moveSpeed, 0)) {
			movingLeft = true;
			movingRight = false;
		}
		else if(TerrainChunk.touchingTerrain(this, "Left", this.moveSpeed, 0)) {
			movingLeft = false;
			movingRight = true;
		}
	}
}