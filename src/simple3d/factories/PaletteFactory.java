package simple3d.factories;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by tfisher on 30/07/2017.
 */
public class PaletteFactory {
    public static void build() {
        Color[][] image = new Color[1][4];
        image[0][0] = Color.GRAY;
        image[0][1] = Color.RED;
        image[0][2] = Color.BLUE;
        image[0][3] = Color.GREEN;

// Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(image.length, image[0].length,
                BufferedImage.TYPE_INT_RGB);

// Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[x].length; y++) {
                bufferedImage.setRGB(x, y, image[x][y].getRGB());
            }
        }

        // retrieve image
        File outputfile = new File("resources/saved.png");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
