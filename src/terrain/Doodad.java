package terrain;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import drawables.Canvas;
import drawables.Node;

// One chunk of terrain.
public class Doodad extends TerrainChunk {
	
	// Just one chunk of the terrain.
	public Doodad(BufferedImage sp, float x, float y) {
		super(sp, x, y);
		setImpassable(false);
	}
}

