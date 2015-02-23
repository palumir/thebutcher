package items;

import java.awt.image.BufferedImage;

import drawables.sprites.SpriteSheet;

public class LanternFuel extends Item {
	
	// Static variables
	private static int amountRefill = 30; // How much fuel do we refill on pickup?
	
	private static SpriteSheet lantern = new SpriteSheet("src/images/items/lantern.png",
			40, 40, 60, 60, 1, 1);
	private static BufferedImage lanternImage = lantern.getSprites()[0];
	
	public LanternFuel(float x, float y) {
			super(lanternImage, 40,60, x, y); // Collision width/height.
			zIndex = 0;
	}
	
	// What happens when we pickup lantern fuel.
	public void pickUp() {
		
		// Set the new fuel. Don't let it go above 100.
		int newFuel = Lantern.getFuel() + amountRefill;
		if(newFuel > 100) newFuel = 100;
		Lantern.setFuel(newFuel);
		
		// Delete the item, of course.
		this.deleteItem();
	}
	
	}