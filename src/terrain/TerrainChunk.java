package terrain;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import units.Player;
import units.Unit;
import drawables.Canvas;
import drawables.Node;

// One chunk of terrain.
public class TerrainChunk extends Node {

	// All the terrain. EVERYTHING.
	private static ArrayList<ArrayList<TerrainChunk>> terrain = new ArrayList<ArrayList<TerrainChunk>>();
	
	// Cosmetics
	private BufferedImage sprite;
	
	// Does the terrain exist to touch?
	private boolean impassable = true;
	
	// Constructor for the empty chunk.
	
	// Just one chunk of the terrain.
	public TerrainChunk(BufferedImage sp, float x, float y) {
		super(sp.getWidth(), sp.getHeight());
		setSprite(sp);
		this.shapeHidden = true;
		getTerrain().add(add(this);
		this.instantlyMove((x)*getSprite().getWidth(), (y)*getSprite().getHeight());
	}
	
	// Override the paintNode function for Unit.
	public void paintNode(Graphics2D g2) {
		// Only paint it if the player can see it...
		if(Player.getCurrentPlayer().close(Math.max(Canvas.getDefaultHeight()/2 + 200, Canvas.getDefaultWidth()/2 + 200),this)) {
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
	}
	
	// Is the current node "standing" on a terrain chunk?
	public static boolean touchingTerrain(Node n, String direction, float x, float y) {
		for(int i = 0; i < getTerrain().size(); i++) if(getTerrain().get(i).isImpassable() && n.touching(getTerrain().get(i), direction, x, y)) return true;
		return false;
	}
	
	// Is the current node IN terrain? 
	public static boolean inTerrain(Unit u) {
		for(int i = 0; i < getTerrain().size(); i++) {
			TerrainChunk t = getTerrain().get(i);
			float uRadius = (float) Math.max(u.getWidth(), u.getHeight());
			float tRadius = (float) Math.max(t.getWidth(), t.getHeight());
			if(u.close((int) (uRadius + tRadius - 1), t)) return true;
		}
		return false;
	}
	
	// Delete the unit.
	public void deleteChunk() {
		this.deleteNode();
		for (int i = 0; i < terrain.size(); i++) {
			TerrainChunk u = terrain.get(i);
			if(u==this) {
				terrain.remove(i);
				break;
			}
		}
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

	public boolean isImpassable() {
		return impassable;
	}

	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}
}

