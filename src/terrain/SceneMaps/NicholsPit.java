package terrain.SceneMaps;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import main.Main;
import terrain.Doodad;
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
	
	// Doodads
	private static BufferedImage willow;
	
	// Ambience
	private static SoundClip pulse = new SoundClip("./../sounds/ambience/yewbic_ambience.wav", false);
	// https://www.freesound.org/people/xDimebagx/sounds/193692/
	
	int renderX = 0;
	int renderY = 0;
	
	// Create Nicholspit scene
	public NicholsPit() {
		super();
		pulse.loop(BigClip.LOOP_CONTINUOUSLY);
		SpriteSheet sheet = new SpriteSheet(
				"src/images/terrain/forest/forest.png", 50, 50, 50, 50, 3, 1);
		
		// Load terrain
		grassy = sheet.getSprites()[0];
		dirt = sheet.getSprites()[1];
		dirtRoof = sheet.getSprites()[2];
		
		// Load doodads
		SpriteSheet treeSheet = new SpriteSheet(
				"src/images/terrain/forest/doodads/weepingwillow.png", 308, 308, 200, 200, 1, 1);
		willow = treeSheet.getSprites()[0];
		
		genRandomWalkableLandBetween(-5,100,10,100);
	}
	
	// Generates land between x and y which is walkable.
	void genRandomWalkableLandBetween(int x1, int x2, int y1, int y2) {
		// Stuff we're going to use throughout.
		Random r = Main.r;
		TerrainChunk chunk = null;
		
		// Some pre-calcs.
		int howManyAcross = Math.abs(x2 - x1);
		int howManyTall = Math.max(Math.abs(y2-y1), r.nextInt(Math.max(Math.abs(y2 - y1) - 2,1)) + 2); 
		int howManyDown = -2;
		
		// Add the willow.
		Doodad doodad = new Doodad(willow, x1, y1);
		this.add(doodad);
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
				if(j==0) chunk = new TerrainChunk(grassy, i+(x1), (j+howManyDown+y1));
				else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof, i+(x1), (j+howManyDown+y1));
				else chunk = new TerrainChunk(dirt, i+(x1), (j+howManyDown+y1));
				this.add(chunk);
			}
			
		// Build a hill down.
		if(i>3) {
			howManyDown++;
		}
		
		// Build a hill up.
		if(i>15) {
			howManyDown = howManyDown - 2;
		}
		}
	}
}



