package Model;

import javafx.event.ActionEvent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

/**
 * Histogram class that displays a histogram of the input image.
 * Inspiration taken from the following website.
 * http://java-buddy.blogspot.com/2015/07/display-images-histogram-on-javafx.html
 * Author: Java Buddy
 * 
 * @author Gustaf Sjölinder, Erika Bengtsdotter
 */
public class Histogram {

    private int fullPix = 256;
    private Image image;
    private static final double GRID_SIZE = 400;

    @SuppressWarnings("rawtypes")
    XYChart.Series sRed;
    @SuppressWarnings("rawtypes")
    XYChart.Series sGreen;
    @SuppressWarnings("rawtypes")
    XYChart.Series sBlue;

    private long red[] = new long[fullPix];
    private long green[] = new long[fullPix];
    private long blue[] = new long[fullPix];

    /**
     * Returns an array of red values.
     * 
     * @return 
     */
    @SuppressWarnings("rawtypes")
    public XYChart.Series getsRed() {
        return this.sRed;
    }

    /**
     * Returns an array of green values.
     * 
     * @return 
     */
    @SuppressWarnings("rawtypes")
    public XYChart.Series getsGreen() {
        return this.sGreen;
    }

    /**
     * Returns an array of blue values.
     * 
     * @return 
     */
    @SuppressWarnings("rawtypes")
    public XYChart.Series getsBlue() {
        return this.sBlue;
    }

    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Histogram(Image srcImage) {

        image = srcImage;

        sRed = new XYChart.Series();
        sGreen = new XYChart.Series();
        sBlue = new XYChart.Series();

       // HBox hbox = new HBox();

        sRed.setName("Red");
        sGreen.setName("Green");
        sBlue.setName("Blue");

        //init
        
        
        for (int i = 0; i < fullPix; i++) {
            red[i] = green[i] = blue[i] = 0;
        }

        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            return;
        }

        //count pixels
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int rgb = pixelReader.getArgb(x, y);

                int r = (0xff & (rgb >> 16));
                int g = (0xff & (rgb >> 8));
                int b = (0xff & rgb);

                blue[b]++;

                green[g]++;

                red[r]++;

            }
        }

        //copy red[], green[], blue[]
        //to sRed, sGreen, sBlue
        for (int i = 0; i < fullPix; i++) {

            sRed.getData().add(new XYChart.Data(String.valueOf(i), red[i]));

            sGreen.getData().add(new XYChart.Data(String.valueOf(i), green[i]));

            sBlue.getData().add(new XYChart.Data(String.valueOf(i), blue[i]));

        }

    }

    /**
     * Returns a gridpane with a histogram option of the input image.
     * 
     * @param image
     * @return 
     */
    @SuppressWarnings("unchecked")
    public GridPane runHistogram(Image image) {


        GridPane grid1 = new GridPane();
    

        NumberAxis y = new NumberAxis();
        CategoryAxis x = new CategoryAxis();
        LineChart<String, Number> chartHistogram = new LineChart<>(x, y);

        grid1.getColumnConstraints().add(new ColumnConstraints(GRID_SIZE));
        grid1.getRowConstraints().add(new RowConstraints(GRID_SIZE));
        grid1.setGridLinesVisible(false);

        //ImageView imageView = new ImageView();
        Button update = new Button("Update");
        update.setOnAction((ActionEvent event) -> {

            ImageView imageView = new ImageView();
            imageView.setImage(image);
            chartHistogram.getData().clear();

            chartHistogram.setCreateSymbols(false);

            Histogram imageHistogram = new Histogram(image);

            chartHistogram.getData().addAll(
                    imageHistogram.getsGreen(),
                    imageHistogram.getsRed(),
                    imageHistogram.getsBlue());
        });

        grid1.add(chartHistogram, 0, 0);
        grid1.add(update, 0, 4);

      
        return grid1;

    }

}
