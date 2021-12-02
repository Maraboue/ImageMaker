package Model;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;

/**
 * The ModelFacade class works as a facade for the logic classes that 
 * represent different image processing methods.
 * 
 * @author Erika Bengtsdotter, Gustaf Sjölinder
 */

public class ModelFacade {
    private ProcessingInterface processInterface;
    
    private Histogram histogram;
    private final BlurImage blurImage;
    private final ChangeContrast changeContrast;
    private final InvertColors invertColors;
    
    private WritableImage wImage;
    //private Image image;
    
    /**
     * Constructs a ModelFacade object with Histogram, BlurImage and 
     * ChangeContrast objects.
     */
    public ModelFacade(){
        blurImage = new BlurImage();
        changeContrast = new ChangeContrast();
        invertColors = new InvertColors();
        wImage = null;
    }
    
    /**
     * Takes an Image object and returns a GridPane with a histogram for
     * the input Image.
     * 
     * @param image
     * @return GridPane with histogram
     */
    public GridPane histogram(Image image){
        histogram = new Histogram(image);
        return histogram.runHistogram(image);
    }
    
    
    /**
     * Takes a WritableImage (pixel matrix) and a kernel value and returns 
     * a blurred pixel matrix.
     * 
     * @param image
     * @param kernel
     * @return blurred WritableImage
     */
    public WritableImage blurImage(WritableImage image, int kernel){
        blurImage.setKernel(kernel);
        wImage = blurImage.manipulatingImage(image);
        return wImage;
    }
    
    /**
     * Takes a WritableImage (pixel matrix), a window value and a level value
     * and returns a pixel matrix with changed contrast.
     * 
     * @param image
     * @param window
     * @param level
     * @return WritableImage with changed contrast
     */
    public WritableImage changeContrast(WritableImage image, int window, int level){
        changeContrast.setWindow(window);
        changeContrast.setLevel(level);
        wImage = changeContrast.manipulatingImage(image);
        return wImage;
    }
    
    /**
     * Takes a WritableImage (pixel matrix) and returns a pixel matrix with
     * inverted colors.
     * 
     * @param image
     * @return inverted WritableImage
     */
    public WritableImage invertColors(WritableImage image){
        InvertColors test = new InvertColors();
        wImage = test.manipulatingImage(image);
        //wImage = invertColors.manipulatingImage(image);
        return wImage;
    }
    
}
