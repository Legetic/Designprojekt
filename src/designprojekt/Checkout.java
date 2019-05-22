package designprojekt;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private Button nextButton;
    @FXML
    private Button backFromCheckoutButton;

    @FXML
    private AnchorPane orderFinishedPanel;

    private int state = 0;
    private CreditCard creditCard ;
    private Customer customer;


    public Checkout(Controller parentController){
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
        setBottomAnchor(this,0.0);
        setTopAnchor(this,0.0);
        setRightAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);


        loadUserPage();
        loadDeliveryPage();
        loadPaymentPage();

        ToggleGroup paymentMethod = new ToggleGroup();
        cardRadioButton.setToggleGroup(paymentMethod);
        fakturaRadioButton.setToggleGroup(paymentMethod);
        cardRadioButton.setSelected(true);
        paymentMethod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (paymentMethod.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) paymentMethod.getSelectedToggle();
                    if(selected == cardRadioButton){
                        cardInfoAnchorPane.setVisible(true);
                        fakturaInfoAnchorPane.setVisible(false);
                    }else{
                        cardInfoAnchorPane.setVisible(false);
                        fakturaInfoAnchorPane.setVisible(true);
                    }
                }
            }
        });

        itemsCheckoutFlowpane.getChildren().clear();
        itemsCheckoutFlowpane.getChildren().addAll(parentController.shoppingCartFlowPane.getChildren());

    }

    private void loadUserPage(){
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        phonenumberTextField.setText(customer.getPhoneNumber());
        mailTextField.setText(customer.getEmail());
        adressTextField.setText(customer.getAddress());
        postCodeTextField.setText(customer.getPostCode());
        postAddressTextField.setText(customer.getPostAddress());
    }
    private void loadDeliveryPage(){
        firstNameDeliveryTextField.setText(customer.getFirstName());
        lastNameDeliveryTextField.setText(customer.getLastName());
        adressDeliveryTextField.setText(customer.getAddress());
        postCodeDeliveryTextField.setText(customer.getPostCode());
        cityDeliveryTextField.setText(customer.getPostAddress());
    }
    private void loadPaymentPage(){
        cardOwnerTextField.setText(creditCard.getHoldersName());
        cardNumberTextField.setText(creditCard.getCardNumber());
        monthTextField.setText(creditCard.getValidMonth() + "");
        yearTextField.setText(creditCard.getValidYear() + "");
        cvcTextField.setText(creditCard.getVerificationCode() + "");


        nameFakturaTextField.setText(customer.getFirstName() + " " + customer.getLastName());
        adressTextField.setText(customer.getAddress());
        postCodeFakturaTextField.setText(customer.getPostCode());
        stadFakturaTextField.setText(customer.getPostAddress());
        //fakturan kan ej sparas
    }


    @FXML
    public void closeWindow() throws IOException{
        parentController.shoppingCartFlowPane.getChildren().addAll(itemsCheckoutFlowpane.getChildren());
        parentController.closeCheckoutPage();

    }

    @FXML
    public void nextCheckoutPage(){
        saveInfo();
        switch (state){
            case 0:
                openUserPage();

                break;
            case 1:
                if(parentController.imatBackendController.isCustomerComplete()) {
                    openDeliveryPage();
                }
                //System.out.println("HEJ PÅ DIG");

                break;
            case 2:
                openPaymentPage();
                //System.out.println("HEJ PÅ DIG igen");
                break;
            case 3:
                if(parentController.imatBackendController.isCustomerComplete()){
                    parentController.imatBackendController.placeOrder();

                    orderFinishedPanel.toFront();
                    backFromCheckoutButton.toFront();
                }
                //System.out.println("HEJ PÅ DIG igen");
                break;

        }

    }
    private void openPaymentPage(){
        state = 3;
        loadPaymentPage();
        paymentAnchorPane.toFront();
        nextButton.setText("Slutför");

    }
    private void openDeliveryPage(){
        state = 2;
        loadDeliveryPage();
        deliveryAnchorPane.toFront();
        nextButton.setText("Fortsätt");


    }
    private void openUserPage(){
        state = 1;
        loadUserPage();
        userAnchorPane.toFront();
        nextButton.setText("Fortsätt");

    }

    private void saveInfo(){
        switch (state){
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
