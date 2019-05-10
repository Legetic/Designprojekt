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
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());

        stage.setTitle(bundle.getString("application.name"));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
