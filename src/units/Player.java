package units;

import java.awt.event.KeyEvent;

import terrain.TerrainChunk;
import drawables.Canvas;


// Global player data.
public class Player extends Unit  {
	
	private static Player currentPlayer;	
	
	// Note: game only current supports one player. If we want multiple, we will need to change
	// movement options and make the currentPlayer a list.
	public Player() {
		super();
		currentPlayer = this;
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
