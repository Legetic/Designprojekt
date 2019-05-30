package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ItemPreviousPurchase extends AnchorPane {
    private Controller parentController;
    private ShoppingItem shoppingItem;

    @FXML
    private ImageView detailPreviousPurchaseImageView;
    @FXML
    private Label itemPreviousPurchaseLabel;
    @FXML
    private Label itemAmountPreviousPurchaseLabel;
    @FXML
    private Label itemPricePreviousPurchaseLabel;
    @FXML
    private Label itemTotalPricePreviousPurchaseLabel;


    public ItemPreviousPurchase(ShoppingItem shoppingItem, Controller parentController){
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

        detailPreviousPurchaseImageView.setImage(this.parentController.getProductImage(shoppingItem.getProduct()));
        itemPreviousPurchaseLabel.setText(shoppingItem.getProduct().getName());
        itemAmountPreviousPurchaseLabel.setText((int)shoppingItem.getAmount() + "");
        itemPricePreviousPurchaseLabel.setText(shoppingItem.getProduct().getPrice() + "");
        double totalPrice = Math.round(shoppingItem.getTotal() * 100.0) / 100.0; // eliminates round off bug
        itemTotalPricePreviousPurchaseLabel.setText(totalPrice + "");
    }


}
