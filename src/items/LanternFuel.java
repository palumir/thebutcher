package items;

import units.Unit;
import drawables.sprites.SpriteSheet;

public class LanternFuel extends Unit {
	public LanternFuel() {
			super(40,60,new SpriteSheet("src/images/items/lantern.png",
					40, 40, 60, 60, 1, 1)); // Collision width/height.
			zIndex = 0;
			//currAnimation = spriteSheet.getSprites()[0]; // WIP TOTALLY FUCKED
		}
	}