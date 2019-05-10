package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    ImatBackendController imatBackendController = new ImatBackendController();
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    @FXML private FlowPane mainGrid;


    List<Product> productList = new ArrayList<>();
    private Map<String, Card> cardMap = new HashMap<String, Card>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Product item : imatBackendController.getProducts()) {
            Card productCard = new Card(item, this);
            cardMap.put(item.getName(), productCard);
        }


        updateMainGrid();
    }

    private void updateMainGrid(){

        mainGrid.getChildren().clear();
        productList = imatBackendController.getProducts();
        for (Product r : productList){
            Card productCard = cardMap.get(r.getName());
            mainGrid.getChildren().add(productCard);

        }

    }


    public Image getProductImage(Product product) {
       return dataHandler.getFXImage(product);
    }

    public Image getProductImage(Product product, double width, double height, boolean keepRatio) {
       return dataHandler.getFXImage(product,width,height,keepRatio);
    }

}