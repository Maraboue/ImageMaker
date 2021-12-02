

package Model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


/**
 * The BlurImage class represents the logic for blurring a pixel matrix.
 * It takes a WritableImage, blurs it with a given kernel value and returns
 * a modified WritableImage.
 * Inspiration for blurring close to edges taken from the following website.
 * https://skrb.hatenablog.com/entry/2013/01/07/171901
 * Author: プロフィール
 * 
 * @author Erika Bengtsdotter, Gustaf Sjölinder
 */

public class BlurImage implements ProcessingInterface {

    private int kernel;

    /**
     * Constructs a BlurImage object with a given kernel value.
     * 
     * @param kernel 
     */
    public BlurImage(int kernel) {
        this.kernel = kernel;
    }

    /**
     * Constructs a BlurImage object with a kernel value of 5 as default.
     */
    public BlurImage() {
        this(5);
    }
    
    /**
     * Sets the value of the kernel to the given input.
     * 
     * @param kernel 
     */
    public void setKernel(int kernel){
        this.kernel = kernel;
    }
    
    /**
     * Abstracts method from implementation of ProcessingInterface that takes
     * a WritableImage and returns a blurred WritableImage.
     * 
     * @param image
     * @return modified WritableImage.
     */
    @Override
    public WritableImage manipulatingImage(WritableImage image) {
        return blurImage(image, kernel);
    }

    /**
     * Private help method that takes a pixel matrix and a kernel value as
     * input and returns a blurred WritableImage.
     * 
     * @param image
     * @param kernel
     * @return modified WritableImage
     */
    private WritableImage blurImage(WritableImage image, int kernel) {
        PixelReader reader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        int n = kernel / 2;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                double red = 0;
                double green = 0;
                double blue = 0;
                double opacity = 0;

                double noOfPixels = 0;

                for (int r = row - n; r <= row + n; r++) {
                    for (int c = col - n; c <= col + n; c++) {

                        if (r < 0 || r >= height || c < 0 || c >= width) {
                            continue;
                        }

                        red += reader.getColor(c, r).getRed();
                        green += reader.getColor(c, r).getGreen();
                        blue += reader.getColor(c, r).getBlue();
                        opacity += reader.getColor(c, r).getOpacity();

                        noOfPixels++;
                    }
                }

                Color color = new Color(
                        red / noOfPixels,
                        green / noOfPixels,
                        blue / noOfPixels,
                        opacity / noOfPixels);

                writer.setColor(col, row, color);

            }
        }

        return result;
    }
}

