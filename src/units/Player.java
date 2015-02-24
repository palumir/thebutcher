package units;

import java.util.ArrayList;

import drawables.sprites.SpriteAnimation;
import drawables.sprites.SpriteSheet;



// Global player data.
public class Player extends Unit  {
	
	// Static stuff
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static int currPlayer = 0;
	
	// Player constructor
	public Player(float x, float y) {
		super(20,64,new SpriteSheet("src/images/player/jack.png",
				64, 20, 64, 64, 21, 13), x, y); // Collision width/height.
		players.add(this);
		moveSpeed = 4;
		this.zIndex = 1;
		loadAnimations();
	}

	// Load all of the player's animations
	void loadAnimations() {
		walkingRight = new SpriteAnimation(spriteSheet, new int[] {
				11 * spriteSheet.getColsInSheet(),
				11 * spriteSheet.getColsInSheet() + 1,
				11 * spriteSheet.getColsInSheet() + 2,
				11 * spriteSheet.getColsInSheet() + 3,
				11 * spriteSheet.getColsInSheet() + 4,
				11 * spriteSheet.getColsInSheet() + 5,
				11 * spriteSheet.getColsInSheet() + 6}, 500);
		walkingRight.loop(true);
		walkingLeft = new SpriteAnimation(spriteSheet, new int[] {
				9 * spriteSheet.getColsInSheet(),
				9 * spriteSheet.getColsInSheet() + 1,
				9 * spriteSheet.getColsInSheet() + 2,
				9 * spriteSheet.getColsInSheet() + 3,
				9 * spriteSheet.getColsInSheet() + 4,
				9 * spriteSheet.getColsInSheet() + 5,
				9 * spriteSheet.getColsInSheet() + 6}, 500);
		walkingLeft.loop(true);
		jumpLeft = new SpriteAnimation(spriteSheet, new int[] {
				1 * spriteSheet.getColsInSheet() + 6}, 500);
		jumpLeft.loop(false);
		jumpRight = new SpriteAnimation(spriteSheet, new int[] {
				3 * spriteSheet.getColsInSheet() + 6}, 500);
		jumpRight.loop(false);
		idleRight = new SpriteAnimation(spriteSheet, new int[] {
				7 * spriteSheet.getColsInSheet()}, 500);
		idleRight.loop(true);
		idleLeft = new SpriteAnimation(spriteSheet, new int[] {
				9 * spriteSheet.getColsInSheet()}, 500);
		idleLeft.loop(true);
		deadAnimation = new SpriteAnimation(spriteSheet, new int[] {
				20 * spriteSheet.getColsInSheet(),
				20 * spriteSheet.getColsInSheet()+1,
				20 * spriteSheet.getColsInSheet()+2,
				20 * spriteSheet.getColsInSheet()+3,
				20 * spriteSheet.getColsInSheet()+4}, 500);
		deadAnimation.loop(false);
		animate(jumpRight);
	}
	
	// Follow closest friend.
	public void followClosestAlly() {
		followedUnit = null;
		float smallestDiff = 1000;
		float dontFollowIfThisFarAway = 300;
		for(int i = 0; i < players.size(); i++) {
			float diffX = this.getMapX() - players.get(i).getMapX();
			float diffY = this.getMapY() - players.get(i).getMapY();
			float distance = (float) Math.sqrt(diffX*diffX + diffY*diffY);
			if(this != players.get(i) && smallestDiff > distance && distance < dontFollowIfThisFarAway) {
				followedUnit = players.get(i);
				smallestDiff = distance;
			}
		}
	}
	
	// Get the next player (tab through for now)
	public static void nextPlayer() {
		currPlayer++;
		if(currPlayer>=players.size()) {
			currPlayer = 0;
		}
		players.get(currPlayer).focus();
	}
	
	public static Player getCurrentPlayer() {
		if(players.contains(Unit.focusedUnit)) {
			return (Player)focusedUnit;
		}
		return null;
	}
}
