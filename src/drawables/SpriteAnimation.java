package drawables;

import java.awt.image.BufferedImage;

import main.Main;

public class SpriteAnimation {
	
	// What are we animating, and for how long?
	private SpriteSheet spriteSheet;
	private boolean loop = false;
	private int[] spritesInAnimation;
	private long spriteSetTime;
	private int curSprite = 0;
	private long duration; // Duration in milliseconds
	
	public SpriteAnimation(SpriteSheet s, int[] n, int d) {
		spriteSheet = s;
		spritesInAnimation = n;
		duration = d;
		spriteSetTime = Main.getGameTime();
	}
	
	public void loop(boolean b) {
		loop = true;
	}
	
	// Gets the current sprite. Also updates if it's changed.
	public BufferedImage getCurrentSprite() {
		if(spriteSetTime + duration/(long)spritesInAnimation.length < Main.getGameTime()) {
			curSprite++;
			spriteSetTime = Main.getGameTime();
		}
		if(curSprite >= spritesInAnimation.length && !loop) return null; // Return null when we're done
		if(curSprite >= spritesInAnimation.length && loop) curSprite = 0;
		return spriteSheet.getSprites()[spritesInAnimation[curSprite]];
	}
	
}