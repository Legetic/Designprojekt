package designprojekt;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.*;


import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Controller implements Initializable {

    ImatBackendController imatBackendController = new ImatBackendController();
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    @FXML
    private AnchorPane startMenu;
    @FXML
    private BorderPane homePage;


    @FXML
    private FlowPane mainGrid;
    @FXML
    private Button startPage_exit_btn;
    @FXML
    private Button startPage_home_btn;
    @FXML
    private Button startPage_recentBuy_btn;
    @FXML
    private Button addButton;
    @FXML
    private FlowPane shoppingCartFlowPane;

    @FXML
    private AnchorPane previousPurchasesRoot;
    @FXML
    private AnchorPane fullscreenPage;



    @FXML
    private ListView searchList;
    @FXML
    private TextField searchBar;
    @FXML
    private ScrollPane searchPane;



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

        searchBar.addEventFilter(KeyEvent.KEY_PRESSED, ( KeyEvent event ) ->  {
            if(event.getCode() == KeyCode.DOWN) {
                searchList.requestFocus();
                searchList.getFocusModel().focusNext();
                //searchList.getSelectionModel().selectNext();
            }
            if(event.getCode() == KeyCode.UP) {
                searchList.getSelectionModel().selectPrevious();
            }

            });

        searchBar.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) {
                    //focusgained - do nothing
                    //openSearchList();
                } else {

                    closeSearchList();
                }

            }
        });
        searchList.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) {
                    //focusgained - do nothing
                    openSearchList();
                } else {

                    closeSearchList();
                }

            }
        });

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {

                openSearchList();

            List<Product> searchResults = dataHandler.findProducts(searchBar.getText());
            searchList.getItems().clear();
            searchList.getItems().addAll(searchResults);

        });


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


    @FXML
    private void openSearchList() {
        //searchList.getItems().clear();
        searchPane.toFront();
        searchPane.setVisible(true);

        //
    }

    @FXML
    private void closeSearchList() {
        searchPane.toBack();
        searchPane.setVisible(false);

        //dataHandler.findProducts(searchBar.getText())
    }

    @FXML
    private void search() {
        System.out.println("lolol");
        //searchList.getSelectionModel().getSelectedItem().toString()

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
        for(Node node : mainGrid.getChildren()) {
            Card card = (Card)node;
            if(card.getAmountControl().isVisible()) {
                card.getAmountControl().setVisible(false);
                card.getAddButton().setVisible(true);
                card.getAmountField().setText("1 st");
            }
        }
    }


    //CARDS & CARTITEMS

    protected void decAmount(Card card) { //Needs card as argument
        card.getCartItem().getShoppingItem().setAmount(card.getCartItem().getShoppingItem().getAmount() - 1);
        if(card.getCartItem().getShoppingItem().getAmount() < 1) {
            card.getCartItem().getShoppingItem().setAmount(1);
            removeItem(card);
        }
        card.getAmountField().setText(((int) card.getCartItem().getShoppingItem().getAmount() + " st"));
        card.getCartItem().getAmountField().setText((int) card.getCartItem().getShoppingItem().getAmount() + " st");
        updatePrice(card.getCartItem().getCartItemTotalPrice(), card.getCartItem().getShoppingItem());
    }


    protected void incAmount(Card card) { //doesn't need everything that card has
        card.getCartItem().getShoppingItem().setAmount(card.getCartItem().getShoppingItem().getAmount() + 1);
        if(card.getCartItem().getShoppingItem().getAmount() < 1) { //Maybe redundant
            card.getCartItem().getShoppingItem().setAmount(1);
        }
        card.getAmountField().setText(((int) card.getCartItem().getShoppingItem().getAmount() + " st"));
        card.getCartItem().getAmountField().setText((int) card.getCartItem().getShoppingItem().getAmount() + " st");
        updatePrice(card.getCartItem().getCartItemTotalPrice(), card.getCartItem().getShoppingItem());
    }


    protected void setAmount(Card card, boolean readCard) {
        if(readCard) {
            updateAmounts(card, readAmount(card.getCartItem().getShoppingItem(), card.getAmountField()));
        } else {
            updateAmounts(card, readAmount(card.getCartItem().getShoppingItem(), card.getCartItem().getAmountField()));

        }
        updatePrice(card.getCartItem().getCartItemTotalPrice(), card.getCartItem().getShoppingItem());
    }

    private String readAmount(ShoppingItem shoppingItem, TextField amountField) {//Invalid input doesn't exist with this method.

        try {
            shoppingItem.setAmount(Integer.parseInt(amountField.getText())); //throws NumberFormatException if String contains non-digits
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : amountField.getText().toCharArray()) { //Isolates digits
                if (Character.isDigit(c)) {
                    stringBuilder.append(c);
                }
            }
            String digitString = stringBuilder.toString();
            if (!digitString.isEmpty()) { //if the string is empty (contained no digits), reset amount
                shoppingItem.setAmount(Integer.parseInt(digitString));
            } else {
                shoppingItem.setAmount(shoppingItem.getAmount());
            }
        }
        if (shoppingItem.getAmount() <= 0) {
            return null; //TODO: DANGEROUS TO SET A STRING TO NULL? May fix this in future.
        }
        return ((int) shoppingItem.getAmount()) + " st";
    }

    private void updateAmounts(Card card, String updateString) {
        try {
            if((updateString.equals(null)));
        } catch (NullPointerException e) {
            removeItem(card);
            return;
        }
        card.getCartItem().getAmountField().setText(updateString); // updates cart field
        card.getAmountField().setText(updateString); // updates card field
    }

    private void updatePrice(Label cartItemTotalPrice, ShoppingItem shoppingItem) {
        cartItemTotalPrice.setText(shoppingItem.getTotal() + " kr"); //updates price

    }


    protected void removeItem(Card card) {
        shoppingCartFlowPane.getChildren().remove(card.getCartItem());
        dataHandler.getShoppingCart().getItems().remove(card.getCartItem().getShoppingItem());
        card.getAmountControl().setVisible(false);
        card.getAddButton().setVisible(true);
        card.getAmountField().setText("1 st");
    }

    public void addProductToCart(Card productCard) {//TODO: implement separate method for the duplicate check
        boolean isDuplicate = false;
        for (ShoppingItem si : dataHandler.getShoppingCart().getItems()) {
            if (si.getProduct().equals(productCard.getProduct())) {
                isDuplicate = true;
                break;
            }
        }
        if (!isDuplicate) {//If not duplicate, add to cart
            ShoppingItem item = new ShoppingItem(productCard.getProduct());
            dataHandler.getShoppingCart().addItem(item);

            CartItem cartItem = new CartItem(item, this, productCard);
            productCard.setCartItem(cartItem);
            shoppingCartFlowPane.getChildren().add(cartItem);

            productCard.getAmountControl().setVisible(true);
            productCard.getAmountField().requestFocus();
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
    public void openPreviousPurchases() throws IOException {
        //ResourceBundle bundle = java.util.ResourceBundle.getBundle("designprojekt/resources/Imat");
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("earlierOrders.fxml"));

        previousPurchasesRoot.getChildren().clear();
        EarlierOrdersPage earlierOrdersPage = new EarlierOrdersPage(this);
        previousPurchasesRoot.getChildren().addAll(earlierOrdersPage);

        homePage.toFront();
        previousPurchasesRoot.toFront();

    }

    @FXML
    public void closePreviousPurchases() {

        previousPurchasesRoot.toBack();

    }

    @FXML
    public void openCustomerServicePage() {
        previousPurchasesRoot.getChildren().clear();
        CustomerServicePage customerServicePage = new CustomerServicePage(this);
        previousPurchasesRoot.getChildren().addAll(customerServicePage);

        homePage.toFront();
        previousPurchasesRoot.toFront();

    }

    @FXML
    public void closeCustomerServicePage() {
        previousPurchasesRoot.toBack();

    }

    @FXML
    public void openCheckoutPage() {
        fullscreenPage.getChildren().clear();
        Checkout checkout = new Checkout(this);
        fullscreenPage.getChildren().addAll(checkout);

        homePage.toFront();
        fullscreenPage.toFront();

    }

    @FXML
    public void closeCheckoutPage() {
        fullscreenPage.toBack();

    }

    @FXML
    public void goHome() {
        homePage.toFront();
    }

    public void selectText(TextField textField) {
        textField.selectAll();

    }




    /*public void openStartMenu(){

        startMenu.toFront();
    }*/

}