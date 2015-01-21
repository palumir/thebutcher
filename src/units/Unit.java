package units;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import player.Player;
import drawables.Canvas;
import drawables.Node;

public class Unit extends Node implements MouseListener{
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	
	public Unit() {
		// Default unit
		super(new Rectangle2D.Double(-10, -10, 20, 20),Color.RED);
		units.add(this);
	}
	
	public static void updateUnits() {
		playerGravity();
	}
	
	public static void playerGravity() {
		Canvas.getGameCanvas().moveAllBut(Player.getSelectedUnit(), 0, -2);
	}
}