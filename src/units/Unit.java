package units;
import items.Lantern;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.Main;
import terrain.TerrainChunk;
import audio.SoundClip;
import drawables.Background;
import drawables.Canvas;
import drawables.Node;
import drawables.animations.Animation;
import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;

public class Unit extends Node implements MouseListener {
	
	// The list of all units. Fairly important.
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	// Sounds for dying 
	protected static SoundClip slash = new SoundClip("./../sounds/effects/slash.wav", true);
	protected static SoundClip groan = new SoundClip("./../sounds/effects/groan.wav", true);
	
	// The current focused unit
	protected static Unit focusedUnit = null;
	
	// Fall speed of the unit (this is for gravity, obviously)
	private static float defaultFallSpeed = -3;
	protected static float fallSpeedCap = -5;
	protected float fallSpeed = -3;
	protected int wallJumps = 0;
	protected int numWallJumps = 3;
	protected float jumpSpeed = 5.6f;
	
	// Unit stats
	protected int HP = 5;
	protected double attackSpeed = 1000;
	protected double lastAttack = 0;
	
	// Who are we following?
	protected Unit followedUnit = null;
	
	// Store the lantern
	private Lantern lantern = null;
	
	// Dead?
	protected boolean dead = false;
	private boolean deadAnimationPlayed = false;
	
	// Stunned?
	protected boolean stunned = false;
	
	// Sounds for monster interactions.
	protected static SoundClip chasing = new SoundClip("./../sounds/ambience/chasing.wav", true);

	// Unitanimations
	protected SpriteAnimation idleRight;
	protected SpriteAnimation idleLeft;
	protected SpriteAnimation jumpLeft;
	protected SpriteAnimation walkingRight;
	protected SpriteAnimation walkingLeft;
	protected SpriteAnimation jumpRight;
	protected SpriteAnimation deadAnimation;
	
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
	
	public Unit(int width, int height, SpriteSheet ss, float x, float y) {
		// Default unit
		super(width, height, x, y);
		spriteSheet = ss;
		this.shapeHidden = true;
		units.add(this);
	}
	
	// What happens when the unit is on the ground.
	public void onGround() {
		if(hitGround==false && fallSpeed < 0) {
			wallJumps = 0;
			playEffect((int) this.trans.getTranslateX(),(int) this.trans.getTranslateY(),Unit.jumpEffect,500);
			this.setFallSpeed(Unit.defaultFallSpeed);
			hitGround = true;
		}
	}
	
	// Override the paintNode function for Unit.
	public void paintNode(Graphics2D g2) {
		if(!isHidden()) {
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
		
		// Deal with our baddies.
		Nichols.randomEvents();
		Smith.spawnOrDespawn();
		Chapman.spawnOrDespawn();
	}
	
	// Set the lantern
	public void setLantern(Lantern l) {
		this.attach(l);
		lantern = l;
	}
	
	// Attack a unit
	public void attack(Unit u, int damage) {
		if(Main.getGameTime() - lastAttack > attackSpeed) {
			lastAttack = Main.getGameTime();
			slash.start();
			u.hit(damage);
		}
	}
	
	// Kill the player. This obviously loses the game.
	public void die() {
		dead = true;
		stunned = true;
		if(this == Unit.focusedUnit && dead && !deadAnimationPlayed) {
			playDeathAnimation();
		}
	}
	
	// Hit the player for damage
	public void hit(int i) {
		HP -= i;
		if(HP<=0) {
			die();
		}
	}
	
	// Play death animation
	public void playDeathAnimation() {	
		//SaveState.purgeAll(); // Destroy everything on the screen.
		groan.start();
		animate(deadAnimation);
	}
	
	// Unit responding to keypresses
	public static void keyPressed(KeyEvent k) {
		// Deal with key presses for the player
		if(k.getKeyCode() == KeyEvent.VK_TAB) { 
			focusedUnit.moveLeft(false);
			focusedUnit.moveRight(false);
			Player.nextPlayer();
		}
		if(k.getKeyCode() == KeyEvent.VK_F) Player.getCurrentPlayer().followClosestAlly();
		if(k.getKeyCode() == KeyEvent.VK_LEFT || k.getKeyCode() == KeyEvent.VK_A) { 
			focusedUnit.moveLeft(true);
			focusedUnit.moveRight(false);
			focusedUnit.followedUnit = null;
		}
		if(k.getKeyCode() == KeyEvent.VK_RIGHT || k.getKeyCode() == KeyEvent.VK_D) { 
			focusedUnit.moveRight(true);
			focusedUnit.moveLeft(false);
			focusedUnit.followedUnit = null;
		}
		if(k.getKeyCode() == KeyEvent.VK_UP || k.getKeyCode() == KeyEvent.VK_W) { 
			focusedUnit.jump();
			focusedUnit.followedUnit = null;
		}
		if(k.getKeyCode() == KeyEvent.VK_SPACE) ((Lantern) focusedUnit.attachedNode).toggle();
	}
	
	// Unit responding to key releases
	public static void keyReleased(KeyEvent k) {
		// Deal with key release for the player
		if(k.getKeyCode() == KeyEvent.VK_LEFT || k.getKeyCode() == KeyEvent.VK_A) focusedUnit.moveLeft(false);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT || k.getKeyCode() == KeyEvent.VK_D) focusedUnit.moveRight(false);
	}

	// Create an effect somewhere due to a unit interaction.
	public static void playEffect(int x, int y, SpriteSheet s, int duration) {
		Animation a = new Animation(s.getSpriteWidth(),s.getSpriteHeight(), s, duration, x, y);
	}
	
	// Follow a unit
	public void follow(Unit u) {
		float closeEnough = 80;
		if(!this.close((int) closeEnough, u)) {
			if(this.trans.getTranslateX() + this.moveSpeed <= u.trans.getTranslateX()) {
				this.movingRight = true;
				this.movingLeft = false;
				if(TerrainChunk.touchingTerrain(this, "Right", -this.moveSpeed, 0) && followedUnit.getMapY() < this.getMapY()) this.jump();
			}
			if(this.trans.getTranslateX() - this.moveSpeed >= u.trans.getTranslateX()) {
				this.movingRight = false;
				this.movingLeft = true;
				if(TerrainChunk.touchingTerrain(this, "Left", this.moveSpeed, 0) && followedUnit.getMapY() < this.getMapY()) this.jump();
			}
			if(followedUnit.getMapY()+15 < this.getMapY()) {
				jump();
			}
		}
		else {
			this.movingLeft = false;
			this.movingRight = false;
		}
	}

	// Obviously, update the unit every frame.
	public void updateUnit() {
		this.gravity();
		this.move();
	}
	
	// Set the unit to be focused.
	public void focus() {
		if(focusedUnit != null) Canvas.getGameCanvas().moveAllBut(null,(focusedUnit.getMapX() - this.getMapX()), (focusedUnit.getMapY() - this.getMapY()));
		focusedUnit = this;
	}
	
	public void move() {
		if(followedUnit != null) {
			follow(followedUnit);
		}
		if(this != null && !stunned) {
			if(this.movingRight) { 
			    move(this.moveSpeed, 0);
				if(!this.falling()) this.animate(this.walkingRight);
				else this.animate(this.jumpRight);
				this.facingLeft = false;
			}
			else if(this.movingLeft) { 
				move(-this.moveSpeed, 0);
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
	
	// Are touching wall?
	public boolean wallTouching() {
		return TerrainChunk.touchingTerrain(this,"Left",moveSpeed,0) || TerrainChunk.touchingTerrain(this,"Right",-moveSpeed,0);
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
		if(!stunned) {
			if(!falling()) { 
				setFallSpeed(jumpSpeed);
				hitGround = false;
				if(facingLeft) animate(jumpLeft);
				else animate(jumpRight);
			}
			if(wallTouching() && wallJumps != numWallJumps) {
				setFallSpeed(jumpSpeed - jumpSpeed*(wallJumps/numWallJumps));
				hitGround = false;
				if(facingLeft) animate(jumpLeft);
				else animate(jumpRight);
				wallJumps++;
			}
		}
	}
	
	// Do the unit gravity. 
	public void gravity() {
			
			// Accelerate
			if(this.getFallSpeed() > Unit.fallSpeedCap) this.setFallSpeed(this.getFallSpeed() - 0.2f);
			
			// Are we falling?
			if(this.falling()) {
				this.hitGround = false;
			}
			
			// Move everything up!
			move(0, -this.getFallSpeed());
	}
	
	// Move the unit. Move everything if it's the focus.
	public void move(float x, float y) {
		if(this==focusedUnit) {
			Point2D.Float p = Canvas.getGameCanvas().moveAllBut(this, -x, -y);
			setMapX((float) (getMapX() - p.getX()));
			setMapY((float) (getMapY() - p.getY()));
		}
		else {
			Point2D.Float p = Canvas.getGameCanvas().moveUnit(this, x, y);
			setMapX((float) (getMapX() + p.getX()));
			setMapY((float) (getMapY() + p.getY()));
		}
	}
	
	// Animate unit
	public void animate(SpriteAnimation s) {
		currAnimation = s;
	}
	
	// Spawn non-player unit at this spawnX, spawnY
	public void spawnAt(Unit relativeTo, float spawnX, float spawnY) {
		// Move to off screen. Don't care about terrain at this point
		setMapX((float) (relativeTo.getMapX() + spawnX));
		setMapY((float) (relativeTo.getMapY() + spawnY));
		this.instantlyMove((float)relativeTo.trans.getTranslateX() + spawnX,(float)relativeTo.trans.getTranslateY() + spawnY);
			
		// If we're in terrain, then move up until we're not.
		while(TerrainChunk.inTerrain(this)) {
			setMapY((float) (getMapY() - 5));
			this.instantlyMove(0, -5);
		}
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
	
	public void stun(boolean b) {
		movingRight = false;
		movingLeft = false;
		if(facingLeft) currAnimation = idleLeft;
		if(!facingLeft) currAnimation = idleRight;
		stunned = b;
	}

	public Lantern getLantern() {
		return lantern;
	}
}