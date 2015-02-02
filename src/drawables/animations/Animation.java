package drawables.animations;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;
import units.Unit;


public class Animation extends Unit {
	public Animation(int width, int height, SpriteSheet ss, int duration) {
		// Default unit
		super(width, height, ss);
		spriteSheet = ss;
		int[] iA = new int[spriteSheet.getColsInSheet()];
		for(int i = 0; i < iA.length; i++) {
			iA[i] = i;
		}
		zIndex = 1;
		currAnimation = new SpriteAnimation(spriteSheet, iA, duration);
	}
	
	public void updateUnit() {
		if(currAnimation.getCurSprite() >= currAnimation.getSpritesInAnimation().length) { 
			this.deleteUnit();
		}
	}
	
}