package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class Card extends AnchorPane {
    private Controller parentController;
    private Product product;

    @FXML private ImageView cardImage;
    @FXML private Label cardName;


    public Card(Product product, Controller parentController){
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

    }

    @FXML
    public void addToCart(){
        parentController.addProductToCart(this.product);
    }
}
