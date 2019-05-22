package designprojekt;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class itemPreviousPurchase extends AnchorPane {
    private Controller parentController;
    private ShoppingItem shoppingItem;


    public itemPreviousPurchase(ShoppingItem shoppingItem, Controller parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("itemPreviousPurchase.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = shoppingItem;
        this.parentController = parentController;


    }
}
