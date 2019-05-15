package designprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class Imat extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ResourceBundle bundle = java.util.ResourceBundle.getBundle("designprojekt/resources/Imat");

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"), bundle);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(root, screenSize.getWidth() * 0.75, screenSize.getHeight() * 0.75);

        stage.setTitle(bundle.getString("application.name"));
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Montserrat");
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setMinWidth(1050.0);
        stage.setMinHeight(600.0);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
