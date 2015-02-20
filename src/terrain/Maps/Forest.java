package terrain.Maps;
import items.LanternFuel;

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
public class Forest extends ArrayList<TerrainChunk> {
	
	// Terrain sprites
	private static BufferedImage grassy;
	private static BufferedImage dirt;
	private static BufferedImage dirtRoof;
	
	// Forest ambience
	private static SoundClip music = new SoundClip("./../sounds/ambience/klankbleed.wav", false); // Sound from http://www.freesound.org/people/klankbeeld/
	// https://www.freesound.org/people/xDimebagx/sounds/193692/
	
	// Doodad sprites
	private static BufferedImage flower1;
	private static BufferedImage flower2;
	private static BufferedImage flower3;
	
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
		SpriteSheet flowerSheet = new SpriteSheet(
				"src/images/terrain/purplehills/doodads/flowers.png", 123, 123, 180, 180, 1, 3);
		flower1 = flowerSheet.getSprites()[0];
		flower2 = flowerSheet.getSprites()[1];
		flower3 = flowerSheet.getSprites()[2];
		genRandomWalkableLandBetween(-200,100,10,100);
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
		int chanceForTunnel = 30;
		
		// Keep a record of where things are.
		TerrainChunk[][] currentTerrain = new TerrainChunk[howManyAcross][howManyTall];
		
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
					if(j==0)  { 
						chunk = new TerrainChunk(grassy, (i+(x1)), (j + y1));
						if(i%20 == 0 && r.nextInt(10) == 1) {
							LanternFuel lf = new LanternFuel();
							lf.instantlyMove((float)chunk.trans.getTranslateX(),(float)chunk.trans.getTranslateY()- 35);
						}
					}
					else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof, (i+(x1)), (j + y1));
					else chunk = new TerrainChunk(dirt, (i+(x1)), (j + y1));
					this.add(chunk);
					currentTerrain[i][j] = chunk;
			}
		}
			
		// Make tennels
		int howLong = 400;
		boolean[][] tunneledFrom = new boolean[howManyAcross][howManyTall];
		for(int i=0; i < howManyAcross; i++) for(int j=0; j < howManyTall; j++) tunneledFrom[i][j] = false;
		for(int x = 0; x < howManyAcross; x++) {
			// Do we tunnel?
			if(r.nextInt(chanceForTunnel) == 1) {
				TerrainChunk oldChunk = currentTerrain[x][0];
				currentTerrain[x][0].deleteChunk();
				currentTerrain[x][0] = emptyChunk;
				int j = 0;
				int i = x;
				int length = 0;
				while(length < howLong) {
					int direction = r.nextInt(4); // 0 - left 1 - right 2 - down left 3 - down right
					
					if( r.nextInt(2) == 1) {
						LanternFuel lf = new LanternFuel();
						lf.instantlyMove((float)oldChunk.trans.getTranslateX(),(float)oldChunk.trans.getTranslateY()-35);
					}
					
					// Left or right go as far as you want.
					int howManyInDirection = Math.max(5,Math.min(r.nextInt(howLong),10));
					
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
		// Spawn our terrain with our random calculations
	/*	for(int i = 0; i < howManyAcross; i++) { 
			// Maybe even add a random doodad on top?
			if(r.nextInt(2)==1 && currentTerrain[i][0] != emptyChunk) {
				int pickOne = r.nextInt(3);
				Doodad doodad = null;
				if(pickOne==0) doodad = new Doodad(flower1, i + x1 - flower1.getWidth()/TerrainChunk.getDefaultBlockSize(), y1 - flower1.getHeight()/TerrainChunk.getDefaultBlockSize());
				if(pickOne==1) doodad = new Doodad(flower2, i + x1 - flower2.getWidth()/TerrainChunk.getDefaultBlockSize(), y1 - flower2.getHeight()/TerrainChunk.getDefaultBlockSize());
				if(pickOne==2) doodad = new Doodad(flower3, i + x1 - flower3.getWidth()/TerrainChunk.getDefaultBlockSize(), y1 - flower3.getHeight()/TerrainChunk.getDefaultBlockSize());
				this.add(doodad);
			}
		}*/
	}
}




