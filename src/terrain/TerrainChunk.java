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
	
	// Just one chunk of the terrain.
	public TerrainChunk(Shape s, Color color) {
		super(s, color);
		terrain.add(this);
	}
	
	// Is the current node "standing" on a terrain chunk?
	public static boolean touchingTerrain(Node n, String direction) {
		for(int i = 0; i < terrain.size(); i++) if(n.touching(terrain.get(i), direction)) return true;
		return false;
	}
}

