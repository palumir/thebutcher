package terrain;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.Main;
import drawables.Canvas;
import drawables.Node;

// One drawable object.
public class Forest extends ArrayList<TerrainChunk> {
	
	Color forestGreen = new Color(72,99,47);
	Color forestBrown = new Color(133,117,91);
	
	// Create the shitty forest.
	public Forest() {
		super();
		TerrainChunk chunk;
		chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, 200, 200),forestGreen);
		chunk.instantlyMove(250, 500);
		this.add(chunk);
		chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, 200, 200),forestBrown);
		chunk.instantlyMove(250, 510);
		this.add(chunk);
		chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, 200, 200),forestGreen);
		chunk.instantlyMove(400, 610);
		this.add(chunk);
		chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, 200, 200),forestBrown);
		chunk.instantlyMove(400, 620);
		this.add(chunk);
	}
	
}



