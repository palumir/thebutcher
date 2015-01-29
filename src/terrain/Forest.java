package terrain;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import main.Main;
import drawables.SpriteSheet;

// One drawable object.
public class Forest extends ArrayList<TerrainChunk> {
	
	// Terrain sprites
	private static BufferedImage grassy;
	private static BufferedImage dirt;
	private static BufferedImage dirtRoof;
	
	int renderX = 0;
	int renderY = 0;
	
	// Create the shitty forest.
	public Forest() {
		super();
		SpriteSheet sheet = new SpriteSheet(
				"src/images/terrain/forest.png", 50, 50, 50, 50, 3, 1);
		grassy = sheet.getSprites()[0];
		dirt = sheet.getSprites()[1];
		dirtRoof = sheet.getSprites()[2];
		genRandomWalkableLandBetween(-500,2000,600,900);
		genRandomWalkableLandBetween(-500,2000,1100,1500);
	}
	
	// Generates land between x and y which is walkable.
	void genRandomWalkableLandBetween(int x1, int x2, int y1, int y2) {
		// Stuff we're going to use throughout.
		Random r = Main.r;
		TerrainChunk chunk = null;
		
		// Some pre-calcs.
		int howManyAcross = Math.abs(x2 - x1)/50;
		int howManyTall = Math.max(Math.abs(y2-y1)/(50*5), r.nextInt(Math.max(Math.abs(y2 - y1)/50 - 2,1)) + 2); 
		int howManyDown = 0;
		
		// Spawn our terrain with our random calculations
		for(int i = 0; i < howManyAcross; i++) {
			for(int j = 0; j < howManyTall; j++) {
				if(j==0) chunk = new TerrainChunk(grassy);
				else if(j==howManyTall-1) chunk = new TerrainChunk(dirtRoof);
				else chunk = new TerrainChunk(dirt);
				chunk.instantlyMove((i+(x1/50))*chunk.getSprite().getWidth(), (j + howManyDown + y1/50)*chunk.getSprite().getHeight());
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



