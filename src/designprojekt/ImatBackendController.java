package designprojekt;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.util.List;

public class ImatBackendController {

    private IMatDataHandler dataHandler = IMatDataHandler.getInstance();

    public List<Product> getProducts(){
        return dataHandler.getProducts();
    }
}
