package units;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import drawables.Canvas;
import drawables.Node;
import drawables.SpriteAnimation;
import drawables.SpriteSheet;

public class Unit extends Node implements MouseListener {
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	// Fall speed of the unit (this is for gravity, obviously)
	private static float defaultFallSpeed = -1;
	protected static float fallSpeedCap = -5;
	private float fallSpeed = -1;
	
	// Cosmetics
	protected SpriteSheet spriteSheet;
	
	// States
	protected boolean facingLeft = false; // Start facing right
	
	// Current animation
	protected SpriteAnimation currAnimation;
	
	// Movement/Physics
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	protected int moveSpeed = 3;
	private boolean jumping = false;
	
	// Cosmetics
	
	public Unit(int width, int height, SpriteSheet ss) {
		// Default unit
		super(new Rectangle2D.Double((-1)*width/2, (-1)*height/2, width, height),Color.RED);
		spriteSheet = ss;
		this.shapeHidden = true;
		units.add(this);
	}
	
	// Override the paintNode function for Unit.
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
	
	public static void updateUnits() {
		Player.updatePlayer();
	}

	public float getFallSpeed() {
		return fallSpeed;
	}

	public void setFallSpeed(float fallSpeed) {
		this.fallSpeed = fallSpeed;
	}

	public static float getDefaultFallSpeed() {
		return defaultFallSpeed;
	}

	public static void setDefaultFallSpeed(float defaultFallSpeed) {
		Unit.defaultFallSpeed = defaultFallSpeed;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
}