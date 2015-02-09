package terrain.Maps;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import terrain.TerrainChunk;
import main.Main;
import audio.BigClip;
import audio.SoundClip;
import drawables.sprites.SpriteSheet;

// One drawable object.
public class Forest extends ArrayList<TerrainChunk> {
	
	// Terrain sprites
	private static BufferedImage grassy;
	private static BufferedImage dirt;
	private static BufferedImage dirtRoof;
	
	// Forest ambience
	private static SoundClip music = new SoundClip("./../sounds/music/dimebag_ambience.wav", false);
	// https://www.freesound.org/people/xDimebagx/sounds/193692/
	
	int renderX = 0;
	int renderY = 0;
	
	// Create the shitty forest.
	public Forest() {
		super();
		music.loop(BigClip.LOOP_CONTINUOUSLY);
		SpriteSheet sheet = new SpriteSheet(
				"src/images/terrain/forest/forest.png", 50, 50, 50, 50, 3, 1);
		grassy = sheet.getSprites()[0];
		dirt = sheet.getSprites()[1];
		dirtRoof = sheet.getSprites()[2];
		genRandomWalkableLandBetween(-5000,5000,600,5000);
	}
	
	// Generates land between x and y which is walkable.
	void genRandomWalkableLandBetween(int x1, int x2, int y1, int y2) {
		// Stuff we're going to use throughout.
		Random r = Main.r;
		TerrainChunk chunk = null;
		
		// Empty chunk, for tunnels
		TerrainChunk emptyChunk = new TerrainChunk(grassy);
		emptyChunk.setImpassable(false);
		
		// Some pre-calcs.
		int howManyAcross = Math.abs(x2 - x1)/50;
		int howManyTall = Math.abs(y2-y1)/(50); 
		int howManyDown = 0;
		int chanceForTunnel = 2;
		
		// Keep a record of where things are.
		TerrainChunk[][] currentTerrain = new TerrainChunk[howManyAcross][howManyTall];
		
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
					if(j==0) chunk = new TerrainChunk(grassy);
					else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof);
					else chunk = new TerrainChunk(dirt);
					chunk.instantlyMove((i+(x1/50))*chunk.getSprite().getWidth(), (j + howManyDown + y1/50)*chunk.getSprite().getHeight());
					this.add(chunk);
					currentTerrain[i][j] = chunk;
			}
			// Move the top up or down. For randomness.
			int bestOf = r.nextInt(9);
			if(bestOf==0) howManyDown += 1; 
			else if(bestOf==1) howManyDown -= 1;
			else  howManyDown += 0;
		}
			
		// Make one tunnel
		int howLong = 100;
		boolean[][] tunneledFrom = new boolean[howManyAcross][howManyTall];
		for(int i=0; i < howManyAcross; i++) for(int j=0; j < howManyTall; j++) tunneledFrom[i][j] = false;
		for(int i = 0; i < howManyAcross; i++) {
			// Do we tunnel?
			if(r.nextInt(chanceForTunnel) == 1) {
				currentTerrain[i][0].deleteChunk();
				currentTerrain[i][0] = emptyChunk;
				int j = 0;
				int length = 0;
				while(length < howLong) {
					int direction = r.nextInt(4); // 0 - left 1 - right 2 - down left 3 - down right
					int howManyInDirection = r.nextInt(howLong - length + 1);
					length++;
				}
			break;
			}
		}
	}
}



