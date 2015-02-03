package units;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import audio.SoundClip;
import terrain.TerrainChunk;
import drawables.Canvas;
import drawables.Node;
import drawables.animations.Animation;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;

public class Unit extends Node implements MouseListener {
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	// Fall speed of the unit (this is for gravity, obviously)
	private static float defaultFallSpeed = -3;
	protected static float fallSpeedCap = -5;
	private float fallSpeed = -3;
	
	// Sounds for monster interactions.
	//protected static SoundClip close = new SoundClip("./../sounds/ambience/violin_stab.wav", true);
	protected static SoundClip chasing = new SoundClip("./../sounds/ambience/chasing.wav", true);

	// Unitanimations
	protected SpriteAnimation idleRight;
	protected SpriteAnimation idleLeft;
	protected SpriteAnimation jumpLeft;
	protected SpriteAnimation walkingRight;
	protected SpriteAnimation walkingLeft;
	protected SpriteAnimation jumpRight;
	
	// Cosmetics
	protected SpriteSheet spriteSheet;
	
	// States
	protected boolean facingLeft = false; // Start facing right
	
	// Current animation
	protected SpriteAnimation currAnimation;
	
	// Movement/Physics
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	protected float moveSpeed = 3;
	protected boolean hitGround = false;
	
	// Unit particle effects
	protected static SpriteSheet jumpEffect = new SpriteSheet("src/images/effects/land.png",64, 64, 64, 64, 1, 5);
	
	public Unit(int width, int height, SpriteSheet ss) {
		// Default unit
		super(new Rectangle2D.Double((-1)*width/2, (-1)*height/2, width, height),Color.RED);
		spriteSheet = ss;
		this.shapeHidden = true;
		units.add(this);
	}
	
	// What happens when the unit is on the ground.
	public void onGround() {
		if(hitGround==false && fallSpeed < 0) {
			playEffect((int) this.trans.getTranslateX(),(int) this.trans.getTranslateY(),Unit.jumpEffect,500);
			this.setFallSpeed(Unit.defaultFallSpeed);
			hitGround = true;
		}
	}
	
	// Override the paintNode function for Unit.
	public void paintNode(Graphics2D g2) {
		if(!hidden) {
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
	}
	
	public static void updateUnits() {
		for(int i=0; i < units.size(); i++) {
			Unit u = units.get(i);
			u.updateUnit();
		}
	}
	
	// Check if this unit is within a radius of u;
	public boolean close(int radius, Unit u) {
		float yDistance = (float) (u.trans.getTranslateY() - this.trans.getTranslateY());
		float xDistance = (float) (u.trans.getTranslateX() - this.trans.getTranslateX());
		float distanceXY = (float) Math.sqrt(yDistance * yDistance
				+ xDistance * xDistance); // It should take this many frames
		return radius > distanceXY;
	}
	
	// Create an effect somewhere due to a unit interaction.
	public static void playEffect(int x, int y, SpriteSheet s, int duration) {
		Animation a = new Animation(s.getSpriteWidth(),s.getSpriteHeight(), s, duration);
		a.instantlyMove(x,y);
	}

	// Obviously, update the unit every frame.
	public void updateUnit() {
		this.gravity();
		this.move();
	}
	
	public void move() {
		if(this != null) {
			if(this.movingRight) { 
				Canvas.getGameCanvas().moveUnit(this, this.moveSpeed, 0);
				if(!this.falling()) this.animate(this.walkingRight);
				else this.animate(this.jumpRight);
				this.facingLeft = false;
			}
			else if(this.movingLeft) { 
				Canvas.getGameCanvas().moveUnit(this, -this.moveSpeed, 0);
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
	
	
	// Are you falling?
	public boolean falling() {
		return !TerrainChunk.touchingTerrain(this,"Down",0,Unit.fallSpeedCap);
	}
	
	// Move right
	public void moveRight(boolean b) {
		movingRight = b;
	}
	
	// Move left
	public void moveLeft(boolean b) {
		movingLeft = b;
	}
	
	// Are you idle?
	public boolean idle() {
		return !movingRight && !movingLeft && !falling() && hitGround;
	}
	
	// Player pressed up key/jump
	public void jump() {
		if(!falling()) { 
			setFallSpeed(5);
			hitGround = false;
			if(facingLeft) animate(jumpLeft);
			else animate(jumpRight);
		}
	}
	
	// Do the unit gravity. Ignore players, of course. 
	public void gravity() {
			// Accelerate
			if(this.getFallSpeed() > Unit.fallSpeedCap) this.setFallSpeed(this.getFallSpeed() - 0.2f);
			
			// Are we falling?
			if(this.falling()) {
				this.hitGround = false;
			}
			
			// Move everything up!
			Canvas.getGameCanvas().moveUnit(this, 0, -this.getFallSpeed());
	}
	
	// Animate unit
	public void animate(SpriteAnimation s) {
		currAnimation = s;
	}
	
	// Delete the unit.
	public void deleteUnit() {
		this.deleteNode();
		for (int i = 0; i < units.size(); i++) {
			Unit u = units.get(i);
			if(u==this) {
				units.remove(i);
				break;
			}
		}
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
}