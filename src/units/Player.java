package units;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import terrain.TerrainChunk;
import drawables.Canvas;
import drawables.SpriteAnimation;
import drawables.SpriteSheet;



// Global player data.
public class Player extends Unit  {
	
	private static Player currentPlayer;	
	
	// Hard define our player animations
	private SpriteAnimation idleRight;
	private SpriteAnimation idleLeft;
	private SpriteAnimation jumpLeft;
	private SpriteAnimation walkingRight;
	private SpriteAnimation walkingLeft;
	private SpriteAnimation jumpRight;
	
	// Player constructor
	public Player() {
		super(20,64,new SpriteSheet("src/images/player/test_character.png",
				64, 20, 64, 64, 20, 13)); // Collision width/height.
		currentPlayer = this;
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
		walkingLeft = new SpriteAnimation(spriteSheet, new int[] {
				9 * spriteSheet.getColsInSheet(),
				9 * spriteSheet.getColsInSheet() + 1,
				9 * spriteSheet.getColsInSheet() + 2,
				9 * spriteSheet.getColsInSheet() + 3,
				9 * spriteSheet.getColsInSheet() + 4,
				9 * spriteSheet.getColsInSheet() + 5,
				9 * spriteSheet.getColsInSheet() + 6}, 500);
		jumpLeft = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet(),
				1 * spriteSheet.getColsInSheet() + 1,
				1 * spriteSheet.getColsInSheet() + 2,
				1 * spriteSheet.getColsInSheet() + 3,
				1 * spriteSheet.getColsInSheet() + 4,
				1 * spriteSheet.getColsInSheet() + 5,
				1 * spriteSheet.getColsInSheet() + 6}, 500);
		jumpRight = new SpriteAnimation(spriteSheet, new int[] {
				3 * spriteSheet.getColsInSheet(),
				3 * spriteSheet.getColsInSheet() + 1,
				3 * spriteSheet.getColsInSheet() + 2,
				3 * spriteSheet.getColsInSheet() + 3,
				3 * spriteSheet.getColsInSheet() + 4,
				3 * spriteSheet.getColsInSheet() + 5,
				3 * spriteSheet.getColsInSheet() + 6}, 500);
		idleRight = new SpriteAnimation(spriteSheet, new int[] {
				3 * spriteSheet.getColsInSheet()}, 500);
		idleLeft = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet()}, 500);
		animate(walkingLeft);
	}
	
	// Animate unit
	public void animate(SpriteAnimation s) {
		currAnimation = s;
		s.loop(true);
	}
	
	// Duh, update the player.
	public static void updatePlayer() {
		Player.playerGravity();
		Player.movePlayer();
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
	public static void playerGravity() {
		// Accelerate
		if(Player.getCurrentPlayer().getFallSpeed() > Unit.fallSpeedCap) Player.getCurrentPlayer().setFallSpeed(Player.getCurrentPlayer().getFallSpeed() - 0.2f);
		
		// Move everything up!
		Canvas.getGameCanvas().moveAllBut(Player.getCurrentPlayer(), 0, Player.getCurrentPlayer().getFallSpeed());
	}
	
	// Always be moving, if the player presses a key.
	public static void movePlayer() {
		if(currentPlayer.movingRight) { 
			Canvas.getGameCanvas().moveAllBut(currentPlayer, (-1)*currentPlayer.moveSpeed, 0);
			if(!currentPlayer.falling()) currentPlayer.animate(currentPlayer.walkingRight);
			else currentPlayer.animate(currentPlayer.jumpRight);
			currentPlayer.facingLeft = false;
		}
		else if(currentPlayer.movingLeft) { 
			Canvas.getGameCanvas().moveAllBut(currentPlayer, currentPlayer.moveSpeed, 0);
			if(!currentPlayer.falling()) currentPlayer.animate(currentPlayer.walkingLeft);
			else currentPlayer.animate(currentPlayer.jumpLeft);
			currentPlayer.facingLeft = true;
		}
		else {
			if(currentPlayer.idle() && currentPlayer.facingLeft) {
				currentPlayer.animate(currentPlayer.idleLeft);
			}
			else if(!currentPlayer.facingLeft && currentPlayer.idle()) {
				currentPlayer.animate(currentPlayer.idleRight);
			}
		}
	}
	
	// Player pressed right key
	public void moveRight(boolean b) {
		movingRight = b;
	}
	
	// Player pressed left key.
	public void moveLeft(boolean b) {
		movingLeft = b;
	}
	
	public boolean idle() {
		return !movingRight && !movingLeft && !falling();
	}
	
	// Are you falling?
	public boolean falling() {
		return !TerrainChunk.touchingTerrain(this,"Down",0,Unit.fallSpeedCap);
	}
	
	// Player pressed up key/jump
	public void jump() {
		if(!falling()) { 
			setJumping(true);
			if(facingLeft) animate(jumpLeft);
			if(!facingLeft) animate(jumpRight);
			setFallSpeed(5);
		}
	}
	
	// Player responding to keypresses
	public static void keyPressed(KeyEvent k) {
		// Deal with key presses for the player
		if(k.getKeyCode() == KeyEvent.VK_LEFT || k.getKeyCode() == KeyEvent.VK_A) Player.getCurrentPlayer().moveLeft(true);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT || k.getKeyCode() == KeyEvent.VK_D) Player.getCurrentPlayer().moveRight(true);
		if(k.getKeyCode() == KeyEvent.VK_UP || k.getKeyCode() == KeyEvent.VK_W|| k.getKeyCode() == KeyEvent.VK_SPACE) Player.getCurrentPlayer().jump();
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
