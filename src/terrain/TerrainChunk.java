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
public class TerrainChunk extends Node {

	// All the terrain. EVERYTHING.
	private static ArrayList<TerrainChunk> terrain = new ArrayList<TerrainChunk>();
	
	// Cosmetics
	private BufferedImage sprite;
	
	// Does the terrain exist to touch?
	protected boolean impassable = true;
	
	// Just one chunk of the terrain.
	public TerrainChunk(BufferedImage sp) {
		super(new Rectangle2D.Double(0, 0, sp.getWidth(), sp.getHeight()), Color.BLACK);
		setSprite(sp);
		this.shapeHidden = true;
		getTerrain().add(this);
	}
	
	// Override the paintNode function for Unit.
	public void paintNode(Graphics2D g2) {
		// Remember the transform being used when called
		AffineTransform t = g2.getTransform();
		
		// Maintain aspect ratio.
		AffineTransform currentTransform = this.getFullTransform();
		g2.translate(currentTransform.getTranslateX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getTranslateY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
		g2.scale(currentTransform.getScaleX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getScaleY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
		
		// Draw the sprite.
		g2.drawImage(getSprite(),0,0,null);

		// Restore the transform.
		g2.setTransform(t);
	}
	
	// Is the current node "standing" on a terrain chunk?
	public static boolean touchingTerrain(Node n, String direction, float x, float y) {
		for(int i = 0; i < getTerrain().size(); i++) if(getTerrain().get(i).impassable && n.touching(getTerrain().get(i), direction, x, y)) return true;
		return false;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public static ArrayList<TerrainChunk> getTerrain() {
		return terrain;
	}

	public static void setTerrain(ArrayList<TerrainChunk> terrain) {
		TerrainChunk.terrain = terrain;
	}
}

