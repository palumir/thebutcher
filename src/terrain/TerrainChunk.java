package terrain;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;

import drawables.Node;

// One chunk of terrain.
public class TerrainChunk extends Node {

	// All the terrain. EVERYTHING.
	private static ArrayList<TerrainChunk> terrain = new ArrayList<TerrainChunk>();
	
	// Terrain chunk details
	private boolean passable = false;
	
	// Just one chunk of the terrain.
	public TerrainChunk(Shape s, Color color) {
		super(s, color);
		terrain.add(this);
	}
	
	// Is the current node "standing" on a terrain chunk?
	public static boolean touchingTerrain(Node n, String direction, float x, float y) {
		for(int i = 0; i < terrain.size(); i++) if(!terrain.get(i).passable && n.touching(terrain.get(i), direction, x, y)) return true;
		return false;
	}
	
	public void setPassable(boolean b) {
		passable = b;
	}

	public boolean isPassable() {
		return passable;
	}
}

