package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class Card extends AnchorPane {
    private Controller parentController;
    private Product product;
    private CartItem cartItem;

    @FXML
    private ImageView cardImage;
    @FXML
    private Label cardPrice;
    @FXML
    private Label cardName;


    @FXML
    private TextField amountTextField;

    @FXML
    private HBox amountControl;
    @FXML
    private HBox addButton;


    public Card(Product product, Controller parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        this.parentController = parentController;

        cardImage.setImage(this.parentController.getProductImage(product));
        cardName.setText(product.getName());
        cardPrice.setText(product.getPrice() + "");

        amountTextField.focusedProperty().addListener((ov, oldV, newV) -> { //executes setAmount() on lost focus
            if (!newV) {
                setAmount();
            }
        });

    }

    @FXML
    public void setAmount() {
        parentController.setAmount(this, true);
    }

    protected void setAmount(boolean readCard) {
        parentController.setAmount(this, readCard);
    }

    public void decAmount() {
        parentController.decAmount(this);
    }

    public void incAmount() {
        parentController.incAmount(this);
    }

    @FXML
    public void addToCart() {
        parentController.addProductToCart(this);
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public Product getProduct() {
        return product;
    }

    public HBox getAddButton() {
        return addButton;
    }

    public HBox getAmountControl() {
        return amountControl;
    }

    public TextField getAmountField() {
        return amountTextField;
    }


    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public void selectText() {
        parentController.selectText(amountTextField);
    }
}
