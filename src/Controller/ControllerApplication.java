package Controller;

import java.io.FileInputStream;
import java.util.List;
import Model.ModelFacade;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


/**
 * The ControllerApplication class runs an image processing program that can
 * open, edit and save images. It uses the JavaFX API.
 * 
 * @author Gustaf Sjölinder, Erika Bengtsdotter
 */

public class ControllerApplication extends Application {

    // Stage
    private Stage menu;

    // Scenes 
    private Scene mainMenu;

    // Menus & choices 
    private Menu fileMenu;		

    private MenuItem openFile;

    private MenuItem saveFile;

    private Menu genMenu;
    
    private MenuItem separator;
    
    private MenuItem separator1;

    private MenuItem histogramGen;

    private MenuItem invertGen;

    private MenuItem contrastGen;

    private MenuItem blurGen;

    private MenuItem fullscreen;

    private MenuItem exit;

    CustomMenuItem customMenuItem;

    private Button invertPic;

    private Text field;

    private Image image;
    private ImageView viewImage;

    private HBox hbox;
    private GridPane grid1;
    private GridPane grid2;

    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser fileSaver = new FileChooser();

    private final ModelFacade model = new ModelFacade();
    private WritableImage wImage;
    private int window, level;

    private static final double GRID_SIZE = 400;
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 600;
    
    /**
     * Main method that runs the program.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method containing the stage, scene, event handlers.
     * 
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        menu = primaryStage;
        /**
         * ******************************************************
         */
        // Layout main menu 
        VBox box = new VBox();

        Slider slider = new Slider(0, 200, 100);

        CustomMenuItem customMenuItem = new CustomMenuItem();

        // Creating the file menu
        fileMenu = new Menu("File");
        openFile = new MenuItem("Open file");
        saveFile = new MenuItem("Save file");
        exit = new MenuItem("Exit");
        separator = new SeparatorMenuItem();

        fileMenu.getItems().addAll(openFile, saveFile, separator, exit);

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().add(fileMenu);
        genMenu = new Menu("Generate");
        histogramGen = new MenuItem("Histogram");
        contrastGen = new MenuItem("Contrast");
        invertGen = new MenuItem("Invert");
        invertPic = new Button("Invert Picture");
        blurGen = new MenuItem("Blur");
        fullscreen = new MenuItem("Fullscreen");

         separator1 = new SeparatorMenuItem();

        field = new Text(" Welcome to ImageMaker!\n Made by Gustaf Sjölinder and Erika Bengtsdotter");
        field.setFill(Color.BLACK);
        field.setStyle("-fx-font: 18 arial;");

        genMenu.getItems().addAll(histogramGen, contrastGen, invertGen, blurGen, separator1, fullscreen);

        // Setting custom menu items 
        customMenuItem.setContent(slider);
        customMenuItem.setHideOnClick(true);
        slider.setShowTickLabels(true);

        grid1 = new GridPane();
        grid2 = new GridPane();

        grid1.getColumnConstraints().add(new ColumnConstraints(GRID_SIZE));
        grid1.getRowConstraints().add(new RowConstraints(GRID_SIZE));
        grid1.setGridLinesVisible(true);

        grid2.getColumnConstraints().add(new ColumnConstraints(GRID_SIZE));
        grid2.getRowConstraints().add(new RowConstraints(GRID_SIZE));
        grid2.setGridLinesVisible(true);

        hbox = new HBox();

        List<Node> children = box.getChildren();

        // Setting Action-Events
        // create an alert 
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setContentText("Do you wish to save your picture before exiting?");

        // action event
        /**
         * Asks the user if they want to save the open image before closing 
         * the program.
         */
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    saveFile(primaryStage);

                    // Insert save file here
                    alert.close();
                    primaryStage.close();

                }
                if (result.isPresent() && result.get() == ButtonType.CANCEL) {

                    alert.close();
                    primaryStage.close();

                }
            }

        });

        /**
         * Sets the program to full screen.
         */
        fullscreen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                menu.setFullScreen(true);

            }

        });

        /**
         * Saves the image to a filename and destination chosen by the user.
         */
        saveFile.setOnAction(
                new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent e) {

                saveFile(primaryStage);

            }

        });

        /**
         * Opens a file with given name and destination in a FileChooser
         * dialog window.
         */
        openFile.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    try {
                        boolean test = false;
                        test = openFile(file, test);
                        if (!test) {
                            menuBar.getMenus().add(genMenu);
                        }
                        grid2.add(viewImage, 0, 0);
                    } catch (FileNotFoundException ex) {
                        Alert alertException = new Alert(AlertType.INFORMATION);
                        alertException.setContentText("Could not open file. Exiting program.");
                        alertException.close();
                        primaryStage.close();
                    }
                }
            }
        });

        /**
         * Blurs the image by iterating over every single pixel with a 
         * certain box kernel.
         */
        blurGen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                grid1.getChildren().clear();
                grid1.setVgap(5);

                wImage = null;
                wImage = pixelMatrix(image);

                Slider slider = new Slider();
                slider.setShowTickLabels(true);
                slider.setMin(0);
                slider.setMax(10);
                Text window = new Text();
                Button saveChange = new Button("Save changes");

                grid1.add(slider, 0, 0);
                grid1.add(window, 0, 1);
                grid1.add(saveChange, 0, 3);

                slider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                            Number old_val, Number new_val) {

                        int kernel = new_val.intValue();
                        window.setText("Blur value: " + Integer.toString(kernel));
                        WritableImage newImage = model.blurImage(wImage, kernel);
                        viewImage.setImage(newImage);
                    }
                });

                saveChange.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent e) {
                        image = viewImage.getImage();
                    }

                });
            }
        });

        /**
         * Inverts the colors of the image and saves the processed image.
         */
        invertGen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                grid1.getChildren().clear();
                grid1.add(invertPic, 0, 0);

                invertPic.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event1) {
                        WritableImage invertImage = pixelMatrix(image);

                        WritableImage wImage = model.invertColors(invertImage);
                        viewImage.setImage(wImage);
                        image = viewImage.getImage();
                        grid1.getChildren().clear();
                        grid1.getChildren().add(new Text("Image inverted"));
                    }

                });
            }
        });

        /**
         * Changes the contrast of the image with help from window and level.
         */
        contrastGen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                grid1.getChildren().clear();
                VBox tempBox = new VBox();

                Slider sliderW = new Slider();
                Slider sliderL = new Slider();

                Text textWindow = new Text("Window:");
                Text textLvl = new Text("Level:");

                Text textW = new Text("Window: ");
                Text textL = new Text("Level: ");

                sliderW.setShowTickLabels(true);
                sliderL.setShowTickLabels(true);
                sliderW.setMin(0);
                sliderW.setMax(255);
                sliderL.setMin(0);
                sliderL.setMax(255);

                sliderW.setMaxWidth(350);
                sliderL.setMaxWidth(350);
//     

                Button contrast = new Button("Update contrast");
                Button saveChange = new Button("Save changes");

                tempBox.getChildren().addAll(textWindow, sliderW, textLvl, sliderL, contrast, saveChange);
                tempBox.getChildren().addAll(textW, textL);
                grid1.add(tempBox, 0, 0);

                sliderW.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                            Number old_val, Number new_val) {
                        window = new_val.intValue();
                        textW.setText("Window: " + Integer.toString(window));
                    }
                });

                sliderL.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                            Number old_val, Number new_val) {
                        level = new_val.intValue();
                        textL.setText("Level: " + Integer.toString(level));
                    }
                });

                contrast.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent e) {

                        wImage = null;
                        wImage = pixelMatrix(image);

                        wImage = model.changeContrast(wImage, window, level);
                        viewImage.setImage(wImage);

                    }

                });

                saveChange.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent e) {
                        image = viewImage.getImage();
                    }

                });
            }
        });

        /**
         * Displays a histogram based on the color values of the image.
         */
        histogramGen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                hbox.getChildren().clear();
                grid1 = model.histogram(image);
                hbox.getChildren().add(grid1);
                hbox.getChildren().add(grid2);

            }

        });

        hbox.getChildren().addAll(grid1, grid2);

        //  adding the children 
        children.add(menuBar);
        children.add(field);
        children.add(hbox);

        // Creating the main menu
        mainMenu = new Scene(box, SCENE_WIDTH, SCENE_HEIGHT);

        // Creating the menu
        menu.setTitle("ImageMaker");
        menu.setScene(mainMenu);
        menu.show();

    }

    /**
     * Private help method to open an image file.
     * @param file
     * @param test
     * @return
     * @throws FileNotFoundException 
     */
    private boolean openFile(File file, boolean test) throws FileNotFoundException {
        try {
            if (image != null) {
                test = true;
            }
            image = new Image(new FileInputStream(file));
            viewImage = new ImageView(image);
            viewImage.setFitHeight(GRID_SIZE);
            viewImage.setFitWidth(GRID_SIZE);
        } catch (FileNotFoundException ex) {
            throw ex;
        } finally {
            if (image != null) {
                image.cancel();
            }
        }
        return test;
    }

    /**
     * Private help method that creates a pixel matrix of the open image.
     * @param image
     * @return 
     */
    private WritableImage pixelMatrix(Image image) {
        PixelReader reader = image.getPixelReader();

        int height = (int) image.getHeight();
        int width = (int) image.getWidth();

        WritableImage newImage = new WritableImage(width, height);

        PixelWriter writer = newImage.getPixelWriter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = reader.getColor(j, i);
                writer.setColor(j, i, color);
            }
        }

        return newImage;
    }

    /**
     * Private help method to save file. Takes the stage as input.
     * Inspiration taken from a Java Tutorial on the following webpage.
     * https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
     * Author: Alla Redko
     * 
     * @param primaryStage 
     */
    private void saveFile(Stage primaryStage) {
        fileSaver.setTitle("Save Image");
        fileSaver.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png"));
        File newFile = fileSaver.showSaveDialog(primaryStage);

        if (newFile != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", newFile);
            } catch (IOException ex) {
                Alert alertException = new Alert(AlertType.INFORMATION);
                alertException.setContentText("Could not save file. Exiting program.");
                alertException.close();
                primaryStage.close();
            }
        }
    }

}

