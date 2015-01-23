package units;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import drawables.Canvas;
import drawables.Node;

public class Unit extends Node implements MouseListener{
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	// Movement/Physics
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	protected int moveSpeed = 3;
	protected boolean jumping = false;
	
	public Unit() {
		// Default unit
		super(new Rectangle2D.Double(-10, -10, 20, 20),Color.RED);
		units.add(this);
	}
	
	public static void updateUnits() {
		Player.playerGravity();
		Player.movePlayer();
	}
	
	public void setMoveSpeed() {
		
	}

}