import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Convolutions
{
    // Get the last element of a list
    private static int maxFilter(int[][] picture, int row, int column)
    {
        List<Integer> list = toArray(picture, row, column);
        
        Collections.sort(list);

        int max = list.get(list.size() - 1);

        return max;
    }

    // Get the median element of a list
    private static int medianFilter(int[][] picture, int row, int column)
    {
        List<Integer> list = toArray(picture, row, column);
        
        Collections.sort(list);

        int median = list.get(list.size() / 2);

        return median;
    }

    // Put the elements of the matrix in a list
    private static List<Integer> toArray(int[][] picture, int row, int column)
    {
        List<Integer> list = new ArrayList<Integer>();
        
        for(int index1 = row; index1 < row + 2; index1++)
        {
            for(int index2 = column; index2 < column + 2; index2++)
            {
                list.add(picture[index1][index2]);
            }
        }

        return list;
    }

    public static int[][] convolve(int[][] picture, int stride)
    {
        int[][] croppedPicture = new int[picture.length / 2][picture[0].length / 2];

        for(int row = 0; row < picture.length; row += stride)
        {
            for(int column = 0; column < picture[0].length; column += stride)
            {
                int croppedRow = row / stride;
                int croppedColumn = column / stride;

                // Replace 4 pixels with one
                croppedPicture[croppedRow][croppedColumn] = medianFilter(picture, row, column);
            }
        }

        return croppedPicture;
    }

    public static void main(String args[])
    {
        int[][] picture = {
            {2, 3, 5, 1, 7, 5},
            {2, 3, 5, 2, 8, 4},
            {9, 5, 4, 2, 1, 2},
            {2, 5, 6, 2, 6, 9},
            {1, 0, 5, 2, 8, 7},
            {5, 2, 7, 9, 2, 0}
        };

        int[][] croppedPicture = convolve(picture, 2);

        int height = croppedPicture.length;
        int width = croppedPicture[0].length;

        // Print the result
        for(int row = 0; row < height; row++)
        {
            for(int column = 0; column < width; column++)
            {
                System.out.print(croppedPicture[row][column] + " ");
            }

            System.out.println();
        }
    }
}