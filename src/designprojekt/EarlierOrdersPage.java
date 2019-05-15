package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EarlierOrdersPage extends AnchorPane {
    private Controller parentController;

    public EarlierOrdersPage(Controller parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("earlierOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        //set size 100%
        setBottomAnchor(this,0.0);
        setTopAnchor(this,0.0);
        setRightAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);

    }

    @FXML
    public void closeWindow() throws IOException{
        parentController.closePreviousPurchases();

    }

}
