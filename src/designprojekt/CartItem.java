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
    private ShoppingItem shoppingItem;

    @FXML private Label cartItemName;
    @FXML private Label cartItemPrice;
    @FXML private Label cartItemTotalPrice;


    public CartItem(ShoppingItem shoppingItem, Controller parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = shoppingItem;
        this.parentController = parentController;

        cartItemName.setText(shoppingItem.getProduct().getName());
        cartItemPrice.setText(shoppingItem.getProduct().getPrice() + "");
        cartItemTotalPrice.setText(shoppingItem.getTotal() + " kr");
        System.out.println(Math.round(shoppingItem.getTotal() * 100) / 100);
    }

    public Controller getParentController() {
        return this.parentController;
    }

    public ShoppingItem getShoppingItem() {
        return this.shoppingItem;
    }




}
