package units;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import drawables.Canvas;
import drawables.Node;

public class Unit extends Node implements MouseListener{
	
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
	
	public Unit() {
		// Default unit
		super(new Rectangle2D.Double(-10, -10, 20, 20),Color.RED);
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