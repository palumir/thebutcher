package terrain;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import drawables.SpriteSheet;
import main.Main;

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
		genNoobIsland();
	}
	
	
	// Generate nooby fucking island
	void genNoobIsland() {
		TerrainChunk chunk;	
		for(int j = 0; j < Main.r.nextInt(500); j++) {
			for(int i = 0; i < Main.r.nextInt(500); i++) {
				int rand1 = Main.r.nextInt(200);
				int rand2 = Main.r.nextInt(200);
				chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, 200, 100 + Main.r.nextInt(100)),forestBrown);
				chunk.instantlyMove(250 + i*199 + rand2, j*500 + rand1);
				this.add(chunk);
				chunk = new TerrainChunk(new Rectangle2D.Double(0, 0, 200, 5),forestGreen);
				chunk.instantlyMove(250 + i*199 + rand2, j*500 + rand1);
				this.add(chunk);
			}
		}
	}
}



