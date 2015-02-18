package items;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import units.Unit;
import drawables.Canvas;
import drawables.Node;
import drawables.sprites.SpriteSheet;

// One chunk of terrain.
public class Item extends Node {
	
	// All items
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	// Cosmetics
	private BufferedImage image;
	
	// Just one chunk of the terrain.
	public Item(BufferedImage i, int width, int height) {
		super(width, height);
		image = i;
		this.shapeHidden = true;
		items.add(this);
	}
	
	// Override the paintNode function for Item.
	public void paintNode(Graphics2D g2) {
		if(!isHidden()) {
			// Remember the transform being used when called
			AffineTransform t = g2.getTransform();
			
			// Maintain aspect ratio.
			AffineTransform currentTransform = this.getFullTransform();
			g2.translate(currentTransform.getTranslateX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getTranslateY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
			g2.scale(currentTransform.getScaleX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getScaleY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
			
			// Draw the sprite.
			g2.drawImage(image,0,0,null);
	
			// Restore the transform.
			g2.setTransform(t);
		}
	}
}

