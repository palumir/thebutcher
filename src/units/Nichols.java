package units;

import java.awt.Color;

import items.Lantern;
import main.Main;
import saving.SaveState;
import terrain.Forest;
import terrain.PurpleHills;
import drawables.Background;
import drawables.Canvas;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;

public class Nichols extends Unit {
	
	public static Nichols nichols;
	
	// How hard are we?
	public static int AILevel = 0;
	
	// Time to kill the player.
	public static int darknessKillPlayer = 5000 - AILevel*1000*2;
	public static double lastCheck = 0;
	public static double totalTime = 0;
	
	
	// Have we died? What deathscene.
	private int deathScene = -1; // -1 if not dead.
	private double deathSceneStart = 0;
	
	// Player constructor
	public Nichols(int difficulty) {
		super(20,64,new SpriteSheet("src/images/characters/smith.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		zIndex = 0;
		nichols = this;
		AILevel = difficulty;
		hidden = true;
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
		this.nicholsAI();
	}
	
	// Create a random death scene
	public void createDeathScene() {
		deathSceneStart = Main.getGameTime();
		deathScene = Main.r.nextInt(2);
		if(deathScene == 1 || deathScene==0) {
			createForestDeathScene();
		}
	}
	
	// WIP FOREST DEATH SCENE
	public void createForestDeathScene() {
		// Create a character for testing.
		Player player = new Player();
		player.instantlyMove(Canvas.getDefaultWidth()/2,Canvas.getDefaultHeight()/2);
		Forest f = new Forest();
		Lantern l = new Lantern();
	}
	
	public void runDeathScene() {
		// Kill after random seconds. I'm a fucking asshole.
		if(Main.getGameTime() - deathSceneStart >  (Main.r.nextInt(15)*1000 + 5000)) {
			killPlayer();
		}
	}
	
	public void killPlayer() {
		Player.getCurrentPlayer().die();
		Tabram.groan.getClip().start();
		Tabram.slash.getClip().loop(3); // WIP IS TABRAM'S DEATH SCREEN
		Background.setBackground(Color.RED);
	}
	
	public void nicholsAI() {
		
		if(deathScene!=-1) {
			runDeathScene();
		}
		
		else {
			// If the lantern is on, don't time.
			if(Lantern.isToggle()) {
				lastCheck = 0;
			}
			
			// If the lantern is off, tick the timer.
			else if(!Lantern.isToggle()) {
				if(lastCheck==0) lastCheck = Main.getGameTime();
				else {
					totalTime += Main.getGameTime() - lastCheck;
					lastCheck = Main.getGameTime();
				}
			}
			System.out.println(totalTime);
			
			// Kill the player if we go over our time limit
			if(totalTime > darknessKillPlayer) {
				eventuallyKillPlayer();
			}
		}
	}
	
	public void eventuallyKillPlayer() {
		
		// Fade out
		SaveState.purgeAll();
		
		// Create nichols so we can run the scene.
		Nichols n = new Nichols(AILevel);
		// WIP DO SOMETHING BEFORE WE TRANSITION TO THE DEATH SCENE
		n.createDeathScene();
	}
}