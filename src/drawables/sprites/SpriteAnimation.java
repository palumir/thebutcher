package drawables.sprites;

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
		setSpritesInAnimation(n);
		duration = d;
		spriteSetTime = Main.getGameTime();
	}
	
	public void start() {
		setCurSprite(0);
	}
	
	public void loop(boolean b) {
		loop = b;
	}
	
	// Gets the current sprite. Also updates if it's changed.
	public BufferedImage getCurrentSprite() {
		if(getCurSprite() < getSpritesInAnimation().length && spriteSetTime + duration/(long)getSpritesInAnimation().length < Main.getGameTime()) {
			setCurSprite(getCurSprite() + 1);
			spriteSetTime = Main.getGameTime();
		}
		if(getCurSprite() >= getSpritesInAnimation().length && !loop) return spriteSheet.getSprites()[getSpritesInAnimation()[getCurSprite()-1]]; // Return null when we're done
		if(getCurSprite() >= getSpritesInAnimation().length && loop) setCurSprite(0);
		return spriteSheet.getSprites()[getSpritesInAnimation()[getCurSprite()]];
	}

	public int getCurSprite() {
		return curSprite;
	}

	public void setCurSprite(int curSprite) {
		this.curSprite = curSprite;
	}

	public int[] getSpritesInAnimation() {
		return spritesInAnimation;
	}

	public void setSpritesInAnimation(int[] spritesInAnimation) {
		this.spritesInAnimation = spritesInAnimation;
	}
	
}