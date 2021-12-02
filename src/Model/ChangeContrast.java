package Model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * The class ChangeContrast contains the logic for changing the window and 
 * level of a pixel matrix. It takes two integers as window and level and 
 * returns a WritableImage. 
 * 
 * The class implements the interface ProcessingInterface.
 * 
 * @author Erika Bengtsdotter, Gustaf Sjölinder
 */

public class ChangeContrast implements ProcessingInterface {

    private int window;
    private int level;
    
    private static final double CONVERSION_CONSTANT = 255;
    private static final double MAX_VALUE = 255;
    private static final int NO_OF_INTENSITIES = (int) MAX_VALUE + 1;
    
    /**
     * Constructs a new ChangeContrast object with given window and level.
     * The window represents the interval of pixel intensities.
     * The level represents the middle value of the window.
     * 
     * @param window
     * @param level 
     */
    public ChangeContrast(int window, int level) {
        this.window = window;
        this.level = level;
    }
    
    /**
     * Constructs a ChangeContrast object with the default window value set
     * to 256 and the default level value set to 128. 
     */
    public ChangeContrast() {
        this(256, 128);
    }
    
    /**
     * Sets the window to given window input parameter.
     * 
     * @param window 
     */
    public void setWindow(int window) {
        this.window = window;
    }
    
    /**
     * Sets the level to given level input parameter.
     * 
     * @param level 
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Abstract method from implemented interface that returns a modified 
     * WritableImage of an input pixel matrix.
     * 
     * @param image
     * @return modified WritableImage
     */
    @Override
    public WritableImage manipulatingImage(WritableImage image) {
        return changeContrast(image, window, level);
    }

    /**
     * Private help method that computes the modified WritableImage and 
     * returns it. 
     * Takes a pixel matrix, window value and level value as inputs.
     * Inspiration for LUT taken from the following webpage.
     * http://what-when-how.com/embedded-image-processing-on-the-tms320c6000-dsp/windowlevel-image-processing-part-1/
     * Author: The-Crankshaft Publishing
     * 
     * @param image
     * @param window
     * @param level
     * @return modified WritableImage
     */
    private WritableImage changeContrast(WritableImage image, 
            int window, int level) {
        PixelReader reader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        double[] LUT = new double[NO_OF_INTENSITIES];

        int startWindow = level - window / 2;
        int endWindow = level + window / 2;

        for (int i = 0; i < startWindow; i++) {
            if (startWindow < 0) {
                break;
            }
            LUT[i] = 0;
        }

        for (int i = startWindow; i <= endWindow; i++) {
            if (i < 0) {
                continue;
            }
            if (i >= LUT.length) {
                break;
            }
            LUT[i] = Math.round((MAX_VALUE / window) * (i - startWindow));
        }

        for (int i = endWindow + 1; i < LUT.length; i++) {
            if (i >= LUT.length) {
                break;
            }
            LUT[i] = MAX_VALUE;
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                Color color = reader.getColor(col, row);

                double[] channels = {
                    color.getRed() * CONVERSION_CONSTANT,
                    color.getGreen() * CONVERSION_CONSTANT,
                    color.getBlue() * CONVERSION_CONSTANT,
                    color.getOpacity() * CONVERSION_CONSTANT};

                for (int i = 0; i < channels.length; i++) {
                    channels[i] = LUT[(int) channels[i]];
                }

                Color newColor = Color.color(
                        channels[0] / CONVERSION_CONSTANT,
                        channels[1] / CONVERSION_CONSTANT,
                        channels[2] / CONVERSION_CONSTANT,
                        channels[3] / CONVERSION_CONSTANT);

                writer.setColor(col, row, newColor);

            }
        }

        return result;
    }
}
