package items;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import main.Main;
import audio.SoundClip;
import drawables.Canvas;
import drawables.Node;


public class Lantern extends Node {
	
	// Sounds
	protected static SoundClip switchSound = new SoundClip("./../sounds/effects/switch.wav", true);
	
	// Levels
	private static int fuel = 50; // 0-100%
	private static int fuelLastsFor = 60000; // milliseconds
	private static double tickTime = 0;
	
	// Strokes for the light
	private static BasicStroke strokeSmall = new BasicStroke(1);
	private static BasicStroke stroke1 = new BasicStroke(150);
	private static BasicStroke stroke2 = new BasicStroke(150);
	private static BasicStroke stroke3 = new BasicStroke(150);
	private static BasicStroke stroke4 = new BasicStroke(150);
	
	private static boolean toggle = true;

	public Lantern() {
		super(new Rectangle2D.Double(0, 0, Canvas.getDefaultWidth(), Canvas.getDefaultHeight()), Color.BLACK);
		this.movesWithPlayer = true;
		this.zIndex = 1;
		FPS = 49;
	}
	
	public static void toggle() {
		if(getFuel()<=0) { // Do we have no fuel left? UH OH.
		}
		else {
			setToggle(!isToggle());
			switchSound.stop();
			switchSound.start();
		}
	}
	
	// Update
	public void update() {
			if(getFuel() > 0) setFuel(getFuel() - 1);
			tickTime = Main.getGameTime();
			
			// If we have no fuel left, turn off.
			if(getFuel()<=0) {
				toggle = false;
			}
			stroke1 = new BasicStroke(700 + getFuel()*2f);
			stroke2 = new BasicStroke(800 + getFuel()*2f);
			stroke3 = new BasicStroke(925 + getFuel()*2f);
			stroke4 = new BasicStroke(1000 + getFuel()*2f);
		System.out.println(fuel); // WIP SHOW SOME INTERFACE FOR FUEL
	}
	
	// Paint the node and it's kids.
	public void paintNode(Graphics2D g2) {
		if(Main.getGameTime() - lastPainted > 1000/FPS) {
			// When was it last painted?
			lastPainted = Main.getGameTime();
			
			// Remember the transform being used when called
			AffineTransform t = g2.getTransform();
			// Maintain aspect ratio.
			AffineTransform currentTransform = this.getFullTransform();
			g2.translate(currentTransform.getTranslateX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getTranslateY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
			g2.scale(currentTransform.getScaleX()*((double)Canvas.getGameCanvas().getWidth()/(double)Canvas.getDefaultWidth()),currentTransform.getScaleY()*((double)Canvas.getGameCanvas().getHeight()/(double)Canvas.getDefaultHeight()));
			float alpha = 0.30f;
			
			// Draw the lantern
			int type = AlphaComposite.SRC_OVER; 
			AlphaComposite composite = AlphaComposite.getInstance(type, alpha);
			Color color = new Color(0, 0, 0, alpha); //Black 
			g2.setPaint(color);
			
			/*if(isToggle()) {
				int fuelConst = getFuel()*2;
				g2.setStroke(stroke1);
				g2.drawOval(fuelConst/2,fuelConst/2,Canvas.getDefaultWidth() - fuelConst, Canvas.getDefaultHeight() - fuelConst);
				g2.setStroke(stroke2);
				g2.drawOval(fuelConst/2,fuelConst/2,Canvas.getDefaultWidth() - fuelConst, Canvas.getDefaultHeight() - fuelConst);
				g2.setStroke(stroke3);
				g2.drawOval(fuelConst/2,fuelConst/2,Canvas.getDefaultWidth() - fuelConst, Canvas.getDefaultHeight() - fuelConst);
				g2.setStroke(stroke4);
				g2.drawOval(fuelConst/2,fuelConst/2,Canvas.getDefaultWidth() - fuelConst, Canvas.getDefaultHeight() - fuelConst);
			}
			else {
				g2.setStroke(strokeSmall);
				g2.fillRect(0, 0, Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
				g2.fillRect(0, 0, Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
				g2.fillRect(0, 0, Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
				g2.fillRect(0, 0, Canvas.getDefaultWidth(), Canvas.getDefaultHeight());
			}*/
			
			// Restore the transform.
			g2.setTransform(t);
		}
	}

	public static boolean isToggle() {
		return toggle;
	}

	public static void setToggle(boolean toggle) {
		Lantern.toggle = toggle;
	}

	public static int getFuel() {
		return fuel;
	}

	public static void setFuel(int fuel) {
		Lantern.fuel = fuel;
	}
	
}