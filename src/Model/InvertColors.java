package Model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


/**
 * The class InvertColors represents the logic for inverting intensity values 
 * of a pixel matrix.
 * It takes a pixel matrix and returns a pixel matrix with
 * inverted intensity values. 
 * 
 * @author Erika Bengtsdotter, Gustaf Sjölinder
 */

public class InvertColors implements ProcessingInterface{
    
    
    /**
     * Private constructor since no object is needed.
     */
    public InvertColors(){
    }

    /**
     * Abstract method from implementation of ProcessingInterface that takes
     * a pixel matrix and return a pixel matrix with inverted values.
     * 
     * @param image
     * @return modified WritableImage
     */
    @Override
    public WritableImage manipulatingImage(WritableImage image) {
        return invertColors(image);
    }
    
    /**
     * Private help method that inverts the colors of an input pixel matrix 
     * and returns a modified WritableImage.
     * 
     * @param pixelMatrix
     * @return inverted WritableImage
     */ 
    private WritableImage invertColors(WritableImage pixelMatrix) {
        int width = (int) pixelMatrix.getWidth();
        int height = (int) pixelMatrix.getHeight();

        WritableImage result = new WritableImage(width, height);
        PixelReader pixelReader = pixelMatrix.getPixelReader();
        PixelWriter writer = result.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                writer.setColor(x, y, color.invert());
            }
        }

        return result;
    }
    
}
