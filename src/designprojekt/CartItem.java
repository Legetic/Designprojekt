package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class CartItem extends AnchorPane {
    private Controller parentController;
    private ShoppingItem shoppingItem;

    @FXML
    private Label cartItemName;
    @FXML
    private Label cartItemPrice;
    @FXML
    private Label cartItemTotalPrice;
    @FXML
    private TextField cartItemAmount;


    public CartItem(ShoppingItem shoppingItem, Controller parentController) {
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
        parentController.removeItem(this);
    }


    @FXML
    public void decAmount() {
        shoppingItem.setAmount(shoppingItem.getAmount() - 1);
        if(shoppingItem.getAmount() < 1) {
            //shoppingItem.setAmount(0);
            removeItem();
        }
        cartItemAmount.setText((int) shoppingItem.getAmount() + " st");
        updatePrice();
    }
    @FXML
    public void incAmount() {
        shoppingItem.setAmount(shoppingItem.getAmount() + 1);
        if(shoppingItem.getAmount() < 0) { //Maybe redundant
            shoppingItem.setAmount(0);
        }
        cartItemAmount.setText((int) shoppingItem.getAmount() + " st");
        updatePrice();
    }
    @FXML
    public void setAmount() { //Invalid input doesn't exist with this method.
        try {
            shoppingItem.setAmount(Integer.parseInt(cartItemAmount.getText())); //throws NumberFormatException if String contains non-digits
        } catch(NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            for(char c : cartItemAmount.getText().toCharArray()) { //Isolates digits
                if(Character.isDigit(c)) {
                    stringBuilder.append(c);
                }
            }
            String digitString = stringBuilder.toString();
            if(!digitString.isEmpty()) { //if the string is empty (contained no digits), reset amount
                shoppingItem.setAmount(Integer.parseInt(digitString));
            } else {
                shoppingItem.setAmount(shoppingItem.getAmount());
            }
        }
        if(shoppingItem.getAmount() <= 0) {
            shoppingItem.setAmount(1);
        }
        cartItemAmount.setText((int)shoppingItem.getAmount() + " st");
        updatePrice();
    }
    @FXML
    public void selectText() {
        cartItemAmount.selectAll();
    }
    @FXML
    public void updatePrice() {
        cartItemTotalPrice.setText(shoppingItem.getTotal() + " kr");
    }


    public Controller getParentController() {
        return this.parentController;
    }

    public ShoppingItem getShoppingItem() {
        return this.shoppingItem;
    }




}
