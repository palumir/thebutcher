package items;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.SoundClip;
import drawables.Canvas;
import drawables.Node;

// This draws ALL LightSources
public class LightSource extends Item {
	
	private static ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
	
	// Sounds
	protected static SoundClip switchSound = new SoundClip("./../sounds/effects/switch.wav", true);
	
	// Levels
	protected int radius;

	// IS it on?
	protected boolean toggle = true;
	
	public LightSource(float x, float y, int r) {
		super(null, r, r , x, y);
		radius = r;
		this.zIndex = 1;
		lightSources.add(this);
	}
	
	public static void drawLighting(Graphics2D g2) {
		
		BufferedImage img = new BufferedImage(Canvas.getDefaultWidth(),Canvas.getDefaultHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		// Draw the darkness
		float alpha = 0.7f;
		Color color = new Color(0, 0, 0, alpha); //Black 
		g.setComposite(AlphaComposite.Src);
		g.setPaint(color);
		g.fillRect(0,0,Canvas.getDefaultWidth(),Canvas.getDefaultHeight());

		g.setComposite(AlphaComposite.Clear); 
		g.setPaint(Color.WHITE);
		for(int i = 0; i < lightSources.size(); i++) {
			LightSource l = lightSources.get(i);
			AffineTransform c = l.getFullTransform();	
			// Clear the circle away
			if(l.toggle) g.fillOval((int)c.getTranslateX() - l.radius/2, (int)c.getTranslateY() - l.radius/2, l.radius, l.radius);
		}
		
		// Draw the newly created image.
		g2.drawImage(img,0,0,null);
	}
	
	// Paint the node and it's kids.
	public void paintNode(Graphics2D g2) {
	}
	
}