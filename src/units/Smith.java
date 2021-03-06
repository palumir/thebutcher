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


// Smith is fairly basic. Turn light off to run by him. Move him off screen, he disappears. Spawns randomly, very fast.
public class Smith extends Unit {
	
	// Make sure there's only one Smith at once.
	public static Smith Smith = null;
	
	// How hard are we?
	public static int AILevel = 1; 
	
	// Static variables
	private static float meanderSpeed = 1;
	private static float chasingSpeed = 4;
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
	private boolean passed = false; // Has the player actually passed Smith?
	
	// Meandering
	protected static double meanderTime = 0;
	
	// Player constructor
	public Smith(float x, float y) {
		super(20,64,new SpriteSheet("src/images/characters/smith.png",
				64, 20, 64, 64, 20, 13), x, y); // Collision width/height.
		Smith = this;
		zIndex = 0;
		passed = false;
		attackSpeed = 500;
		
		setAI(AILevel);
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
		this.AI();
		this.gravity();
		this.move();
	}
	
	// Set AI
	public static void setAI(int i) {
		AILevel = i;
		
		// Configure difficulty
		meanderSpeed = 1 + (float)AILevel/1.3f;
		chasingSpeed = 4f + (float)AILevel/2.3f;
		meanderStop = 3000 - 100*AILevel;
		chaseRange = 150+AILevel*4;
		closeRange = 230;
		killRange = 30+AILevel*2;
		minSpawnCheck = 2000;
		defaultSpawnCheck = 30000 - AILevel*2000;
		spawnChance = 10/AILevel + 1; // 1/spawnChance is the spawnChance, every 10 seconds or so.
		spawnCheck = defaultSpawnCheck;
	}
	
	// What to do when Smith isn't spawned?
	public static void spawnOrDespawn() {
		if(Smith==null) {
			
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
		
		// Delete Smith if we move past and he goes off screen, or if we move 3 screens away from him.
		else if((Smith.passed && !Smith.close(Math.max(Canvas.getDefaultWidth()/2, Canvas.getDefaultHeight()/2),Player.getCurrentPlayer())) ||
				!Smith.close(Math.max(3*Canvas.getDefaultWidth()/2, 3*Canvas.getDefaultHeight()/2),Player.getCurrentPlayer())) {
			chasing.stop();
			Smith.deleteUnit(); 
			Smith = null;
		}
	}
	
	// Spawn Smith
	public static void spawn() {
		Player p = Player.getCurrentPlayer();
		if(p!=null) {
			
			// Is the player moving left or right? Don't spawn Smith behind the player, silly!
			int leftOrRight = 1;
			if(p.facingLeft) leftOrRight = -1;
			
			// Spawn unit here. This will make sure he's not in terrain.
			Smith c = new Smith(0,0);
			c.spawnAt(p, (float)(leftOrRight*(Canvas.getDefaultWidth()/2+100)),0);
		}
	}
	
	public void AI() {
		if(Player.getClosestPlayer(this) != null) {
			// STOP STATES
			// If the player has gotten 150 away, stop chasing.
			if(Smith == null || (chasingPlayer && !Player.getClosestPlayer(this).close(chaseRange,this))) {
				chasingPlayer = false;
				chasing.stop();
				movingRight = false;
				movingLeft = false;
				moveSpeed = meanderSpeed;
			}
			
			// INITIATE STATES
			// Kill player!
			if(Player.getClosestPlayer(this).close(killRange,this)) {
				this.attack(Player.getClosestPlayer(this),1);
			}
			// Chase player!
			else if(!chasingPlayer && Player.getClosestPlayer(this).close(chaseRange,this) && Player.getCurrentPlayer().getLantern().isToggle()) {
				chasingPlayer = true;
				chasing.restart();
				chasing.loop(Clip.LOOP_CONTINUOUSLY);
				moveSpeed = chasingSpeed;
			}
			// Play noise if we're close to player!
			else if(!closeToPlayer && Player.getClosestPlayer(this).close(closeRange,this)) {
				closeToPlayer = true;
			}
			
			// STATES
			// Start chasing the player if he's close and has his lantern on.
			if(chasingPlayer && !Player.getClosestPlayer(this).dead && this.followedUnit == null) {
				this.followedUnit = Player.getClosestPlayer(this);
			}
			else {
				this.followedUnit = null;
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
			
			// Let the player know if Smith has passed.
			if(Math.abs(Player.getClosestPlayer(this).trans.getTranslateX() - this.trans.getTranslateX()) < 5) {
				passed = true;
			}
		}
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
}