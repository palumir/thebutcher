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

// This draws ALL lanterns
public class Lantern extends LightSource {
	
	// Sounds
	protected static SoundClip switchSound = new SoundClip("./../sounds/effects/switch.wav", true);
	
	// Levels
	private int fuel = 100; // 0-100%
	private int maxRadius;
	private int fuelLastsFor = 180000; // milliseconds
	private boolean fuelDropping = true;
	private double tickTime = 0;
	
	// Strokes for the light
	private static BasicStroke strokeSmall = new BasicStroke(1);

	public Lantern(float x, float y, int r) {
		super(x, y, r);
		maxRadius = r;
		this.zIndex = 1;
	}
	
	public void toggle() {
		if(getFuel()<=0) { // Do we have no fuel left? UH OH.
		}
		else {
			setToggle(!isToggle());
			switchSound.stop();
			switchSound.start();
		}
	}
	
	public void toggleSilent() {
		if(getFuel()<=0) { // Do we have no fuel left? UH OH.
		}
		else {
			setToggle(!isToggle());
		}
	}
	
	// Update
	public void updateItem() {
		if(fuelDropping) {
			if(toggle && Main.getGameTime() - tickTime > fuelLastsFor/100) {
				if(getFuel() > 0) setFuel(getFuel() - 1);
				tickTime = Main.getGameTime();
				
				radius = (fuel/100)*maxRadius;
				
				// If we have no fuel left, turn off.
				if(getFuel()<=0) {
					toggle = false;
				}
			}
		}
	}
	
	// Paint the node and it's kids.
	public void paintNode(Graphics2D g2) {
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean t) {
		toggle = t;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int f) {
		fuel = fuel;
	}

	public boolean isFuelDropping() {
		return fuelDropping;
	}

	public void setFuelDropping(boolean f) {
		fuelDropping = f;
	}
	
}