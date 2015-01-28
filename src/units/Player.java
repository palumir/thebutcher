package units;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import terrain.TerrainChunk;
import drawables.Canvas;
import drawables.Node;
import drawables.Sprite;


// Global player data.
public class Player extends Unit  {
	
	private static Player currentPlayer;	
	
	// Cosmetics
	BufferedImage[] sprites;
	final static int squareWidth = 64; // Width of the square we cut out of the spritesheet
	final static int realWidth = 32; // Width of the character. The square will be much larger.
	final static int height = 64; // Height. Doesn't change.
	final static int rows = 20;
	final static int cols = 13;
	
	// Note: game only current supports one player. If we want multiple, we will need to change
	// movement options and make the currentPlayer a list.
	public Player() {
		super(realWidth,height);
		currentPlayer = this;
		initPlayer();
	}
	
	private void initPlayer() {
		sprites = Sprite.loadSpriteSheet("src/images/player/test_character.png",64,32,64,20,13);
	}
	
	public void paintNode(Graphics2D g2) {
		// Remember the transform being used when called
		AffineTransform t = g2.getTransform();
		// Maintain aspect ratio.
		AffineTransform currentTransform = this.getFullTransform();
		g2.translate(currentTransform.getTranslateX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getTranslateY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
		g2.scale(currentTransform.getScaleX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getScaleY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
		g2.drawImage(sprites[3*cols + 0],-squareWidth/2,-height/2,null);

		// Restore the transform.
		g2.setTransform(t);
	}

	public static void playerGravity() {
		// Accelerate
		if(Player.getCurrentPlayer().getFallSpeed() > Unit.fallSpeedCap) Player.getCurrentPlayer().setFallSpeed(Player.getCurrentPlayer().getFallSpeed() - 0.2f);
		
		// Move everything up!
		Canvas.getGameCanvas().moveAllBut(Player.getCurrentPlayer(), 0, Player.getCurrentPlayer().getFallSpeed());
	}
	
	public static void movePlayer() {
		if(currentPlayer.movingRight) Canvas.getGameCanvas().moveAllBut(currentPlayer, (-1)*currentPlayer.moveSpeed, 0);
		if(currentPlayer.movingLeft) Canvas.getGameCanvas().moveAllBut(currentPlayer, currentPlayer.moveSpeed, 0);
	}
	
	public void moveRight(boolean b) {
		movingRight = b;
	}
	
	public void moveLeft(boolean b) {
		movingLeft = b;
	}
	
	public void jump() {
		
		if(TerrainChunk.touchingTerrain(this,"Down",0,Unit.fallSpeedCap)) { 
			setJumping(true);
			setFallSpeed(5);
		}
	}
	
	public static void keyPressed(KeyEvent k) {
		// Deal with key presses for the player
		if(k.getKeyCode() == KeyEvent.VK_LEFT) Player.getCurrentPlayer().moveLeft(true);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT) Player.getCurrentPlayer().moveRight(true);
		if(k.getKeyCode() == KeyEvent.VK_UP) Player.getCurrentPlayer().jump();
	}
	
	public static void keyReleased(KeyEvent k) {
		// Deal with key release for the player
		if(k.getKeyCode() == KeyEvent.VK_LEFT) Player.getCurrentPlayer().moveLeft(false);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT) Player.getCurrentPlayer().moveRight(false);
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	public static void setCurrentPlayer(Player s) {
		currentPlayer = s;
	}
}
