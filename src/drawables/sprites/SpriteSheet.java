package drawables.sprites;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SpriteSheet {
	// The actual spritesheet
	private BufferedImage[] sprites; // To access a sprite in row i and col j, use sprites[i*rowsInSheet + j]
	
	// Spritesheet specific information
	private int spriteWidth;
	private int collisionWidth;
	private int spriteHeight;
	private int collisionHeight;
	private int rowsInSheet;
	private int colsInSheet;
	
	public SpriteSheet(String file, int squareWidth, int realWidth, int squareHeight, int realHeight, int rows, int cols) {
		// Load our spritesheet from file into sprites.
		// squareWidth is the width of the sprite box
		// realWidth is the real width of the sprite for collison, since the box is probably bigger.
		// squareHeight is the height of the sprite for the sprite box.
		// realHeight is the height of the sprite for collision
		// rows is the number of rows in the spritesheet
		// cols is the number of columns in the spritesheet
		setSpriteWidth(squareWidth);
		collisionWidth = realWidth;
		setSpriteHeight(squareHeight);
		collisionHeight = realHeight;
		rowsInSheet = rows;
		setColsInSheet(cols);
		
		try {
			BufferedImage bigImg = ImageIO.read(new File(file));
			BufferedImage[] newSprites = new BufferedImage[rows*cols];
			for (int i = 0; i < rows; i++)
			{
			    for (int j = 0; j < cols; j++)
			    {
			        newSprites[(i * cols) + j] = bigImg.getSubimage(
			            j * squareWidth,
			            i * squareHeight,
			            squareWidth,
			            squareHeight
			        );
			    }
			}
			setSprites(newSprites);
		}
		catch(Exception e) { e.printStackTrace(); }
	}

	public BufferedImage[] getSprites() {
		return sprites;
	}

	public void setSprites(BufferedImage[] sprites) {
		this.sprites = sprites;
	}

	public int getColsInSheet() {
		return colsInSheet;
	}

	public void setColsInSheet(int colsInSheet) {
		this.colsInSheet = colsInSheet;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
}