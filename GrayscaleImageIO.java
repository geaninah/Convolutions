import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Loads and saves grayscale images (two-dimensional brightness maps).
 *
 * @author Andrei Muntean
 */
public class GrayscaleImageIO
{
    /**
     * Loads an image (as a grayscale brightness map) from the specified path.
     * 
     * @param path The input image path.
     */
    public static int[][] loadImage(String path) throws IOException
    {
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] imageArray = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        int[][] image = new int[height][width];
        
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < width; ++x)
            {
                int pixel = imageArray[y * width + x];
                
                // The first 8 bits of the pixel represent the alpha channel. The next 8 bits represent the red
                // channel, followed by green and lastly by blue.
                int alpha = pixel >>> 24;
                int red = pixel << 8 >>> 24;
                int green = pixel << 16 >>> 24;
                int blue = pixel << 24 >>> 24;
                
                // The human eye perceives green light as brighter than red and red light as brighter than blue.
                // It is believed that 299/587/114 is the ideal brightness ratio.
                // NOTE: This needs to be revised once non-human intelligent lifeforms are discovered.
                image[y][x] = (int)(alpha / 255.0 * (red * 299 + green * 587 + blue * 114) / 1000);
            }
        }
        
        return image;
    }
    
    /**
     * Saves the specified grayscale image as a png file.
     *
     * @param image The grayscale image. Each integer value must be between 0 and 255.
     * @param path The output path.
     */
    public static void saveImage(int[][] image, String path) throws IOException
    {
        int height = image.length;
        int width = image[0].length;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < width; ++x)
            {
                if (image[y][x] < 0 || image[y][x] > 255)
                {
                    throw new IllegalArgumentException("Value out of bounds: " + image[y][x]);
                }
                
                // Makes the alpha channel opaque (255).
                int pixel = 0xFF000000;
                
                // Each color channel is set to the brightness value.
                pixel += image[y][x] << 16;
                pixel += image[y][x] << 8;
                pixel += image[y][x];
                
                // Sets the pixel in the image.
                bufferedImage.setRGB(x, y, pixel);
            }
        }
        
        ImageIO.write(bufferedImage, "png", new File(path));
    }
}