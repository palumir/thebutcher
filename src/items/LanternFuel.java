package items;

import java.awt.image.BufferedImage;

import drawables.sprites.SpriteSheet;

public class LanternFuel extends Item {
	
	private static SpriteSheet lantern = new SpriteSheet("src/images/items/lantern.png",
			40, 40, 60, 60, 1, 1);
	private static BufferedImage lanternImage = lantern.getSprites()[0];
	
	public LanternFuel() {
			super(lanternImage, 40,60); // Collision width/height.
			zIndex = 0;
			//currAnimation = spriteSheet.getSprites()[0]; // WIP TOTALLY FUCKED
		}
	}