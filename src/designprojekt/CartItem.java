package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class CartItem extends AnchorPane {
    private Controller parentController;
    private ShoppingItem shoppingItem;
    private Card card;
    @FXML
    private Label cartItemName;
    @FXML
    private Label cartItemPrice;
    @FXML
    private Label cartItemTotalPrice;
    @FXML
    private TextField cartItemAmount;


    public CartItem(ShoppingItem shoppingItem, Controller parentController, Card card) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setLeftAnchor(this,0.0);
        setRightAnchor(this,0.0);

        this.shoppingItem = shoppingItem;
        this.parentController = parentController;
        this.card = card;


        cartItemName.setText(shoppingItem.getProduct().getName());
        cartItemPrice.setText(shoppingItem.getProduct().getPrice() + "");
        updatePrice();
        System.out.println(Math.round(shoppingItem.getTotal() * 100) / 100);
        cartItemAmount.focusedProperty().addListener((ov, oldV, newV) -> { //executes setAmount() on lost focus
            if (!newV) {
                setAmount();
            }
        });
    }

    @FXML
    public void removeItem() {
        parentController.removeItem(card);

    }


    @FXML
    public void decAmount() {
        card.decAmount();
    }
    @FXML
    public void incAmount() {
        card.incAmount();
    }
    @FXML
    public void setAmount() { //Invalid input doesn't exist with this method.
        card.setAmount(false);
    }
    @FXML
    public void selectText() {
        parentController.selectText(cartItemAmount);
    }

    @FXML
    public void updatePrice() {
        parentController.updatePrice(cartItemTotalPrice, shoppingItem);
    }

    public Controller getParentController() {
        return this.parentController;
    }

    public ShoppingItem getShoppingItem() {
        return this.shoppingItem;
    }




    public TextField getAmountField() {
        return cartItemAmount;
    }

    public Label getCartItemTotalPrice() {
        return cartItemTotalPrice;
    }
}
