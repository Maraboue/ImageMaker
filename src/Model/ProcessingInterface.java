package Model;

import javafx.scene.image.WritableImage;
/**
 * Interface to process an image with different methods through an 
 * abstract method.
 * 
 * @author Erika Bengtsdotter, Gustaf Sjölinder
 */
public interface ProcessingInterface {
    public abstract WritableImage manipulatingImage(WritableImage image);
}
