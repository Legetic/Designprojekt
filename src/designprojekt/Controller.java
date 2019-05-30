package designprojekt;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import se.chalmers.cse.dat216.project.*;


import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Controller implements Initializable {

    ImatBackendController imatBackendController = new ImatBackendController();
    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    private Button previousSelectedCategoryButton;

    @FXML
    private VBox categoryList;

    @FXML
    private AnchorPane startMenu;
    @FXML
    private BorderPane homePage;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Button goToCheckoutButton;


    @FXML
    private FlowPane mainGrid;
    @FXML
    private AnchorPane mainGridCategoryImage;
    @FXML
    private Button startPage_exit_btn;
    @FXML
    private Button startPage_home_btn;
    @FXML
    private Button startPage_recentBuy_btn;
    @FXML
    private Button addButton;

    @FXML
    private Button emptyButton;
    @FXML
    private ImageView emptyIcon;
    @FXML
    private AnchorPane emptyPrompt;

    @FXML
    public FlowPane shoppingCartFlowPane;
    @FXML
    private ComboBox sortList;
    @FXML
    private Label shoppingCartEmptyLabel;

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

    private Button currentCategoryButton;

    @FXML
    private Label featureHeader;
    @FXML
    private Label featureText;


    private List<Product> productsShown;


    private Checkout checkout;
    private double totalPrice = 0;

    //private List<Product> productList = new ArrayList<>();
    private List<ShoppingItem> cartList = new ArrayList<>();

    private Map<String, Card> cardMap = new HashMap<String, Card>();
    private Map<String, ShoppingItem> shoppingItemMap = new HashMap<String, ShoppingItem>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        for (Product item : imatBackendController.getProducts()) {
            Card productCard = new Card(item, this);
            cardMap.put(item.getName(), productCard);
        }

        searchBar.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN) {
                searchList.requestFocus();
                searchList.getFocusModel().focusNext();
                //searchList.getSelectionModel().selectNext();
            }
            if (event.getCode() == KeyCode.UP) {
                searchList.getSelectionModel().selectPrevious();
            }

            if (event.getCode() == KeyCode.ENTER) {
                search();
                closeSearchList();

            }

        });
        searchList.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                search();
                closeSearchList();

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

            if(searchBar.getText() != "") {
                openSearchList();

                List<String> searchResults = new ArrayList<String>();
                for (Product product : dataHandler.findProducts(searchBar.getText())) {
                    searchResults.add(product.getName());
                }

                //dataHandler.findProducts(searchBar.getText());

                searchList.getItems().clear();

            /*for(Product p : searchResults){
                int searchStringStart = p.getName().indexOf(searchBar.getText());
                int searchStringEnd = p.getName().indexOf(searchBar.getText()) + searchBar.getText().length();

                Text text1 = new Text(p.getName().substring(0,searchStringStart ));
                text1.setFill(Color.web("19466B"));
                Text text2 = new Text(p.getName().substring(searchStringStart,searchStringEnd ));
                text2.setFill(Color.web("3599EB"));
                Text text3 = new Text(p.getName().substring(searchStringEnd));
                text3.setFill(Color.web("19466B"));

                TextFlow textFlow = new TextFlow(text1, text2, text3);

                searchList.getItems().addAll(textFlow);
            }*/
                searchList.getItems().addAll(searchResults);
            }else{
                searchList.getItems().clear();
                closeSearchList();
            }

        });

        shoppingCartFlowPane.getChildren().addListener(new ListChangeListener<Node>() { // if shoppingCart is empty, show "Shopping Cart Is Empty" text
            @Override
            public void onChanged(Change<? extends Node> change) {
                checkCart();
            }
        });


        sortList.getItems().addAll("Ingen Sortering", "Lägsta Pris", "Högsta Pris");
        sortList.getSelectionModel().select("Ingen Sortering");
        sortList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (newValue) {
                    case "Ingen Sortering":
                        Collections.sort(productsShown, new idComparator());
                        updateMainGrid(productsShown);
                        break;
                    case "Högsta Pris":
                        Collections.sort(productsShown, new highestPriceComparator());
                        updateMainGrid(productsShown);
                        break;
                    case "Lägsta Pris":
                        Collections.sort(productsShown, new lowestPriceComparator());
                        updateMainGrid(productsShown);
                        break;

                }
            }
        });


        for(Node d : categoryList.getChildren()){
            if(d instanceof Button){
                d.setOnMouseClicked(ev -> {
                    //System.out.println(((Button) d).getText());
                    if(currentCategoryButton != null) {
                        currentCategoryButton.getStyleClass().remove("currentCategory");
                    }
                    currentCategoryButton = (Button) d;
                    currentCategoryButton.getStyleClass().add("currentCategory");
                    changeFeature(((Button) d).getText());

                });
            }
        }


        changeFeature("Alla Produkter");
        populateSortComboBox();
        updateMainGrid(imatBackendController.getProducts());
        Collections.sort(productsShown, new idComparator());
        updateShoppingCart();


    }

    private void changeFeature(String category){
        String imagePath = "";
        String headerText = "";
        String description = "";
        switch (category){
            case "Alla Produkter":
                imagePath = "Designprojekt/resources/categoryPictures/StartPage.jpg";
                headerText = "Välkommen!";
                description = "Här hittar du allt du behöver för att laga mat.";

                break;
            case "Baljväxter":
                    imagePath = "Designprojekt/resources/categoryPictures/Pod.jpg";
                    headerText = "Baljväxter";
                    description = "Allt från bönor till ärtor.";

                break;
            case "Bröd":
                imagePath = "Designprojekt/resources/categoryPictures/Bread.jpg";
                headerText = "Bröd";
                description = "Nybakat var det här!";

                break;
            case "Bär":
                imagePath = "Designprojekt/resources/categoryPictures/berry.jpg";
                headerText = "Bär";
                description = "Små och söta.";

                break;
            case "Citrus Frukter":
                imagePath = "Designprojekt/resources/categoryPictures/Citrus.jpg";
                headerText = "Citrus Frukter";
                description = "Allt det där sura.";

                break;



        }
        setFeature(imagePath, headerText, description);
    }
    private void setFeature(String imagePath, String headerText, String description){

        mainGridCategoryImage.setStyle("-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-position: center; " + "-fx-background-size: cover");
        featureHeader.setText(headerText);
        featureText.setText(description);
    }

    private void checkCart() {
        if (shoppingCartFlowPane.getChildren().isEmpty()) {
            shoppingCartEmptyLabel.setVisible(true);
            goToCheckoutButton.setDisable(true);
            emptyButton.setDisable(true);
        } else {
            shoppingCartEmptyLabel.setVisible(false);
            goToCheckoutButton.setDisable(false);
            emptyButton.setDisable(false);
        }
    }


    private void populateSortComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {

                                switch (item) {

                                    case "Ingen Sortering":
                                        iconPath = "Designprojekt/resources/icons/MainBlue/sorting_arrows_32px.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Lägsta Pris":
                                        iconPath = "Designprojekt/resources/icons/MainBlue/generic_sorting_2_32px.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Högsta Pris":
                                        iconPath = "Designprojekt/resources/icons/MainBlue/generic_sorting_32px.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    default:
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                System.out.println("pop");

                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(22);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };

        sortList.setButtonCell(cellFactory.call(null));
        sortList.setCellFactory(cellFactory);
    }


    class lowestPriceComparator implements Comparator<Product> {
        public int compare(Product p1, Product p2) {
            return (((int) (p1.getPrice() * 100)) - ((int) (p2.getPrice() * 100))) / 100;
        }
    }

    class highestPriceComparator implements Comparator<Product> {
        public int compare(Product p1, Product p2) {
            return (((int) (p2.getPrice() * 100)) - ((int) (p1.getPrice() * 100))) / 100;
        }
    }

    class idComparator implements Comparator<Product> {
        public int compare(Product p1, Product p2) {
            return p1.getProductId() - p2.getProductId();
        }
    }


    private void updateMainGrid(List<Product> productList) {
        productsShown = productList;
        mainGrid.getChildren().clear();
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
        List<Product> searchResult = new ArrayList<>();
        String searchString = "";
        if (searchList.getSelectionModel().getSelectedItem() == null) {
            if(searchBar.getText() != "") {
                searchString = searchBar.getText();
                searchResult = dataHandler.findProducts(searchString);
            }else{
                searchResult = imatBackendController.getProducts();
            }

        } else {
            searchString = searchList.getSelectionModel().getSelectedItem().toString();
            int startIndex = 0;
            for (int i = 0; i < searchString.length(); i++) {
                char c = searchString.charAt(i);
                if (Character.isLetter(c)) {
                    startIndex = i;
                    break;
                }
            }

            searchResult = dataHandler.findProducts(searchString.substring(startIndex));

        }
        if (searchString == "") {
            updateMainGrid(imatBackendController.getProducts());
        } else if (searchResult.size() == 0) {
            //INGA SÖKRESULTAT
        } else {
            updateMainGrid(searchResult);


        }
        //searchList.getSelectionModel().getSelectedItem().toString()

    }


    //SHOPPING CART
    void updateShoppingCart() {
        shoppingCartFlowPane.getChildren().clear();
        cartList = dataHandler.getShoppingCart().getItems();
        for (ShoppingItem s : cartList) {
            CartItem cartItem = new CartItem(s, this, cardMap.get(s.getProduct().getName()));
            cardMap.get(s.getProduct().getName()).setCartItem(cartItem);
            cardMap.get(s.getProduct().getName()).getAmountField().setText("" + (int) s.getAmount());
            cardMap.get(s.getProduct().getName()).setAmount();

            cardMap.get(s.getProduct().getName()).getAmountControl().setVisible(true);
            cardMap.get(s.getProduct().getName()).getAddButton().setVisible(false);
            shoppingCartFlowPane.getChildren().add(cartItem);
        }
        checkCart();
        updateTotalLabel();
    }

    public void clearShoppingCart() {
        shoppingCartFlowPane.getChildren().clear();
        dataHandler.getShoppingCart().clear();
        for (Node node : mainGrid.getChildren()) {
            Card card = (Card) node;
            if (card.getAmountControl().isVisible()) {
                card.getAmountControl().setVisible(false);
                card.getAddButton().setVisible(true);
                card.getAmountField().setText("1 st");
            }
        }
        closeEmptyPrompt();
        updateTotalLabel();
    }


    //CARDS & CARTITEMS

    protected void decAmount(Card card) { //Needs card as argument
        card.getCartItem().getShoppingItem().setAmount(card.getCartItem().getShoppingItem().getAmount() - 1);
        if (card.getCartItem().getShoppingItem().getAmount() < 1) {
            card.getCartItem().getShoppingItem().setAmount(1);
            removeItem(card);
        }
        card.getAmountField().setText(((int) card.getCartItem().getShoppingItem().getAmount() + " st"));
        card.getCartItem().getAmountField().setText((int) card.getCartItem().getShoppingItem().getAmount() + " st");
        updatePrice(card.getCartItem().getCartItemTotalPrice(), card.getCartItem().getShoppingItem());
    }


    protected void incAmount(Card card) { //doesn't need everything that card has
        card.getCartItem().getShoppingItem().setAmount(card.getCartItem().getShoppingItem().getAmount() + 1);
        if (card.getCartItem().getShoppingItem().getAmount() < 1) { //Maybe redundant
            card.getCartItem().getShoppingItem().setAmount(1);
        }
        card.getAmountField().setText(((int) card.getCartItem().getShoppingItem().getAmount() + " st"));
        card.getCartItem().getAmountField().setText((int) card.getCartItem().getShoppingItem().getAmount() + " st");
        updatePrice(card.getCartItem().getCartItemTotalPrice(), card.getCartItem().getShoppingItem());
    }


    protected void setAmount(Card card, boolean readCard) {
        if (readCard) {
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
                if (Character.isDigit(c) || c == '.') {
                    stringBuilder.append(c);
                }
            }
            String digitString = stringBuilder.toString();
            if (!digitString.isEmpty()) { //if the string is empty (contained no digits), reset amount
                shoppingItem.setAmount((int)(Double.parseDouble(digitString) + 0.5));
            } else {
                shoppingItem.setAmount(shoppingItem.getAmount());
            }
        }
        if (shoppingItem.getAmount() < 1) {
            return null; //TODO: DANGEROUS TO SET A STRING TO NULL? May fix this in future.
        }
        return ((int) shoppingItem.getAmount()) + " st";
    }

    private void updateAmounts(Card card, String updateString) {
        try {
            if ((updateString.equals(null))) ;
        } catch (NullPointerException e) {
            removeItem(card);
            return;
        }
        card.getCartItem().getAmountField().setText(updateString); // updates cart field
        card.getAmountField().setText(updateString); // updates card field
    }

    protected void updatePrice(Label cartItemTotalPrice, ShoppingItem shoppingItem) {
        double totalPrice = Math.round(shoppingItem.getTotal() * 100.0) / 100.0; //rounds off price because of previous bug
        cartItemTotalPrice.setText(totalPrice + " kr"); //updates price
        totalPriceLabel.setText(totalPrice + "kr");
        updateTotalLabel();

    }

    private void updateTotalLabel() {
        totalPrice = 0.0;
        for (Node node : shoppingCartFlowPane.getChildren()) {
            CartItem cartItem = (CartItem) node;
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : cartItem.getCartItemTotalPrice().getText().toCharArray()) { //Isolates digits & '.'
                if (Character.isDigit(c) || c == '.') {
                    stringBuilder.append(c);
                }
            }
            String price = stringBuilder.toString();
            totalPrice += Double.parseDouble(price);
        }
        if (totalPrice == 0.0) {
            totalPriceLabel.setText("0 kr");
        } else {
            totalPrice = Math.round(totalPrice * 100.0) / 100.0; //rounds off cause of bug
            totalPriceLabel.setText(totalPrice + " kr");
        }
    }


    protected void removeItem(Card card) {
        shoppingCartFlowPane.getChildren().remove(card.getCartItem());
        if (checkout != null) {
            if (!checkout.itemsCheckoutFlowpane.getChildren().isEmpty()) {
                checkout.itemsCheckoutFlowpane.getChildren().remove(card.getCartItem());
            }
        }
        dataHandler.getShoppingCart().getItems().remove(card.getCartItem().getShoppingItem());
        card.getAmountControl().setVisible(false);
        card.getAddButton().setVisible(true);
        card.getAmountField().setText("1 st");
        updateTotalLabel();
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
        updatePrice(productCard.getCartItem().getCartItemTotalPrice(), productCard.getCartItem().getShoppingItem()); // changed from updatetotalprice() (change if it fucks things up)
    }

    /*private void addingTheCardToCart(Card productCard){
        ShoppingItem item = new ShoppingItem(productCard.getProduct());
        dataHandler.getShoppingCart().addItem(item);

        CartItem cartItem = new CartItem(item, this, productCard);
        productCard.setCartItem(cartItem);
        shoppingCartFlowPane.getChildren().add(cartItem);

        productCard.getAmountControl().setVisible(true);
        productCard.getAmountField().requestFocus();
        productCard.getAddButton().setVisible(false);
    }*/


    public void addProductFromOrderToCart(ShoppingItem item) {//TODO: implement separate method for the duplicate check
        Card productCard = cardMap.get(item.getProduct().getName());
        boolean isDuplicate = false;
        for (ShoppingItem si : dataHandler.getShoppingCart().getItems()) {
            if (si.getProduct().equals(productCard.getProduct())) { //If item exists in cart, add amount of item to cart
                isDuplicate = true;

                System.out.println("si amount: " + si.getAmount());
                System.out.println("item amount: " + item.getAmount());
                System.out.println("card amount: " + productCard.getCartItem().getShoppingItem().getAmount());
                si.setAmount(item.getAmount() + si.getAmount());

                break;
            }
        }
        if (!isDuplicate) {//If not duplicate, add item to cart
            dataHandler.getShoppingCart().addItem(item);

            CartItem cartItem = new CartItem(item, this, productCard);
            productCard.setCartItem(cartItem);
            shoppingCartFlowPane.getChildren().add(cartItem);

            productCard.getAmountControl().setVisible(true);
            productCard.getAmountField().requestFocus();
            productCard.getAddButton().setVisible(false);

        }
        updateShoppingCart();
        updatePrice(productCard.getCartItem().getCartItemTotalPrice(), productCard.getCartItem().getShoppingItem()); // changed from updatetotalprice() (change if it fucks things up)
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
        updateShoppingCart();


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
        updateShoppingCart();

    }

    @FXML
    public void closeCustomerServicePage() {
        previousPurchasesRoot.toBack();

    }

    @FXML
    public void openCheckoutPage() {
        fullscreenPage.getChildren().clear();
        checkout = new Checkout(this);
        fullscreenPage.getChildren().addAll(checkout);

        homePage.toFront();
        fullscreenPage.toFront();

    }

    @FXML
    public void closeCheckoutPage() {
        fullscreenPage.toBack();

    }

    @FXML
    public void openEmptyPrompt() {
        emptyPrompt.toFront();
        emptyButton.getStyleClass().add("activeEmptyButton");
    }

    @FXML
    public void closeEmptyPrompt() {
        emptyPrompt.toBack();
        emptyButton.getStyleClass().remove("activeEmptyButton");

    }

    @FXML
    public void goHome() {
        homePage.toFront();

        if (checkout != null) {
            if (!checkout.itemsCheckoutFlowpane.getChildren().isEmpty()) {
                shoppingCartFlowPane.getChildren().addAll(checkout.itemsCheckoutFlowpane.getChildren());
            }
        }
    }

    public void selectText(TextField textField) {
        textField.selectAll();
    }



    @FXML
    public void showPOD() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.POD));
    }

    @FXML
    public void showBREAD() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.BREAD));
    }

    @FXML
    public void showBERRY() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.BERRY));
    }

    @FXML
    public void showCITRUS() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.CITRUS_FRUIT));
    }

    @FXML
    public void showHOT_DRINKS() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.HOT_DRINKS));
    }

    @FXML
    public void showCOLD_DRINKS() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.COLD_DRINKS));
    }

    @FXML
    public void showEXOTIC() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.EXOTIC_FRUIT));
    }

    @FXML
    public void showFISH() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.FISH));
    }

    @FXML
    public void showVEGETABLE_FRUIT() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.VEGETABLE_FRUIT));
    }

    @FXML
    public void showCABBAGE() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.CABBAGE));
    }

    @FXML
    public void showMEAT() { //meat and fish????!!!
        updateMainGrid(imatBackendController.getProducts(ProductCategory.MEAT));
    }

    @FXML
    public void showDAIRIES() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.DAIRIES));
    }

    @FXML
    public void showMELONS() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.MELONS));
    }

    @FXML
    public void showFLOUR_SUGAR_SALT() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.FLOUR_SUGAR_SALT));
    }

    @FXML
    public void showNUTS_AND_SEEDS() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.NUTS_AND_SEEDS));
    }

    @FXML
    public void showPASTA() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.PASTA));
    }

    @FXML
    public void showPOTATO_RICE() { // and pasta???!!
        updateMainGrid(imatBackendController.getProducts(ProductCategory.POTATO_RICE));
    }

    @FXML
    public void showROOT_VEGETABLE() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.VEGETABLE_FRUIT));
    }

    @FXML
    public void showFRUIT() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.FRUIT));
    }

    @FXML
    public void showSWEET() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.SWEET));
    }

    @FXML
    public void showHERB() {
        updateMainGrid(imatBackendController.getProducts(ProductCategory.HERB));
    }
    @FXML
    public void showAll() {
        updateMainGrid(imatBackendController.getProducts());
    }




    /*public void openStartMenu(){

        startMenu.toFront();
    }*/

}