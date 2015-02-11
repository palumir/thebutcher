package units;

import items.Lantern;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import saving.SaveState;
import drawables.Canvas;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;



// Global player data.
public class Player extends Unit  {
	
	private static Player currentPlayer;	
	
	// Where is the player in the map?
	private float movedX = 0;
	private float movedY = 0;
	
	// Player constructor
	public Player() {
		super(20,64,new SpriteSheet("src/images/player/jack.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		currentPlayer = this;
		moveSpeed = 4;
		this.zIndex = 1;
		setX(Canvas.getDefaultWidth()/2);
		setY(Canvas.getDefaultHeight()/2);
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
	
	// Override the paintNode function for Player.
	public void paintNode(Graphics2D g2) {
		// Remember the transform being used when called
		AffineTransform t = g2.getTransform();
		
		// Maintain aspect ratio.
		AffineTransform currentTransform = this.getFullTransform();
		g2.translate(currentTransform.getTranslateX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getTranslateY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
		g2.scale(currentTransform.getScaleX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getScaleY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
		
		// Draw the sprite.
		g2.drawImage(currAnimation.getCurrentSprite(),-spriteSheet.getSpriteWidth()/2,-spriteSheet.getSpriteHeight()/2,null);

		// Restore the transform.
		g2.setTransform(t);
	}

	// Fall at all times, if possible.
	public void gravity() {
		
		if(Player.getCurrentPlayer() != null ) {
			// Accelerate
			if(Player.getCurrentPlayer().getFallSpeed() > Unit.fallSpeedCap)  { 
				Player.getCurrentPlayer().setFallSpeed(Player.getCurrentPlayer().getFallSpeed() - 0.2f);
			}
			
			// Are we falling?
			if(Player.getCurrentPlayer().falling()) {
				Player.getCurrentPlayer().hitGround = false;
			}
			
			// Move everything up!
			Canvas.getGameCanvas().moveAllBut(Player.getCurrentPlayer(), 0, Player.getCurrentPlayer().getFallSpeed());
		}
	
	}
	
	// Kill the player. This obviously loses the game.
	public void die() {
		SaveState.purgeAll(); // Destroy everything on the screen.
	}
	
	// Always be moving, if the player presses a key.
	public void move() {
		if(this != null) {
			if(this.movingRight) { 
				Canvas.getGameCanvas().moveAllBut(this, (-1)*this.moveSpeed, 0);
				if(!this.falling()) this.animate(this.walkingRight);
				else this.animate(this.jumpRight);
				this.facingLeft = false;
			}
			else if(this.movingLeft) { 
				Canvas.getGameCanvas().moveAllBut(this, this.moveSpeed, 0);
				if(!this.falling()) this.animate(this.walkingLeft);
				else this.animate(this.jumpLeft);
				this.facingLeft = true;
			}
			else {
				if(this.idle() && this.facingLeft) {
					this.animate(this.idleLeft);
				}
				else if(!this.facingLeft && this.idle()) {
					this.animate(this.idleRight);
				}
			}
		}
	}
	
	// Player responding to keypresses
	public static void keyPressed(KeyEvent k) {
		// Deal with key presses for the player
		if(k.getKeyCode() == KeyEvent.VK_LEFT || k.getKeyCode() == KeyEvent.VK_A) Player.getCurrentPlayer().moveLeft(true);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT || k.getKeyCode() == KeyEvent.VK_D) Player.getCurrentPlayer().moveRight(true);
		if(k.getKeyCode() == KeyEvent.VK_UP || k.getKeyCode() == KeyEvent.VK_W) Player.getCurrentPlayer().jump();
		if(k.getKeyCode() == KeyEvent.VK_SPACE) Lantern.toggle();
	}
	
	// Player responding to key releases
	public static void keyReleased(KeyEvent k) {
		// Deal with key release for the player
		if(k.getKeyCode() == KeyEvent.VK_LEFT || k.getKeyCode() == KeyEvent.VK_A) Player.getCurrentPlayer().moveLeft(false);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT || k.getKeyCode() == KeyEvent.VK_D) Player.getCurrentPlayer().moveRight(false);
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	public static void setCurrentPlayer(Player s) {
		currentPlayer = s;
	}
}
