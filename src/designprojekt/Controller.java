package designprojekt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ImatBackendController imatBackendController = new ImatBackendController();

    @FXML
    private FlowPane mainGrid;
    @FXML
    private Button startPage_recentBuy_btn;
    @FXML
    private Button startPage_home_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //git är lite dumt ibland
        /*for (ShoppingItem item : imatBackendController.getProducts()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }*/


    }


    /*private void populateMainGrid(){

        mainGrid.getChildren().clear();
        recipeList = rbc.getRecipes();
        for (Recipe r : recipeList){
            RecipeListItem recipeListItem = recipeListItemMap.get(r.getName());
            flowPane.getChildren().add(recipeListItem);

        }
    }*/
}