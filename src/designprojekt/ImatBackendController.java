package designprojekt;

import javafx.scene.image.Image;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;
import se.chalmers.cse.dat216.project.*;

import java.util.ArrayList;
import java.util.List;

public class ImatBackendController {

    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    public List<Product> getProducts() {
        return dataHandler.getProducts();
    }

    public List<Product> getProducts(ProductCategory pc) {
        return dataHandler.getProducts(pc);
    }

    public User getUser() {
        return dataHandler.getUser();
    }

    public Customer getCustomer() {
        return dataHandler.getCustomer();
    }

    public CreditCard getCreditCard() {
        return dataHandler.getCreditCard();
    }

    public ShoppingCart getShoppingCart() {
        return dataHandler.getShoppingCart();
    }

    public boolean isCustomerComplete() {
        return dataHandler.isCustomerComplete();
    }

    public Order placeOrder() {
        return this.placeOrder(true);
    }

    public Order placeOrder(boolean clearShoppingCart) {
        return dataHandler.placeOrder(clearShoppingCart);
    }

    public List<Order> getOrders() {
        return dataHandler.getOrders();
    }

    public Image getFXImage(Product p, double requestedWidth, double requestedHeight, boolean preserveRatio){
        return dataHandler.getFXImage(p,requestedWidth,requestedHeight,preserveRatio);
    }

}
