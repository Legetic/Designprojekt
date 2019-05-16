package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable{

    ImatBackendController imatBackendController = new ImatBackendController();
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    @FXML private AnchorPane startMenu;
    @FXML private BorderPane homePage;


    @FXML private FlowPane mainGrid;
    @FXML private Button startPage_exit_btn;
    @FXML private Button startPage_home_btn;
    @FXML private Button startPage_recentBuy_btn;
    @FXML private Button addButton;
    @FXML private FlowPane shoppingCartFlowPane;

    @FXML private AnchorPane previousPurchasesRoot;
    @FXML private AnchorPane fullscreenPage;



    private List<Product> productList = new ArrayList<>();
    private List<ShoppingItem> cartList = new ArrayList<>();

    private Map<String, Card> cardMap = new HashMap<String, Card>();
    private Map<String, ShoppingItem> shoppingItemMap = new HashMap<String, ShoppingItem>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        for (Product item : imatBackendController.getProducts()) {
            Card productCard = new Card(item, this);
            cardMap.put(item.getName(), productCard);
        }


        updateMainGrid();
    }

    private void updateMainGrid() {

        mainGrid.getChildren().clear();
        productList = imatBackendController.getProducts();
        for (Product r : productList) {
            Card productCard = cardMap.get(r.getName());
            mainGrid.getChildren().add(productCard);

        }

    }


    @FXML private void fillSearchList(){

    }


    //SHOPPING CART
    private void updateShoppingCart() {//TODO: MAKE USE OF CARDS INSTEAD OF CARTITEMS!
        shoppingCartFlowPane.getChildren().clear();
        cartList = dataHandler.getShoppingCart().getItems();
        for (ShoppingItem s : cartList) {
            //CartItem cartItem = new CartItem(s, this);
            //shoppingCartFlowPane.getChildren().add(cartItem);

        }
    }

    public void clearShoppingCart() {
        shoppingCartFlowPane.getChildren().clear();
        dataHandler.getShoppingCart().clear();
    }

    protected void setAmount() {}//TODO: fix this

    protected void removeItem(CartItem cartItem) {
        shoppingCartFlowPane.getChildren().remove(cartItem);
        dataHandler.getShoppingCart().getItems().remove(cartItem.getShoppingItem());
        cartItem.getCard().getAmountControl().setVisible(false);
        cartItem.getCard().getAddButton().setVisible(true);
    }

    public void addProductToCart(Card productCard) {//TODO: implement separate method for the duplicate check
        Boolean isDuplicate = false;
        for(ShoppingItem si : dataHandler.getShoppingCart().getItems()) {
            if(si.getProduct().equals(productCard.getProduct())) {
                isDuplicate = true;
                break;
            }
        }
        if(!isDuplicate) {//If not duplicate, add to cart
            ShoppingItem item = new ShoppingItem(productCard.getProduct());
            CartItem cartItem = new CartItem(item, this, productCard);
            dataHandler.getShoppingCart().addItem(item);
            shoppingCartFlowPane.getChildren().add(cartItem);
            productCard.getAmountControl().setVisible(true);
            productCard.getAddButton().setVisible(false);
        }
    }




    //GET IMAGES

    public Image getProductImage(Product product) {
        return dataHandler.getFXImage(product);
    }

    public Image getProductImage(Product product, double width, double height, boolean keepRatio) {
        return dataHandler.getFXImage(product, width, height, keepRatio);
    }


    //MENUS AND PAGES
    @FXML
    public void closeStartMenu() {
        startMenu.toBack();
    }

    @FXML
    public void openPreviousPurchases() throws IOException{
        //ResourceBundle bundle = java.util.ResourceBundle.getBundle("designprojekt/resources/Imat");
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("earlierOrders.fxml"));

        previousPurchasesRoot.getChildren().clear();
        EarlierOrdersPage earlierOrdersPage = new EarlierOrdersPage( this);
        previousPurchasesRoot.getChildren().addAll(earlierOrdersPage);

        homePage.toFront();
        previousPurchasesRoot.toFront();

    }

    @FXML
    public void closePreviousPurchases(){

        previousPurchasesRoot.toBack();

    }

    @FXML
    public void openCustomerServicePage(){
        previousPurchasesRoot.getChildren().clear();
        CustomerServicePage customerServicePage = new CustomerServicePage( this);
        previousPurchasesRoot.getChildren().addAll(customerServicePage);

        homePage.toFront();
        previousPurchasesRoot.toFront();

    }
    @FXML
    public void closeCustomerServicePage(){
        previousPurchasesRoot.toBack();

    }

    @FXML
    public void openCheckoutPage(){
        fullscreenPage.getChildren().clear();
        Checkout checkout = new Checkout( this);
        fullscreenPage.getChildren().addAll(checkout);

        homePage.toFront();
        fullscreenPage.toFront();

    }

    @FXML
    public void closeCheckoutPage(){
        fullscreenPage.toBack();

    }

    @FXML
    public void goHome(){
        homePage.toFront();
    }




    /*public void openStartMenu(){

        startMenu.toFront();
    }*/

}