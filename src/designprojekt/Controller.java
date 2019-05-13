package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    ImatBackendController imatBackendController = new ImatBackendController();
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    @FXML private FlowPane mainGrid;
    @FXML private AnchorPane startMenu;
    @FXML private Button startPage_exit_btn;
    @FXML private Button startPage_home_btn;
    @FXML private Button startPage_recentBuy_btn;
    @FXML private Button addButton;
    @FXML private FlowPane shoppingCartFlowPane;


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

    private void addCartItem(CartItem cartItem){ //VERY WIP
        dataHandler.getShoppingCart().addItem(cartItem.getShoppingItem());
        shoppingCartFlowPane.getChildren().add(cartItem);
    }


    public Image getProductImage(Product product) {
       return dataHandler.getFXImage(product);
    }

    public Image getProductImage(Product product, double width, double height, boolean keepRatio) {
       return dataHandler.getFXImage(product,width,height,keepRatio);
    }


    @FXML
    public void closeStartMenu(){
        startMenu.toBack();
    }

    /*public void openStartMenu(){

        startMenu.toFront();
    }*/

}