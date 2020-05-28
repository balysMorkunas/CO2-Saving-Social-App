package gui.models;

import gogreenclient.Application;
import gui.controllers.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import model.Person;

import javax.security.auth.message.AuthException;

public class SuggestedCell<P> extends ListCell<Person> {
    //Main HBox.
    HBox hbox = new HBox();
    //Components
    Label label = new Label("");
    Pane pane = new Pane();
    Button followButton = new Button("+");
    String lastItem;
    MainController mainController;

    /**
     * Method for displaying modified cell.
     */
    public SuggestedCell(MainController mainController) {
        super();

        //Add everything to the main HBox.
        hbox.getChildren().addAll(label, pane, followButton);
        HBox.setHgrow(pane, Priority.ALWAYS);
        followButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Person_id: " + lastItem
                        +  " is added to Following (TO BE IMPLEMENTED)");
                try {
                    Application.follow(lastItem, MainController.authKey);
                    mainController.goToSocialPane();
                    mainController.displayLeaderboard();
                } catch (AuthException e) {
                    e.printStackTrace();
                }
            }
        });
        this.mainController = mainController;
    }

    @Override
    public void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);
        setText(null);
        setGraphic(null);
        if (empty || person == null) {
            setGraphic(null);
        } else {
            lastItem = Integer.toString(person.getId());
            label.setText(person.getFirstName() + " " + person.getLastName());
            setGraphic(hbox);
        }
    }
}
