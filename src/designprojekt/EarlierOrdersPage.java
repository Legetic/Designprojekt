package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EarlierOrdersPage extends AnchorPane {
    private Controller parentController;
    private Order openedOrder;
    @FXML
    private AnchorPane previousPurchaseAnchorPane;
    @FXML
    private FlowPane flowPanePreviousPurchases;
    @FXML
    private FlowPane flowPanePreviousPurchasesDetails;


    public EarlierOrdersPage(Controller parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("earlierOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        //set size 100%
        setBottomAnchor(this, 0.0);
        setTopAnchor(this, 0.0);
        setRightAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);

        flowPanePreviousPurchases.getChildren().clear();
        for (Order r : parentController.imatBackendController.getOrders()) {
            PreviousPurchase prev = new PreviousPurchase(r, this);
            flowPanePreviousPurchases.getChildren().add(prev);
        }
    }

    public void showPreviousPurchaseDetail(Order order){
        openedOrder = order;
        flowPanePreviousPurchasesDetails.getChildren().clear();
        for (ShoppingItem si : order.getItems()) {
            ItemPreviousPurchase itprev = new ItemPreviousPurchase(si,parentController);
            flowPanePreviousPurchasesDetails.getChildren().add(itprev);
        }
    }

    public Controller getParentController(){
        return parentController;
    }


    public void addOrderToCart(){
        if(openedOrder !=null){
            for(ShoppingItem item : openedOrder.getItems()){
                ShoppingItem duplicateItem = new ShoppingItem(item.getProduct(), item.getAmount()); // defensive copy of ShoppingItem in Order (to avoid changing Order amount)
                parentController.addProductFromOrderToCart(duplicateItem);
            }
        }
    }

    @FXML
    public void closeWindow() throws IOException{
        parentController.closePreviousPurchases();
    }



}
