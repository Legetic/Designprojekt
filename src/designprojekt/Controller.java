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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import se.chalmers.cse.dat216.project.*;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.List;

import static se.chalmers.cse.dat216.project.ProductCategory.POD;

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
    private ScrollPane cartScrollPane;
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
    @FXML
    private ScrollPane mainGridScrollPane;
    @FXML
    private Button showMoreButton;

    private Button allProductsButton;

    private List<Product> productsShown;
    int displayAmount = 30;


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

            if(!searchBar.getText().equals("")) {
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

            if(dataHandler.findProducts(searchBar.getText()).isEmpty()){
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
                sort(newValue);
            }
        });


        for(Node d : categoryList.getChildren()){
            if(d instanceof Button){
                if(((Button) d).getText().equals("Alla Produkter")){
                    allProductsButton = (Button) d;
                    currentCategoryButton = (Button) d;
                    currentCategoryButton.getStyleClass().add("currentCategory");
                }

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

    private void sort(String sorttype){
        switch (sorttype) {
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

    private void changeFeature(String category){
        String imagePath = "";
        String headerText = "";
        String description = "";
        switch (category){
            case "Alla Produkter":
                imagePath = "Designprojekt/resources/categoryPictures/StartPage.jpg";
                headerText = "Välkommen!";
                description = "Här hittar du allt du behöver för att laga mat.";
                if(currentCategoryButton != null) {
                    currentCategoryButton.getStyleClass().remove("currentCategory");
                }
                currentCategoryButton = allProductsButton;
                currentCategoryButton.getStyleClass().add("currentCategory");


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
            case "Citrusfrukter":
                imagePath = "Designprojekt/resources/categoryPictures/Citrus.jpg";
                headerText = "Citrusfrukter";
                description = "Allt det där sura.";

                break;
            case "Mjölkprodukter":
                imagePath = "Designprojekt/resources/categoryPictures/milk.jpg";
                headerText = "Mjölkprodukter";
                description = "Ett glas om dagen!";

                break;
            case "Grönsaker":
                imagePath = "Designprojekt/resources/categoryPictures/vegetables.jpg";
                headerText = "Grönsaker";
                description = "Grönt och gott!";

                break;
            case "Kött":
                imagePath = "Designprojekt/resources/categoryPictures/meat.jpg";
                headerText = "Kött";
                description = "Inga veganer här!";

                break;
            case "Kalla Drycker":
                imagePath = "Designprojekt/resources/categoryPictures/coldDrinks.jpg";
                headerText = "Kalla Drycker";
                description = "Kallt och läskande!";

                break;
            case "Varma Drycker":
                imagePath = "Designprojekt/resources/categoryPictures/hotdrinks.jpg";
                headerText = "Varma Drycker";
                description = "Kom in och värm dig!";

                break;
            case "Exotisk Frukt":
                imagePath = "Designprojekt/resources/categoryPictures/exotic.jpg";
                headerText = "Exotisk Frukt";
                description = "Upptäck de okända!";

                break;
            case "Kål":
                imagePath = "Designprojekt/resources/categoryPictures/cole.jpg";
                headerText = "Kål";
                description = "Detta kommer inte ta kål på dig!";

                break;
            case "Meloner":
                imagePath = "Designprojekt/resources/categoryPictures/melons.jpg";
                headerText = "Meloner";
                description = "En på melonen!";

                break;
            case "Örter":
                imagePath = "Designprojekt/resources/categoryPictures/herbs.jpg";
                headerText = "Örter";
                description = "Krydda till vardagen";

                break;
            case "Skafferi":
                imagePath = "Designprojekt/resources/categoryPictures/sugar.jpg";
                headerText = "Skafferi";
                description = "Allt för ditt bakande";

                break;
            case "Nötter & Frön":
                imagePath = "Designprojekt/resources/categoryPictures/nuts.jpg";
                headerText = "Nötter & Frön";
                description = "Varning: kan innehålla nötter!";

                break;
            case "Sötsaker":
                imagePath = "Designprojekt/resources/categoryPictures/candy.jpg";
                headerText = "Sötsaker";
                description = "Söt som dig";

                break;
            case "Frukt":
                imagePath = "Designprojekt/resources/categoryPictures/fruit.jpg";
                headerText = "Frukt";
                description = "Frukt är naturens godis";

                break;
            case "Fisk":
                imagePath = "Designprojekt/resources/categoryPictures/fish.jpg";
                headerText = "Fisk";
                description = "Direkt från sjön";

                break;
            case "Pasta":
                imagePath = "Designprojekt/resources/categoryPictures/pasta.jpg";
                headerText = "Pasta";
                description = "Pasta la vista baby!";

                break;
            case "Potatis & Ris":
                imagePath = "Designprojekt/resources/categoryPictures/potatoes.jpg";
                headerText = "Potatis & Ris";
                description = "Glöm inte dina kolhydrater!";

                break;

            case "Rotfrukter":
                imagePath = "Designprojekt/resources/categoryPictures/root.jpg";
                headerText = "Rotfrukter";
                description = "Låt oss inte utrotas!";

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
        showMoreButton.setVisible(true);

        if(displayAmount > productList.size()){
            displayAmount = productList.size();
        }

        for(int i = 0; i < displayAmount; i ++){
                Product r = productList.get(i);
                Card productCard = cardMap.get(r.getName());
                mainGrid.getChildren().add(productCard);
        }

        if(displayAmount == productList.size()){
            showMoreButton.setVisible(false);
        }


    }
    @FXML
    private void showMoreProducts(){
        displayAmount += 21;
        updateMainGrid(productsShown);
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
        displayAmount = 30;
        currentCategoryButton.getStyleClass().remove("currentCategory");

        List<Product> searchResult = new ArrayList<>();
        String searchString = "";
        if (searchList.getSelectionModel().getSelectedItem() == null) {
            if(!searchBar.getText().equals("")) {
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
        if (searchString.equals("")) {
            updateMainGrid(imatBackendController.getProducts());
            changeFeature("Alla Produkter");
        } else if (searchResult.size() == 0) {
            //INGA SÖKRESULTAT
            setFeature("Designprojekt/resources/categoryPictures/Search.jpg", "Inga sökresultat.", "Tyvärr hittades inga produkter...");
            updateMainGrid(new ArrayList<Product>());

        } else {
            updateMainGrid(searchResult);
            setFeature("Designprojekt/resources/categoryPictures/Search.jpg", "Visar: " + searchString, "Hittade: " + searchResult.size() + " Sökresultat.");


        }
        sort(sortList.getSelectionModel().getSelectedItem().toString());

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
            return null;
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
            updateCheckout();
        } else {
            totalPrice = Math.round(totalPrice * 100.0) / 100.0; //rounds off cause of bug
            totalPriceLabel.setText(totalPrice + " kr");
            updateCheckout();
        }
    }

    private void updateCheckout() {
        try {
            checkout.updateTotalPrice();
            checkout.updateAmount();
        } catch (NullPointerException e) {
            //checkout not active
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

    public void addProductToCart(Card productCard) {
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
            //cartScrollPane.setVvalue(1);
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


    public void addProductFromOrderToCart(ShoppingItem item) {
        Card productCard = cardMap.get(item.getProduct().getName());
        boolean isDuplicate = false;
        for (ShoppingItem si : dataHandler.getShoppingCart().getItems()) { //If item exists in cart, add amount of item to cart
            if (si.getProduct().equals(productCard.getProduct())) { //
                si.setAmount(item.getAmount() + si.getAmount());
                isDuplicate = true;
                break;
            }
        }
        if (!isDuplicate) {//If not duplicate, add to cart
            dataHandler.getShoppingCart().addItem(item);

            CartItem cartItem = new CartItem(item, this, productCard);
            productCard.setCartItem(cartItem);
            shoppingCartFlowPane.getChildren().add(cartItem);

            productCard.getAmountControl().setVisible(true);
            productCard.getAmountField().requestFocus();
            productCard.getAddButton().setVisible(false);
            //cartScrollPane.setVvalue(1);
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
        earlierOrdersPage.getChooseOrderLabel().setVisible(true);
        earlierOrdersPage.getAddPreviousPurchaseToCartButton().setDisable(true);
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
        updateShoppingCart();
        //updateCart();
    }



    public void selectText(TextField textField) {
        textField.selectAll();
    }



    @FXML
    public void showPOD() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.POD));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showBREAD() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.BREAD));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showBERRY() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.BERRY));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showCITRUS() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.CITRUS_FRUIT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showHOT_DRINKS() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.HOT_DRINKS));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showCOLD_DRINKS() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.COLD_DRINKS));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showEXOTIC() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.EXOTIC_FRUIT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showFISH() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.FISH));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showVEGETABLE_FRUIT() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.VEGETABLE_FRUIT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showCABBAGE() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.CABBAGE));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showMEAT() { //meat and fish????!!!
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.MEAT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showDAIRIES() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.DAIRIES));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showMELONS() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.MELONS));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showFLOUR_SUGAR_SALT() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.FLOUR_SUGAR_SALT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());

    }

    @FXML
    public void showNUTS_AND_SEEDS() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.NUTS_AND_SEEDS));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showPASTA() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.PASTA));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showPOTATO_RICE() { // and pasta???!!
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.POTATO_RICE));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showROOT_VEGETABLE() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.VEGETABLE_FRUIT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showFRUIT() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.FRUIT));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showSWEET() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.SWEET));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }

    @FXML
    public void showHERB() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts(ProductCategory.HERB));
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }
    @FXML
    public void showAll() {
        displayAmount = 30;
        updateMainGrid(imatBackendController.getProducts());
        sort(sortList.getSelectionModel().getSelectedItem().toString());
        mainGridScrollPane.setVvalue(0);
    }


    /*public void openStartMenu(){

        startMenu.toFront();
    }*/

}