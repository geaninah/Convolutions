import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

public class Convolutions
{
    static final double[][] innerOutline = new double[][] {
        { -1, -1, -1 },
        { -1, 8, -1 },
        { -1, -1, -1 }
    };

    static final double[][] outerOutline = new double[][] {
        { 1, 1, 1 },
        { 1, -8, 1 },
        { 1, 1, 1 }
    };

    static final double[][] average = new double[][] {
        { 1.0 / 9, 1.0 / 9, 1.0 / 9 },
        { 1.0 / 9, 1.0 / 9, 1.0 / 9 },
        { 1.0 / 9, 1.0 / 9, 1.0 / 9 }
    };

    static final double[][] keepMiddle = new double[][] {
        { 0, 0, 0 },
        { 0, 1, 0 },
        { 0, 0, 0 }
    };

    // Put the elements of the matrix in a list
    static List<Integer> toList(int[][] picture, int startY, int startX, int endY, int endX)
    {
        List<Integer> list = new ArrayList<Integer>();
        
        for(int y = startY; y < endY; y++)
        {
            for(int x = startX; x < endX; x++)
            {
                list.add(picture[y][x]);
            }
        }

        return list;
    }

    // Get the median element of the region
    static int medianFilter(int[][] picture, int startY, int startX, int endY, int endX)
    {
        List<Integer> list = toList(picture, startY, startX, endY, endX);
        
        Collections.sort(list);

        int median = list.get(list.size() / 2);

        return median;
    }

    // Get the greatest element of the region
    static int maxFilter(int[][] picture, int startY, int startX, int endY, int endX)
    {
        int max = 0;

        for(int y = startY; y < endY; ++y)
        {
            for(int x = startX; x < endX; ++x)
            {
                if(picture[y][x] > max)
                {
                    max = picture[y][x];
                }
            }
        }

        return max;
    }

    // Multiplies the region with the convolution
    static int multiply(int[][] picture, int startY, int startX, int endY, int endX, double[][] convolution)
    {
        double result = 0;

        for(int y = startY; y < endY; y++)
        {
            for(int x = startX; x < endX; x++)
            {
                result += convolution[y - startY][x - startX] * picture[y][x];
            }
        }

        if(result < 0)
        {
            return 0;
        }
        else if(result > 255)
        {
            return 255;
        }

        return (int)Math.round(result);
    }

    public static int[][] convolve(int[][] picture, int size, int stride)
    {
        int height = picture.length;
        int width = picture[0].length;

        int[][] convolvedPicture = new int[(height - size) / stride + 1][(width - size) / stride + 1];
        int offset = size / 2;

        for(int y = offset; y < height - offset; y += stride)
        {
            for(int x = offset; x < width - offset; x += stride)
            {
                int mappedY = (y - offset) / stride;
                int mappedX = (x - offset) / stride;
                
                if(size % 2 == 0)
                {
                    convolvedPicture[mappedY][mappedX] = multiply(picture,
                        y - offset, x - offset,
                        y + offset, x + offset,
                        innerOutline);
                }
                else
                {
                    convolvedPicture[mappedY][mappedX] = multiply(picture,
                        y - offset, x - offset,
                        y + offset + 1, x + offset + 1,
                        innerOutline);
                }
            }
        }

        return convolvedPicture;
    }

    public static void main(String args[]) throws IOException
    {
        int[][] image = GrayscaleImageIO.loadImage(args[0]);
        int[][] modifiedImage = convolve(image, 3, 3);

        GrayscaleImageIO.saveImage(modifiedImage, args[1]);
    }
}