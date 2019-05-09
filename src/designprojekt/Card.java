package designprojekt;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Card {
    private MainController parentController;

    public Card(MainController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;


    }
}
