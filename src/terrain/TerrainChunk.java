package terrain;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import units.Player;
import units.Unit;
import drawables.Canvas;
import drawables.Node;

// One chunk of terrain.
public class TerrainChunk extends Node {

	// All the terrain. EVERYTHING.
	private static Map<String,TerrainChunk> terrain = new HashMap<String,TerrainChunk>();
	private String mapPos;
	
	// Default stuff
	private static int defaultBlockSize = 50; // 50by50
	
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
		terrain.put((int)x + "x" + (int)y,this);
		this.instantlyMove((int)(x)*getSprite().getWidth(), (int)(y)*getSprite().getHeight());
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
	public static boolean touchingTerrain(Unit n, String direction, float x, float y) {
		
		// Check 10 blocks around the unit for "touching". General rule.
		for(int i = -5; i < 5; i++) {
			for(int j = -5; j < 5; j++) {
				String whatPos =(int) ((n.getX()/defaultBlockSize) + i) + "x" + (int) (n.getY()/defaultBlockSize + j);
				if(getTerrain().get(whatPos) == null) {
				}
				else {
					if(getTerrain().get(whatPos).isImpassable() && n.touching(getTerrain().get(whatPos), direction, x, y)) return true;
				}
			}
		}
		return false;
	}
	
	// Is the current node IN terrain? 
	public static boolean inTerrain(Unit u) {
		
		// Check 10 blocks around the unit for "inside". General rule.
		for(int i = -5; i < 5; i++) {
			for(int j = -5; j < 5; j++) {
				String whatPos =(int) ((u.getX()/defaultBlockSize) + i) + "x" + (int) (u.getY()/defaultBlockSize + j);
				TerrainChunk t = getTerrain().get(whatPos);
				float uRadius = (float) Math.max(u.getWidth(), u.getHeight());
				if(t!=null) { 
					float tRadius = (float) Math.max(t.getWidth(), t.getHeight());
					if(u.close((int) (uRadius + tRadius - 1), t)) return true;
				}
			}
		}
		return false;
	}
	
	// Delete the unit.
	public void deleteChunk() {
		this.deleteNode();
		terrain.remove(mapPos);
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public static Map<String,TerrainChunk> getTerrain() {
		return terrain;
	}

	public static void setTerrain(Map<String,TerrainChunk> terrain) {
		TerrainChunk.terrain = terrain;
	}

	public boolean isImpassable() {
		return impassable;
	}

	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}

	public static int getDefaultBlockSize() {
		return defaultBlockSize;
	}
}

