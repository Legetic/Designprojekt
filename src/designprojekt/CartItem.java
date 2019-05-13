package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class CartItem extends AnchorPane {
    private Controller parentController;

    public Controller getParentController() {
        return parentController;
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

    private ShoppingItem shoppingItem;


    public CartItem(ShoppingItem shoppingItem, Controller parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShoppingItem.fxml"));
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
