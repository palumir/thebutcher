package drawables;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Sprite {
	
	// squareWidth - Width of the square we cut out of the spritesheet
	// realWidth - Width of the character. The square will be much larger.
	// height - Height. Doesn't change.
	// rows - Rows in sprite sheet
	// cols - Cols in sprite sheet
	
	public static BufferedImage[] loadSpriteSheet(String fileLocation, int squareWidth, int realWidth, int height, int rows, int cols) {
		// Load the player spritesheet.
		try {
			BufferedImage bigImg = ImageIO.read(new File(fileLocation));
			// The above line throws an checked IOException which must be caught
			BufferedImage[] sprites = new BufferedImage[rows*cols];
		
			for (int i = 0; i < rows; i++)
			{
			    for (int j = 0; j < cols; j++)
			    {
			        sprites[(i * cols) + j] = bigImg.getSubimage(
			            j * squareWidth,
			            i * height,
			            squareWidth,
			            height
			        );
			    }
			}
			return sprites;
		}
		catch(Exception e) { e.printStackTrace(); }
		return null;
	}
}