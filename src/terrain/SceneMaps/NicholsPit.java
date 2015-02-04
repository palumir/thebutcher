package terrain.SceneMaps;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import main.Main;
import terrain.TerrainChunk;
import audio.BigClip;
import audio.SoundClip;
import drawables.Canvas;
import drawables.sprites.SpriteSheet;

// One drawable object.
public class NicholsPit extends ArrayList<TerrainChunk> {
	
	// Terrain sprites
	private static BufferedImage grassy;
	private static BufferedImage dirt;
	private static BufferedImage dirtRoof;
	
	// Ambience
	private static SoundClip pulse = new SoundClip("./../sounds/ambience/yewbic_ambience.wav", false);
	// https://www.freesound.org/people/xDimebagx/sounds/193692/
	
	int renderX = 0;
	int renderY = 0;
	
	// Create tabrampit scene
	public NicholsPit() {
		super();
		pulse.loop(BigClip.LOOP_CONTINUOUSLY);
		//music.getBigClip().loop(BigClip.LOOP_CONTINUOUSLY);
		SpriteSheet sheet = new SpriteSheet(
				"src/images/terrain/forest/forest.png", 50, 50, 50, 50, 3, 1);
		grassy = sheet.getSprites()[0];
		dirt = sheet.getSprites()[1];
		dirtRoof = sheet.getSprites()[2];
		genRandomWalkableLandBetween(-200,500,Canvas.getDefaultHeight()/2 + 50,900);
	}
	
	// Generates land between x and y which is walkable.
	void genRandomWalkableLandBetween(int x1, int x2, int y1, int y2) {
		// Stuff we're going to use throughout.
		Random r = Main.r;
		TerrainChunk chunk = null;
		
		// Some pre-calcs.
		int howManyAcross = Math.abs(x2 - x1)/50;
		int howManyTall = Math.max(Math.abs(y2-y1)/(50*5), r.nextInt(Math.max(Math.abs(y2 - y1)/50 - 2,1)) + 2); 
		
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
				if(j==0) chunk = new TerrainChunk(grassy);
				else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof);
				else chunk = new TerrainChunk(dirt);
				chunk.instantlyMove((i+(x1/50))*chunk.getSprite().getWidth(), (j+ y1/50)*chunk.getSprite().getHeight());
				this.add(chunk);
			}
			
		// Move the bottom up or down. For randomness.
		if(r.nextInt(2) == 1) howManyTall--;
		else howManyTall++;
		howManyTall = Math.min(Math.max(howManyTall,2),Math.abs(y2 - y1)/50);
		}
	}
}



