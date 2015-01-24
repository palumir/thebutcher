package units;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import player.Player;
import drawables.Canvas;
import drawables.Node;

public class Unit extends Node {
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	// Fall speed of the unit (this is for gravity, obviously)
	private static float defaultFallSpeed = -3;
	private static float fallSpeedCap = -10;
	private float fallSpeed = -1;
	
	public Unit() {
		// Default unit
		super(new Rectangle2D.Double(-10, -10, 20, 20),Color.RED);
		units.add(this);
	}
	
	public static void updateUnits() {
		playerGravity();
	}
	
	public static void playerGravity() {
		// Accelerate
		if(Player.getSelectedUnit().getFallSpeed() > fallSpeedCap) Player.getSelectedUnit().setFallSpeed(Player.getSelectedUnit().getFallSpeed() - 0.05f);
		
		// Move everything up!
		Canvas.getGameCanvas().moveAllBut(Player.getSelectedUnit(), 0, Player.getSelectedUnit().getFallSpeed());
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