package gui.models;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import gogreenclient.Application;
import gui.controllers.MainController;
import gui.controllers.ProfileController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ActionLog;
import model.Person;

import java.io.File;
import java.io.IOException;
import javax.security.auth.message.AuthException;

public class MainCell<P> extends ListCell<ActionLog> {
    //Main VBox to put other H/VBoxes in.
    VBox main = new VBox();
    //Main HBox.
    HBox hbox = new HBox();
    //Left Side HBox.
    HBox buttonBox = new HBox();
    //For all text HBox.
    VBox textBox = new VBox();
    //Horizontal boss for profile picture and name.
    HBox personBox = new HBox();
    //Components
    Label name = new Label("");
    Label count = new Label("0");
    Label empty = new Label(" ");
    FontAwesomeIcon cheerIcon = new FontAwesomeIcon();
    Button cheerButton = new Button("", cheerIcon);
    Label label = new Label("");
    Pane paneMain = new Pane();
    Pane paneBottom = new Pane();
    Circle circle = new Circle();
    ImageView imageView = new ImageView();
    HBox gap = new HBox();
    int personId;

    /**
     * Class for modifying the look of a cell in the news feed.
     */
    public MainCell() {
        super();

        //Intentional gap after text
        gap.setMinHeight(20);

        circle.setRadius(17);
        circle.setFill(Color.rgb(120, 244, 178));
        personBox.getChildren().addAll(circle, name);
        personBox.setSpacing(3);
        personBox.setStyle("-fx-padding: 5");

        textBox.getChildren().addAll(personBox, empty, label, imageView);
        cheerButton.setStyle("-fx-background-color: #78F4B2;");
        count.setStyle("-fx-padding: 8px;");

        cheerIcon.setIconName("TREE");
        cheerIcon.setSize("1.5em");
        cheerIcon.setFill(Color.valueOf("#505050"));

        name.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass()
                            .getResource("/views/profilescreen.fxml"));
                    Parent root = loader.load();
                    ProfileController profileController = loader.getController();
                    profileController.initialize(personId);
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //ButtonBox configuration, put it in the bottom right corner and up for 15px
        buttonBox.setStyle("-fx-background-color: #78F4B2;");
        buttonBox.getChildren().addAll(cheerButton, paneBottom, count);

        //Add everything to the main HBox.
        hbox.getChildren().addAll(textBox, paneMain);
        hbox.setStyle("-fx-background-color: #ADFAD1");
        main.setStyle("-fx-background-color: #ADFAD1");
        main.getChildren().addAll(hbox, paneMain, gap, buttonBox);
        main.setVgrow(paneMain, Priority.ALWAYS);
        buttonBox.setHgrow(paneBottom, Priority.ALWAYS);
    }

    @Override
    public void updateItem(ActionLog action, boolean empty) {
        if (action != null) {
            String actionId = new Integer(action.getId()).toString();
            Application.getActionUpvotes(actionId);
            count.setText(new Integer(Application.getActionUpvotes(actionId)).toString());
        }
        System.out.println("update");
        super.updateItem(action, empty);
        setText(null);
        setGraphic(null);
        if (action != null && !empty) {
            personId = action.getPerson_id();
            Person person = Application.getPersonById(personId);
            name.setText(person.getFirstName() + " " + person.getLastName());

            //Retrieves profile picture
            if (person.getRealImage() == null) {
                System.out.println("User has no profile image set");
            } else if (person.getRealImage() != null) {
                System.out.println("User has a profile image set");
                File imageFile = person.getRealImage();
                Image image = new Image(imageFile.toURI().toString(),
                        240, // requested width
                        240, // requested height
                        true, // preserve ratio
                        true, // smooth rescaling
                        false); //load in background
                circle.setFill(new ImagePattern(image));
            }

            if (action.getRealImage() == null) {
                System.out.println("Action has no image");
                imageView.setImage(null);
            } else if (action.getRealImage() != null) {
                System.out.println("Action has an image");
                File imageFile = action.getRealImage();
                Image image = new Image(imageFile.toURI().toString(),
                        240, // requested width
                        240, // requested height
                        true, // preserve ratio
                        true, // smooth rescaling
                        false); //load in background
                imageView.setImage(image);
            }

            name.setStyle("-fx-font-weight: bold; -fx-label-padding: 10;");

            label.setText(action.getDescription());
            prefWidthProperty().bind(this.widthProperty().subtract(2));
            setWidth(MainController.mainPaneWidth);
            label.setWrapText(true);
            setGraphic(main);
            //setStyle("-fx-cell-size: 200;");
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    name.setTextFill(Color.BLACK);
                    label.setTextFill(Color.BLACK);
                    count.setTextFill(Color.BLACK);
                }
            });

            if (!Application.getAlreadyUpvoted(MainController
                    .authKey, new Integer(action.getId()).toString())) {
                cheerIcon.setFill(Color.valueOf("#00CC00"));
            }
        }


        //Cheers someone for doing an activity
        cheerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (Application.getAlreadyUpvoted(MainController
                        .authKey, new Integer(action.getId()).toString())) {
                    int cheerCount = Integer.valueOf(count.getText());

                    cheerCount++;
                    count.setText(String.valueOf(cheerCount));
                    cheerIcon.setFill(Color.valueOf("#00CC00"));
                    try {
                        Application.postActionUpvote(MainController
                                .authKey, new Integer(action.getId()).toString());
                    } catch (AuthException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}