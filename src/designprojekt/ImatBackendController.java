package designprojekt;

import javafx.scene.image.Image;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.List;

public class ImatBackendController {

    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    public List<Product> getProducts(){
        return dataHandler.getProducts();
    }
    public List<Product> getProducts(ProductCategory pc){
        return dataHandler.getProducts(pc);
    }



    public List<Order> getOrders(){
        return dataHandler.getOrders();
    }

}
