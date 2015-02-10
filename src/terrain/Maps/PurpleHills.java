package terrain.Maps;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import terrain.Doodad;
import terrain.TerrainChunk;
import audio.BigClip;
import audio.SoundClip;
import main.Main;
import drawables.sprites.SpriteSheet;

// One drawable object.
public class PurpleHills extends ArrayList<TerrainChunk> {
	
	// PH ambience
	private static SoundClip music = new SoundClip("./../sounds/music/dimebag_ambience.wav", false);
	// https://www.freesound.org/people/xDimebagx/sounds/193692/
	
	// Terrain sprites
	private static BufferedImage grassy;
	private static BufferedImage dirt;
	private static BufferedImage dirtRoof;
	
	// Doodad sprites
	private static BufferedImage flower1;
	private static BufferedImage flower2;
	private static BufferedImage flower3;
	
	int renderX = 0;
	int renderY = 0;
	
	// Create the shitty forest.
	public PurpleHills() {
		super();
		music.loop(BigClip.LOOP_CONTINUOUSLY);
		SpriteSheet terrainSheet = new SpriteSheet(
				"src/images/terrain/purplehills/purplehills.png", 50, 50, 50, 50, 3, 1);
		grassy = terrainSheet.getSprites()[0];
		dirt = terrainSheet.getSprites()[1];
		dirtRoof = terrainSheet.getSprites()[2];
		SpriteSheet flowerSheet = new SpriteSheet(
				"src/images/terrain/purplehills/doodads/flowers.png", 123, 123, 180, 180, 1, 3);
		flower1 = flowerSheet.getSprites()[0];
		flower2 = flowerSheet.getSprites()[1];
		flower3 = flowerSheet.getSprites()[2];
		genRandomWalkableLandBetween(-10000,10000,600,900);
	}
	
	// Generates land between x and y which is walkable.
	void genRandomWalkableLandBetween(int x1, int x2, int y1, int y2) {
		// Stuff we're going to use throughout.
		Random r = Main.r;
		TerrainChunk chunk = null;
		Doodad doodad = null;
		
		// Some pre-calcs.
		int howManyAcross = Math.abs(x2 - x1)/50;
		int howManyTall = Math.max(Math.abs(y2-y1)/(50*5), r.nextInt(Math.max(Math.abs(y2 - y1)/50 - 2,1)) + 2); 
		int howManyDown = 0;
		
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
				
				// Decide if we want a grass top, dirt middle, or dirt bottom.
				/*if(j==0) {
					chunk = new TerrainChunk(grassy);
					
					// Maybe even add a random doodad on top?
					if(r.nextInt(16)==1) {
						int pickOne = r.nextInt(3);
						if(pickOne==0) doodad = new Doodad(flower1);
						if(pickOne==1) doodad = new Doodad(flower2);
						if(pickOne==2) doodad = new Doodad(flower3);
						doodad.instantlyMove((i + (x1 / chunk.getSprite()
								.getWidth())) * chunk.getSprite().getWidth() - chunk.getSprite().getWidth()/2, (j
								+ howManyDown + y1 / chunk.getSprite().getHeight())
								* chunk.getSprite().getHeight()
								- doodad.getSprite().getHeight());
						this.add(doodad);
					}
				}
				else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof);
				else chunk = new TerrainChunk(dirt);*/
				chunk.instantlyMove((i+(x1/chunk.getSprite().getWidth()))*chunk.getSprite().getWidth(), (j + howManyDown + y1/chunk.getSprite().getHeight())*chunk.getSprite().getHeight());
				this.add(chunk);
			}
			
		// Move the top up or down. For randomness.
		int bestOf = r.nextInt(9);
		if(bestOf==0) howManyDown += 1; 
		else if(bestOf==1) howManyDown -= 1;
		else  howManyDown += 0;
			
		// Move the bottom up or down. For randomness.
		if(r.nextInt(2) == 1) howManyTall--;
		else howManyTall++;
		howManyTall = Math.min(Math.max(howManyTall,2),Math.abs(y2 - y1)/50);
		}
	}
}



