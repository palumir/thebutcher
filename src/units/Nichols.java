package units;

import items.Lantern;

import java.awt.Color;

import main.Main;
import saving.SaveState;
import terrain.SceneMaps.NicholsPit;
import audio.BigClip;
import audio.SoundClip;
import drawables.Background;
import drawables.Canvas;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;

// Nichols is a cunt. He plays random sounds to spook you, and kills you when your
// lantern is done. Not a visible unit.
public class Nichols extends Unit {
	
	public static Nichols nichols;
	
	// How hard are we?
	public static int AILevel = 1;
	
	// Spooky sound for when lantern is off.
	protected static SoundClip violin = new SoundClip("./../sounds/ambience/spooky_violin.wav", false);
	protected static SoundClip stab = new SoundClip("./../sounds/ambience/violin_stab.wav", true);
	protected static SoundClip ghost = new SoundClip("./../sounds/effects/ghost.wav", true);
	protected boolean playing = false;
	
	// Static AI variables
	public static int darknessKillPlayer = 30000 - AILevel*2500;
	public static double lastCheck = 0;
	public static double totalTime = 0;
	public static float howQuicklyDoWeGiveBackTime = 1f; // 1x as fast
	public static double lastEvent = 0;
	public static double randomEventTimer = 10000;
	
	// Have we died? What deathscene.
	private int deathScene = -1; // -1 if not dead.
	private double deathSceneStart = 0;
	
	// Player constructor
	public Nichols() {
		super(20,64,new SpriteSheet("src/images/characters/smith.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		zIndex = 0;
		nichols = this;
		
		// AI Static variables
		darknessKillPlayer = 30000 - AILevel*2500;
		howQuicklyDoWeGiveBackTime = 1/Math.max(1,AILevel);
		
		setHidden(true);
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
			createPitDeathScene();
		}
	}
	
	// Random events. 
	public static void randomEvents() {
			// Only check to make a noise every randomEventTimer (10 default) seconds
			if(Main.getGameTime() - lastEvent > randomEventTimer) {
				lastEvent = Main.getGameTime();
				
				// Chance to make a noise
				if(Main.r.nextInt(100) == 1) {
					randomSound().start();
				}
			}
	}
	
	// Choose a random noice
	public static SoundClip randomSound() {
		return ghost;
	}
	
	// WIP FOREST DEATH SCENE
	public void createPitDeathScene() {
		// Create a character for testing.
		Player player = new Player();
		player.instantlyMove(Canvas.getDefaultWidth()/2,Canvas.getDefaultHeight()/2);
		NicholsPit f = new NicholsPit();
		Lantern l = new Lantern();
		Lantern.setFuelDropping(false);
		Lantern.setFuel(5);
		Lantern.toggleSilent();
		Background.setC(Color.BLACK);
	}
	
	public void runDeathScene() {
		// Kill after random seconds. I'm a fucking asshole.
		if(Main.getGameTime() - deathSceneStart >  (Main.r.nextInt(15)*1000 + 7000)) {
			killPlayer();
		}
	}
	
	public void killPlayer() {
		Player.getCurrentPlayer().die();
		Chapman.groan.start();
		Chapman.slash.loop(3); // WIP IS TABRAM'S DEATH SCREEN
		Background.setBackground(Color.RED);
	}
	
	public void nicholsAI() {
		
		if(deathScene!=-1) {
			runDeathScene();
		}
		
		else {
			// If the lantern is on, don't time.
			if(Lantern.isToggle() && violin.playing) {
				violin.stop();
				lastCheck = 0;
			}
			
			// If the lantern is on, give them their time back.
			else if(Lantern.isToggle()) {
				// Play spooky violin louder and louder
				// -15 => +30
				double percent = totalTime/darknessKillPlayer;
				float val = (float) (21*percent - 15);
				violin.setVolume(val);
				
				// Adjust the timer.
				if(lastCheck==0) lastCheck = Main.getGameTime();
				else {
					totalTime -= Main.getGameTime() - howQuicklyDoWeGiveBackTime*lastCheck;
					if(totalTime<0) totalTime = 0;
					lastCheck = Main.getGameTime();
				}
			}
			
			// If the lantern is off, tick the timer.
			else if(!Lantern.isToggle()) {
				// Play spooky violin louder and louder
				// -15 => +30
				double percent = totalTime/darknessKillPlayer;
				float val = (float) (21*percent - 15);
				violin.setVolume(val);
				if(!violin.playing) violin.loop(BigClip.LOOP_CONTINUOUSLY);
				
				// Adjust the timer.
				if(lastCheck==0) lastCheck = Main.getGameTime();
				else {
					totalTime += Main.getGameTime() - lastCheck;
					lastCheck = Main.getGameTime();
				}
			}
			
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
		Nichols n = new Nichols();
		// WIP DO SOMETHING BEFORE WE TRANSITION TO THE DEATH SCENE
		n.createDeathScene();
		stab.start();
	}
}