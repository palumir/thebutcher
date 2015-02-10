package terrain;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import units.Player;
import drawables.Canvas;

// One drawable object.
public class School extends ArrayList<TerrainChunk> {
	
	Color forestGreen = new Color(72,99,47);
	Color forestBrown = new Color(133,117,91);
	
	int renderX = 0;
	int renderY = 0;
	
	// Create the shitty forest.
	public School() {
		super();
		genClassRoom(0,0,200,false,true,false,false);
		genClassRoom(200,0,200,true,true,true,false);
		genClassRoom(275,0,200,true,true,true,false);
		genClassRoom(375,0,200,true,true,false,false);
		genClassRoom(575,0,200,true,true,false,false);
		genClassRoom(775,0,200,true,true,false,false);
		genClassRoom(975,0,200,true,false,false,false);
	}
	
	
	void genClassRoom(int x, int y, int size, boolean doorLeft, boolean doorRight, boolean doorDown, boolean doorUp) {
		TerrainChunk chunk;	
		float spawnHeight = (float) (Canvas.getDefaultHeight()/2 + Player.getCurrentPlayer().getHeight()/2);
		float spawnWidth = Canvas.getDefaultWidth()/2 - size/4;
		
		/*// Floor
		if(doorDown) {}
		else {
			chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, size + 1, size/4),forestBrown);
			chunk.instantlyMove(spawnWidth - size/4 - 1 + x - 2, spawnHeight - 1 + y);
		}
		
		// Left wall
		if(doorLeft) {}
		else {
			chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, size/4 + 2, size/2),forestBrown);
			chunk.instantlyMove(spawnWidth - size/4 - 3 + x, spawnHeight - size/2 + y);
			this.add(chunk);
		}
		
		// Right wall
		if(doorRight) {}
		else {
			chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, size/4 + 1, size/2),forestBrown);
			chunk.instantlyMove(spawnWidth + size/2 - 3 + x, spawnHeight - size/2 + y);
			this.add(chunk);
		}
		
		// Roof
		if(doorUp) {}
		else {
			chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, size + 1, size/4),forestBrown);
			chunk.instantlyMove(spawnWidth - size/4 - 3 + x, spawnHeight - (size - size/4) + 1 + y);
			this.add(chunk);
		}*/
	}
}



