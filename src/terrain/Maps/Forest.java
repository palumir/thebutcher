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
		genRandomWalkableLandBetween(-200,200,10,100);
	}
	
	// Generates land between x and y which is walkable.
	void genRandomWalkableLandBetween(int x1, int x2, int y1, int y2) {
		// Stuff we're going to use throughout.
		Random r = Main.r;
		TerrainChunk chunk = null;
		
		// Empty chunk, for tunnels
		TerrainChunk emptyChunk = new TerrainChunk(grassy,0,0);
		emptyChunk.setImpassable(false);
		
		// Some pre-calcs.
		int howManyAcross = Math.abs(x2 - x1);
		int howManyTall = Math.abs(y2-y1); 
		int howManyDown = 0;
		int chanceForTunnel = 20;
		
		// Keep a record of where things are.
		TerrainChunk[][] currentTerrain = new TerrainChunk[howManyAcross][howManyTall];
		
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
					if(j==0) chunk = new TerrainChunk(grassy, (i+(x1)), (j + howManyDown + y1));
					else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof, (i+(x1)), (j + howManyDown + y1));
					else chunk = new TerrainChunk(dirt, (i+(x1)), (j + howManyDown + y1));
					this.add(chunk);
					currentTerrain[i][j] = chunk;
			}
		}
			
		// Make one tunnel
		int howLong = 100;
		boolean[][] tunneledFrom = new boolean[howManyAcross][howManyTall];
		for(int i=0; i < howManyAcross; i++) for(int j=0; j < howManyTall; j++) tunneledFrom[i][j] = false;
		for(int x = 0; x < howManyAcross; x++) {
			// Do we tunnel?
			if(r.nextInt(chanceForTunnel) == 1) {
				currentTerrain[x][0].deleteChunk();
				currentTerrain[x][0] = emptyChunk;
				int j = 0;
				int i = x;
				int length = 0;
				while(length < howLong) {
					int direction = r.nextInt(4); // 0 - left 1 - right 2 - down left 3 - down right
					
					// Left or right go as far as you want.
					int howManyInDirection = Math.max(5,Math.min(r.nextInt(howLong),howLong/3));
					
					// Down left/Down Right only go up to a certain point
					if(direction == 3 || direction == 2) {
						howManyInDirection = Math.max(3, r.nextInt(6));
					}
					
					// Don't allow the grass to get owned.
					if((j == 0 || j == 1) && (direction == 1 || direction == 0)) {
						howManyInDirection = 0;
					}
					
					// Put down the blocks
					for(int m = 0; m < howManyInDirection; m++) {
						if(j+1 < howManyTall && i-1 >=0 && i + 1 < howManyAcross) {
							if(direction==0) {
								i--;
								currentTerrain[i][j].deleteChunk();
								currentTerrain[i][j] = emptyChunk;
								currentTerrain[i][j+1].deleteChunk();
								currentTerrain[i][j+1] = emptyChunk;
							}
							if(direction==1) {
								i++;
								currentTerrain[i][j].deleteChunk();
								currentTerrain[i][j] = emptyChunk;
								currentTerrain[i][j+1].deleteChunk();
								currentTerrain[i][j+1] = emptyChunk;
							}
							if(direction==2) {
								j++;
								currentTerrain[i][j].deleteChunk();
								currentTerrain[i][j] = emptyChunk;
								i++;
								currentTerrain[i][j].deleteChunk();
								currentTerrain[i][j] = emptyChunk;
							}
							if(direction==3) {
								j++;
								currentTerrain[i][j].deleteChunk();
								currentTerrain[i][j] = emptyChunk;
								i--;
								currentTerrain[i][j].deleteChunk();
								currentTerrain[i][j] = emptyChunk;
							}
							length++;
						}
						else { length = howLong; break; }
					}	
				}
			}
		}
	}
}



