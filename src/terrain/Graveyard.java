package terrain;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.Main;
import units.Player;
import drawables.Canvas;

// One drawable object.
public class Graveyard extends ArrayList<TerrainChunk> {
	
	Color deathlyGrey = new Color(94,87,105);
	Color deathlyGreen = new Color(98,105,87);
	
	int renderX = 0;
	int renderY = 0;
	
	// Create the shitty forest.
	public Graveyard() {
		super();
		TerrainChunk chunk;
		chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, Canvas.getGameCanvas().getDefaultWidth()*5, Canvas.getGameCanvas().getDefaultHeight()/2),deathlyGrey);
		chunk.instantlyMove(-Canvas.getGameCanvas().getDefaultWidth()*5/2, 500);
		this.add(chunk);
		chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, Canvas.getGameCanvas().getDefaultWidth()*5, 5),deathlyGreen);
		chunk.instantlyMove(-Canvas.getGameCanvas().getDefaultWidth()*5/2, 500);
		this.add(chunk);
	}
	
}



