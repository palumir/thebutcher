package units;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import drawables.Node;

public class Unit extends Node implements MouseListener {
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	// Fall speed of the unit (this is for gravity, obviously)
	private static float defaultFallSpeed = -1;
	protected static float fallSpeedCap = -3;
	private float fallSpeed = -1;
	
	// Movement/Physics
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	protected int moveSpeed = 3;
	private boolean jumping = false;
	
	// Cosmetics
	
	public Unit(int width, int height) {
		// Default unit
		super(new Rectangle2D.Double((-1)*width/2, (-1)*height/2, width, height),Color.RED);
		this.shapeHidden = true;
		units.add(this);
	}
	
	public static void updateUnits() {
		Player.playerGravity();
		Player.movePlayer();
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