package designprojekt;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.Text;
import se.chalmers.cse.dat216.project.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Checkout extends AnchorPane {

    private Controller parentController;
    @FXML
    private AnchorPane conclusionAnchorPane;
    @FXML
    private AnchorPane varukorgAnchorPane;
    @FXML
    public FlowPane itemsCheckoutFlowpane;

    @FXML
    private AnchorPane deliveryAnchorPane;
    @FXML
    private TextField firstNameDeliveryTextField;
    @FXML
    private TextField lastNameDeliveryTextField;
    @FXML
    private TextField adressDeliveryTextField;
    @FXML
    private TextField postCodeDeliveryTextField;
    @FXML
    private TextField cityDeliveryTextField;

    @FXML
    private AnchorPane userAnchorPane;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField phonenumberTextField;
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField adressTextField;
    @FXML
    private TextField postCodeTextField;
    @FXML
    private TextField postAddressTextField;

    @FXML
    private AnchorPane paymentAnchorPane;
    @FXML
    private AnchorPane cardBackgroundAnchorPane;
    @FXML
    private AnchorPane fakturaBackgroundAnchorPane;
    @FXML
    private RadioButton cardRadioButton;
    @FXML
    private RadioButton fakturaRadioButton;
    @FXML
    private AnchorPane cardInfoAnchorPane;
    @FXML
    private TextField cardOwnerTextField;
    @FXML
    private TextField cardNumberTextField;
    @FXML
    private TextField monthTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField cvcTextField;

    @FXML
    private AnchorPane fakturaInfoAnchorPane;
    @FXML
    private TextField nameFakturaTextField;
    @FXML
    private TextField personalNumberTextField;
    @FXML
    private TextField adressFakturaTextField;
    @FXML
    private TextField postCodeFakturaTextField;
    @FXML
    private TextField stadFakturaTextField;

    @FXML
    private Circle firstStepIndicator;
    @FXML
    private Circle secondStepIndicator;
    @FXML
    private Circle thirdStepIndicator;
    @FXML
    private Circle fourthStepIndicator;
    @FXML
    private Rectangle secondStepLine;
    @FXML
    private Rectangle thirdStepLine;
    @FXML
    private Rectangle fourthStepLine;
    @FXML
    private Label firstStepLabel;
    @FXML
    private Label secondStepLabel;
    @FXML
    private Label thirdStepLabel;
    @FXML
    private Label fourthStepLabel;

    List<Node> sequenceMapParts = new ArrayList<>();


    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button backFromCheckoutButton;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label checkoutAmount;
    @FXML
    private Label errorMessage;

    @FXML
    private AnchorPane orderFinishedPanel;

    private int state = 0;
    private CreditCard creditCard;
    private Customer customer;
    private boolean orderComplete = false;
    private static String method = "Card";


    List<TextField> userFields = new ArrayList<TextField>();
    List<TextField> deliveryFields = new ArrayList<TextField>();
    List<TextField> paymentCardFields = new ArrayList<TextField>();
    List<TextField> paymentFakturaFields = new ArrayList<TextField>();


    public Checkout(Controller parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("checkout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        creditCard = parentController.imatBackendController.getCreditCard();
        customer = parentController.imatBackendController.getCustomer();
        //set size 100%
        setBottomAnchor(this, 0.0);
        setTopAnchor(this, 0.0);
        setRightAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);

        itemsCheckoutFlowpane.getChildren().clear();
        itemsCheckoutFlowpane.getChildren().addAll(parentController.shoppingCartFlowPane.getChildren());

        updateTotalPrice();
        updateAmount();

        loadUserPage();
        loadDeliveryPage();
        loadPaymentPage();


        ToggleGroup paymentMethod = new ToggleGroup();
        cardRadioButton.setToggleGroup(paymentMethod);
        fakturaRadioButton.setToggleGroup(paymentMethod);
        cardRadioButton.setSelected(true);

//what to be selected at first

        try {
            FileInputStream reader=new FileInputStream("config.properties");
            Properties p = new Properties();
            p.load(reader);
            reader.close();

            //System.out.println(p.getProperty("paymentMethod"));

            if( p.getProperty("paymentMethod").equals("Card")){
                cardRadioButton.setSelected(true);
                cardInfoAnchorPane.setVisible(true);
                fakturaInfoAnchorPane.setVisible(false);
            }else{
                fakturaRadioButton.setSelected(true);
                cardInfoAnchorPane.setVisible(false);
                fakturaInfoAnchorPane.setVisible(true);
            }
            //System.out.println(p.getProperty("paymentMethod"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        paymentMethod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (paymentMethod.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) paymentMethod.getSelectedToggle();
                    if (selected == cardRadioButton) {
                        method = "Card";
                        cardInfoAnchorPane.setVisible(true);
                        fakturaInfoAnchorPane.setVisible(false);
                    } else {
                        method = "Faktura";
                        cardInfoAnchorPane.setVisible(false);
                        fakturaInfoAnchorPane.setVisible(true);
                    }
                    savePrefs();
                }
            }
        });





        userFields.add(lastNameTextField);
        userFields.add(firstNameTextField);
        userFields.add(phonenumberTextField);
        userFields.add(mailTextField);
        userFields.add(adressTextField);
        userFields.add(postCodeTextField);
        userFields.add(postAddressTextField);

        deliveryFields.add(postCodeDeliveryTextField);
        deliveryFields.add(cityDeliveryTextField);
        deliveryFields.add(adressDeliveryTextField);
        deliveryFields.add(firstNameDeliveryTextField);
        deliveryFields.add(lastNameDeliveryTextField);

        paymentCardFields.add(cardOwnerTextField);
        paymentCardFields.add(cardNumberTextField);
        paymentCardFields.add(monthTextField);
        paymentCardFields.add(yearTextField);

        paymentFakturaFields.add(adressFakturaTextField);
        paymentFakturaFields.add(postCodeFakturaTextField);
        paymentFakturaFields.add(nameFakturaTextField);
        paymentFakturaFields.add(stadFakturaTextField);
        paymentFakturaFields.add(personalNumberTextField);

        for (TextField t : userFields) {
            t.textProperty().addListener((observable, oldValue, newValue) -> {
                t.getStyleClass().remove("inputFieldError");
            });
        }
        for (TextField t : deliveryFields) {
            t.textProperty().addListener((observable, oldValue, newValue) -> {
                t.getStyleClass().remove("inputFieldError");
            });
        }
        for (TextField t : paymentFakturaFields) {
            t.textProperty().addListener((observable, oldValue, newValue) -> {
                t.getStyleClass().remove("inputFieldError");
            });
        }


        sequenceMapParts.add(firstStepIndicator);
        sequenceMapParts.add(secondStepIndicator);
        sequenceMapParts.add(thirdStepIndicator);
        sequenceMapParts.add(fourthStepIndicator);
        sequenceMapParts.add(secondStepLine);
        sequenceMapParts.add(thirdStepLine);
        sequenceMapParts.add(fourthStepLine);
        sequenceMapParts.add(firstStepLabel);
        sequenceMapParts.add(secondStepLabel);
        sequenceMapParts.add(thirdStepLabel);
        sequenceMapParts.add(fourthStepLabel);

        updateSequenceMap();

    }

    private void savePrefs(){


        try {

            FileInputStream reader=new FileInputStream("config.properties");
            Properties p = new Properties();
            p.load(reader);
            p.setProperty("paymentMethod",method);
            reader.close();

            FileOutputStream fos = new FileOutputStream("config.properties");
            p.store(fos, "Writing properties to a file");
            fos.close();


            //System.out.println(p.getProperty("paymentMethod"));




            //System.out.println(p.getProperty("paymentMethod"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserPage() {
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        phonenumberTextField.setText(customer.getPhoneNumber());
        mailTextField.setText(customer.getEmail());
        adressTextField.setText(customer.getAddress());
        postCodeTextField.setText(customer.getPostCode());
        postAddressTextField.setText(customer.getPostAddress());
    }

    private void loadDeliveryPage() {
        firstNameDeliveryTextField.setText(customer.getFirstName());
        lastNameDeliveryTextField.setText(customer.getLastName());
        adressDeliveryTextField.setText(customer.getAddress());
        postCodeDeliveryTextField.setText(customer.getPostCode());
        cityDeliveryTextField.setText(customer.getPostAddress());
    }

    private void loadPaymentPage() {
        cardOwnerTextField.setText(creditCard.getHoldersName());
        cardNumberTextField.setText(creditCard.getCardNumber());
        monthTextField.setText(creditCard.getValidMonth() + "");
        yearTextField.setText(creditCard.getValidYear() + "");
        cvcTextField.setText(creditCard.getVerificationCode() + "");


        nameFakturaTextField.setText(customer.getFirstName() + " " + customer.getLastName());
        adressFakturaTextField.setText(customer.getAddress());
        postCodeFakturaTextField.setText(customer.getPostCode());
        stadFakturaTextField.setText(customer.getPostAddress());
        //fakturan kan ej sparas
    }


    @FXML
    public void closeWindow() throws IOException {
        parentController.shoppingCartFlowPane.getChildren().addAll(itemsCheckoutFlowpane.getChildren());
        parentController.closeCheckoutPage();
        if (orderComplete) {
            parentController.clearShoppingCart();
        }
        parentController.updateShoppingCart();
    }

    @FXML
    public void nextCheckoutPage() {
        saveInfo();
        switch (state) {
            case 0:
                openUserPage();
                break;
            case 1:
                if (isUserComplete()) {
                    errorMessage.setVisible(false);
                    openDeliveryPage();
                } else {
                    errorMessage.setVisible(true);
                    checkUserErrors();
                }
                //System.out.println("HEJ PÅ DIG");

                break;
            case 2:
                if (isDeliveryComplete()) {
                    errorMessage.setVisible(false);
                    openPaymentPage();
                } else {
                    errorMessage.setVisible(true);
                    checkDeliveryErrors();
                }
                //System.out.println("HEJ PÅ DIG igen");
                break;
            case 3:
                // if(parentController.imatBackendController.isCustomerComplete()){
                if (isPaymentComplete() && parentController.imatBackendController.isCustomerComplete()) {
                    errorMessage.setVisible(false);

                    parentController.imatBackendController.placeOrder();

                    orderFinishedPanel.toFront();
                    backFromCheckoutButton.toFront(); //TODO: Behövs denna?
                    orderComplete = true;
                } else {
                    errorMessage.setVisible(true);
                    checkPaymentErrors();
                }
                // }
                //System.out.println("HEJ PÅ DIG igen");
                break;

        }

        previousButton.setDisable(false);

    }

    @FXML
    public void previousCheckoutPage() {
        saveInfo();
        switch (state) {
            case 0:


                break;
            case 1:

                openCartPage();
                errorMessage.setVisible(false);
                checkDeliveryErrors();
                //System.out.println("HEJ PÅ DIG");
                previousButton.setDisable(true);

                break;
            case 2:
                openUserPage();
                errorMessage.setVisible(false);
                checkDeliveryErrors();

                //System.out.println("HEJ PÅ DIG igen");
                break;
            case 3:
                // if(parentController.imatBackendController.isCustomerComplete()){
                openDeliveryPage();
                errorMessage.setVisible(false);
                checkDeliveryErrors();
                // }
                //System.out.println("HEJ PÅ DIG igen");
                break;

        }

    }

    private void updateSequenceMap() {

        for (Node n : sequenceMapParts) {
            n.getStyleClass().remove("activeIndicator");
            n.getStyleClass().remove("activeIndicatorLine");
            n.getStyleClass().remove("activeIndicatorText");
        }

        switch (state) {
            case 0:
                firstStepIndicator.getStyleClass().add("activeIndicator");
                firstStepLabel.getStyleClass().add("activeIndicatorText");
                break;
            case 1:
                secondStepIndicator.getStyleClass().add("activeIndicator");
                secondStepLabel.getStyleClass().add("activeIndicatorText");
                secondStepLine.getStyleClass().add("activeIndicatorLine");

                break;
            case 2:
                thirdStepIndicator.getStyleClass().add("activeIndicator");
                thirdStepLabel.getStyleClass().add("activeIndicatorText");
                secondStepLine.getStyleClass().add("activeIndicatorLine");
                thirdStepLine.getStyleClass().add("activeIndicatorLine");
                break;
            case 3:
                fourthStepIndicator.getStyleClass().add("activeIndicator");
                fourthStepLabel.getStyleClass().add("activeIndicatorText");
                fourthStepLine.getStyleClass().add("activeIndicatorLine");
                thirdStepLine.getStyleClass().add("activeIndicatorLine");
                secondStepLine.getStyleClass().add("activeIndicatorLine");
                break;
        }
    }

    private boolean isPaymentComplete() {
        boolean isComplete = true;
        if (cardRadioButton.isSelected()) {
            for (TextField t : paymentCardFields) {
                if (t.getText().equals("")) {
                    isComplete = false;
                }
            }
        } else {
            for (TextField t : paymentFakturaFields) {
                if (t.getText().equals("")) {
                    isComplete = false;
                }
            }
        }

        return isComplete;
    }

    private boolean isDeliveryComplete() {
        boolean isComplete = true;
        for (TextField t : deliveryFields) {
            if (t.getText().equals("")) {
                isComplete = false;
            }
        }
        return isComplete;
    }

    private boolean isUserComplete() {
        boolean isComplete = true;
        for (TextField t : userFields) {
            if (t.getText().equals("")) {
                isComplete = false;
            }
        }
        return isComplete;
    }

    private void checkUserErrors() {

        for (TextField t : userFields) {
            t.getStyleClass().remove("inputFieldError");
            if (t.getText().equals("")) {
                t.getStyleClass().add("inputFieldError");
                t.setPromptText("Fältet får ej vara tomt");

            }
        }
    }


    void updateAmount() {
        checkoutAmount.setText(itemsCheckoutFlowpane.getChildren().size() + " st");
    }
    void updateTotalPrice() {
        double totalPrice = 0.0;
        for (Node node : itemsCheckoutFlowpane.getChildren()) {
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
        /*double totalPrice = Math.round(parentController.imatBackendController.getShoppingCart().getTotal() * 100.0) / 100.0; //rounds off price because of previous bug
        totalPriceLabel.setText(totalPrice + " kr"); //updates price*/
    }



    private void checkPaymentErrors() {


        if (cardRadioButton.isSelected()) {

            for (TextField t : paymentCardFields) {
                t.getStyleClass().remove("inputFieldError");
                if (t.getText().equals("")) {
                    t.getStyleClass().add("inputFieldError");
                    t.setPromptText("Fältet får ej vara tomt");

                }
            }
        } else {

            for (TextField t : paymentFakturaFields) {
                t.getStyleClass().remove("inputFieldError");
                if (t.getText().equals("")) {
                    t.getStyleClass().add("inputFieldError");
                    t.setPromptText("Fältet får ej vara tomt");

                }
            }

        }


    }

    private void checkDeliveryErrors() {

        for (TextField t : deliveryFields) {
            t.getStyleClass().remove("inputFieldError");

            if (t.getText().equals("")) {
                t.getStyleClass().add("inputFieldError");
                t.setPromptText("Fältet får ej vara tomt");

            }
        }
    }


    private void openPaymentPage() {
        state = 3;
        loadPaymentPage();
        updateSequenceMap();
        paymentAnchorPane.toFront();
        nextButton.setText("Slutför");

    }

    private void openDeliveryPage() {
        state = 2;
        loadDeliveryPage();
        updateSequenceMap();
        deliveryAnchorPane.toFront();
        nextButton.setText("Fortsätt");


    }

    private void openCartPage(){
        state = 0;
        updateSequenceMap();
        varukorgAnchorPane.toFront();
        nextButton.setText("Fortsätt");
    }

    private void openUserPage() {
        state = 1;
        loadUserPage();
        updateSequenceMap();
        userAnchorPane.toFront();
        nextButton.setText("Fortsätt");

    }

    private void saveInfo() {
        switch (state) {
            case 0:
                //finns inget att spara
                break;
            case 1:

                customer.setFirstName(firstNameTextField.getText());
                customer.setLastName(lastNameTextField.getText());
                customer.setEmail(mailTextField.getText());
                customer.setAddress(adressTextField.getText());
                customer.setPhoneNumber(phonenumberTextField.getText());
                customer.setPostCode(postCodeTextField.getText());
                customer.setPostAddress(postAddressTextField.getText());

                break;
            case 2:
                //kan inte spara någon deliveryinfo

                break;
            case 3:

                creditCard.setCardNumber(cardNumberTextField.getText());
                creditCard.setHoldersName(cardOwnerTextField.getText());
                creditCard.setValidMonth(Integer.valueOf(monthTextField.getText()));
                creditCard.setValidYear(Integer.valueOf(yearTextField.getText()));
                creditCard.setVerificationCode(Integer.valueOf(cvcTextField.getText()));

                //kan inte spara fakturan

                break;


        }
    }


}
