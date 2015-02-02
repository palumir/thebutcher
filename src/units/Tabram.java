package units;

import items.Lantern;

import javax.sound.sampled.Clip;

import terrain.TerrainChunk;
import audio.SoundClip;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;

public class Tabram extends Unit {
	
	public static Tabram tabram = null;
	public static int AILevel = 1; 
	private Unit chasingUnit = null;
	
	// Player constructor
	public Tabram() {
		super(20,64,new SpriteSheet("src/images/characters/tabram.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		tabram = this;
		this.zIndex = 0;
		tabram = this;
		this.moveSpeed = 3 + AILevel/3;
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
		
		// If the player has gotten 150 away, stop chasing.
		if(!Player.getCurrentPlayer().close(150+AILevel*3,this)) {
			chasingUnit = null;
			movingRight = false;
			movingLeft = false;
		}
		
		// Start chasing the player if he's close and has his lantern on.
		if((Player.getCurrentPlayer().close(120 + AILevel*3,this) && Lantern.isToggle())
				|| chasingUnit != null) {
			chasingUnit = Player.getCurrentPlayer();
			follow(chasingUnit);
			chasing.getClip().loop(Clip.LOOP_CONTINUOUSLY);
			close.getClip().stop();
		}
		
		// Play a sound the first time he's close
		else if(Player.getCurrentPlayer().close(300,this)) {
			close.getClip().start(); 
			chasing.getClip().stop();
		}
	}
	
	public void startChase(Unit u) {
		chasingUnit = u;
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