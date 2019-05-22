package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class PreviousPurchase extends AnchorPane {
    private Controller parentController;
    private Order order;
    private EarlierOrdersPage earliersOrdersPage;

    @FXML
    private Label previousPurchaseDate;
    @FXML
    private Label previousPurchasePrice;
    @FXML
    private Button AddPreviousPurchaseToCartButton;


    public PreviousPurchase(Order order, EarlierOrdersPage earliersOrdersPage){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("previousPurchase.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;
        this.earliersOrdersPage = earliersOrdersPage;
        this.parentController = earliersOrdersPage.getParentController();

    }

    public void showShoppingItems(){
        earliersOrdersPage.showPreviousPurchaseDetail(order);
    }
}
