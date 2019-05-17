package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Checkout extends AnchorPane {
    private Controller parentController;
    @FXML
    private AnchorPane conclusionAnchorPane;
    @FXML
    private AnchorPane varukorgAnchorPane;
    @FXML
    private FlowPane itemsCheckoutFlowpane;

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
    private TextField nameTextField;
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
    private TextField cardBackgroundTextField;
    @FXML
    private TextField fakturaBackgroundTextField;
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
        //set size 100%
        setBottomAnchor(this,0.0);
        setTopAnchor(this,0.0);
        setRightAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);

    }

    @FXML
    public void closeWindow() throws IOException{
        parentController.closeCheckoutPage();

    }

}
