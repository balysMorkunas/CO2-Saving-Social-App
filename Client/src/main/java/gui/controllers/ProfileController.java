package gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import gogreenclient.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Person;

import java.io.File;
import java.util.ArrayList;

public class ProfileController {

    double coordinateX;
    double coordinateY;

    @FXML private Label name;

    @FXML private Label points;

    @FXML private Label co2Saved;

    @FXML private Label followers;

    @FXML private Label following;

    @FXML private ImageView profilePicture;

    @FXML private FontAwesomeIcon userIcon;

    @FXML private HBox shadowBox;

    /**
     * Initializes the new screen.
     * @param personId Passes person id on whom it was clicked on.
     */
    @FXML
    public void initialize(int personId) {

        System.out.println(String.valueOf(personId));
        Person person = Application.getPersonById(personId);

        name.setText(person.getFirstName() + " " + person.getLastName());
        points.setText(String.valueOf(person.getPoints()));
        co2Saved.setText(String.valueOf(person.getPoints() / 25));
        ArrayList<Person> followingList = Application.getFollowingListById(personId);
        following.setText(String.valueOf(followingList.size()));
        followers.setText(String.valueOf(Application.getWhoFollowsId(personId)));
        //Image profileImage = new Image(person.getImage());
        //profilePicture.setImage(new Image(person.getImage()));
        if (person.getRealImage() == null) {
            System.out.println("User has no profile image set");
            userIcon.setVisible(true);
        } else if (person.getRealImage() != null) {
            System.out.println("User has a profile image set");
            File imageFile = person.getRealImage();
            Image image = new Image(imageFile.toURI().toString(),
                    155, // requested width
                    155, // requested height
                    true, // preserve ratio
                    true, // smooth rescaling
                    false); //load in background
            profilePicture.setImage(image);
            userIcon.setVisible(false);
        }

    }

    /**
     * Closes a window.
     * @param mouseEvent click event.
     */
    public void close(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.close();

    }

    /**
     * Drags window.
     * @param mouseEvent click event.
     */
    public void dragged(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - coordinateX);
        stage.setY(mouseEvent.getScreenY() - coordinateY);
    }

    /**
     * Gets window coordinates.
     * @param event click event.
     */
    public void pressed(javafx.scene.input.MouseEvent event) {

        coordinateX = event.getSceneX();
        coordinateY = event.getSceneY();

    }

}
