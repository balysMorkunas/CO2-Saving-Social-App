package gui.controllers;

import gogreenclient.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro8.JMetro;
import main.java.com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import model.AuthKey;
import org.springframework.web.client.RestClientException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.security.auth.message.AuthException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class LoginController {

    int score = 50;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML VBox addPopup = new VBox();
    @FXML TextField email = new TextField();
    @FXML  TextField firstName = new TextField();
    @FXML TextField lastName = new TextField();
    @FXML  TextField password1 = new PasswordField();
    @FXML TextField password2 = new PasswordField();
    @FXML  Label message = new Label();
    @FXML Label imageName = new Label();
    @FXML Button browseImage = new Button("Upload profile picture");
    @FXML Button addButton = new Button("Sign Up");
    @FXML Button goBackButton = new Button("Go back");
    @FXML Label loginMsg;

    @FXML AnchorPane mainPane;

    private File selectedFile = null;
    private double coordinateX;
    private double coordinateY;

    /**
     * Method attempt to login using password and username.
     * If username or password field is empty, prompts the user to enter something in those fields.
     * TODO If password does not match username, prompt the user to enter correct password
     *
     * @param event event
     */

    @FXML
    void loginAttempt(ActionEvent event) {
        loginMsg.setText("");
        if (username.getText().length() < 1 || password.getText().length() < 1) {
            String msg = "";
            if (username.getText().length() < 1) {
                System.out.println("username == null");
                msg += "username ";
            }
            if (password.getText().length() < 1) {
                if (msg.length() > 0) {
                    msg += " and  a ";
                }
                System.out.println("password == null");
                msg += "password";
            }
            loginMsg.setText("Please enter a " + msg);
            return;
        }

        String usern = username.getText();
        String passw = password.getText();
        // Check if username or password is empty

        try {
            AuthKey authKey = gogreenclient.Application.login(usern, passw);
            System.out.println(authKey);
            System.out.println("Login succeeded");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainscreen.fxml"));
            Parent root = (Parent) loader.load();

            MainController mainController = loader.getController();
            System.out.println("Username is " + authKey.getEmail());
            //Passes on the authKey to the mainscreen
            mainController.initialize(authKey);

            Stage stage = new Stage();
            BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED,
                    root, 500, 700);
            scene.removeDefaultCSS();
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
            stage.setScene(scene);

            Stage currentStage = (Stage) username.getScene().getWindow();
            currentStage.close();
            stage.show();
            System.out.println("LoginScreen opened");

        } catch (AuthException e) {
            loginMsg.setText("Wrong password or username");
            System.out.println("login failed");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void resetPassword() {
        System.out.println("Test");

        Stage newStage = new Stage();
        VBox addPopup = new VBox();
        addPopup.setMaxSize(300,100);
        addPopup.setAlignment(Pos.CENTER);

        newStage.setTitle("Password Reset");

        TextField email = new TextField();
        email.setPromptText("Email(used to login with)");
        email.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");

        Button sendButton = new Button("Send");

        Text emailText = new Text("Fill in your e-mail below to reset your password");
        Label message = new Label();
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isValidEmailAddress(email.getText())) {
                    try {
                        Application.resetPassword(email.getText());
                        message.setTextFill(Color.BLACK);
                        message.setText("An mail has been sent to: " + email.getText());
                    } catch (RestClientException e) {
                        message.setText("Sending email has failed");
                        message.setTextFill(Color.RED);
                    }
                } else {
                    message.setText("Please enter a valid e-mail");
                    message.setTextFill(Color.RED);
                }
            }
        });
        addPopup.getChildren().addAll(emailText,email,message,sendButton);
        Scene stageScene = new Scene(addPopup, addPopup.getMaxWidth(), addPopup.getMaxHeight());
        newStage.setScene(stageScene);

        // load stylesheet
        new JMetro(JMetro.Style.LIGHT).applyTheme(stageScene);
        newStage.show();
    }

    /**
     * Method attempts to create an account and store it on the database.
     * TODO establish creation of account and storing it on the database
     * User is prompted to enter a username and a password (twice).
     *
     * @param event event
     */
    @FXML
    void signUp(ActionEvent event) {
        mainPane.getChildren().clear();
        Stage newStage = new Stage();
        addPopup.setMaxSize(250,280);
        addPopup.setAlignment(Pos.CENTER);
        newStage.setTitle("Create account");
        Label starMessage = new Label("* indicates required");
        starMessage.setTextFill(Color.BLACK);
        email.setPromptText("Email(used to login with)*");
        email.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        email.setLayoutX(81);
        email.setLayoutY(81);
        firstName.setPromptText("First Name*");
        firstName.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        firstName.setLayoutX(81);
        firstName.setLayoutY(124);

        lastName.setPromptText("Last Name*");
        lastName.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        lastName.setLayoutX(81);
        lastName.setLayoutY(167);

        password1.setPromptText("Password*");
        password1.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        password1.setLayoutX(81);
        password1.setLayoutY(210);

        password2.setPromptText("Re-enter password*");
        password2.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        password2.setLayoutX(81);
        password2.setLayoutY(253);


        browseImage.setStyle("-fx-font-size: 12" + "-fx-");
        browseImage.setAlignment(Pos.CENTER_LEFT);
        browseImage.setLayoutX(81);
        browseImage.setLayoutY(297);


        Button addButton = new Button("Sign Up");
        addButton.setAlignment(Pos.CENTER);
        addButton.setLayoutX(81);
        addButton.setLayoutY(340);

        goBackButton.setAlignment(Pos.CENTER);
        goBackButton.setLayoutX(81);
        goBackButton.setLayoutY(383);

        // label to return error messages
        message.setLayoutY(40);
        message.setTextFill(Color.RED);

        imageName.setLayoutX(81);
        imageName.setLayoutY(322);
        imageName.setTextFill(Color.BLACK);

        browseImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView()
                        .getHomeDirectory());
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG image", "jpg");
                fileChooser.addChoosableFileFilter(filter);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    imageName.setText(selectedFile.getName());
                }
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ////Check if all required fields are filled in
                ArrayList<String> requiredFields = new ArrayList<>(Arrays.asList(
                        email.getText(),
                        firstName.getText(),
                        lastName.getText(),
                        password1.getText(),
                        password2.getText()
                ));
                for (String field: requiredFields) {
                    if (field.length() == 0) {
                        message.setText("Please make sure to fill in all fields");
                        return;
                    }
                }
                // check if email is valid
                if (!isValidEmailAddress(email.getText())) {
                    message.setText("Please enter a valid e-mail address");
                    return;
                } else if (!password1.getText().equals(password2.getText())) {
                    // check if password and re-entered password are the same.
                    message.setText("Please make sure both passwords match.");
                    return;
                } else if (password1.getText().length() < 6) {
                    // passwords should be atleast 6 characters long
                    message.setText("Password should be at least 6 characters");
                    return;
                } else {
                    gogreenclient.Application.register(password1.getText(),
                            firstName.getText(), lastName.getText(),
                            email.getText(), selectedFile);
                    System.out.println("Image:" + selectedFile);
                    Stage stage = (Stage) firstName.getScene().getWindow();
                    GridPane root = null;
                    try {
                        String resource = "views/loginscreen_alt.fxml";
                        root = FXMLLoader.load(getClass().getClassLoader().getResource(resource));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene scene = new Scene(root);
                    new JMetro(JMetro.Style.LIGHT).applyTheme(root);
                    stage.setScene(scene);
                    System.out.println("LoginScreen opened");
                    newStage.close();
                }

            }
        });
        
        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) goBackButton.getScene().getWindow();
                GridPane root = null;
                try {
                    String resource = "views/loginscreen_alt.fxml";
                    root = FXMLLoader.load(getClass().getClassLoader().getResource(resource));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root);
                new JMetro(JMetro.Style.LIGHT).applyTheme(root);
                stage.setScene(scene);
                System.out.println("LoginScreen opened");
            }
        });

        mainPane.getChildren().addAll(starMessage,email,firstName,lastName,password1,
                password2,addButton,browseImage, message, goBackButton, imageName);
    }

    /**
    *  Method checks whether an EmailAddress is valid or not.
    *
    * @param email email address to check.
    * @return boolean
    */
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - coordinateX);
        stage.setY(event.getScreenY() - coordinateY);
    }

    @FXML
    void pressed(MouseEvent event) {

        coordinateX = event.getSceneX();
        coordinateY = event.getSceneY();

    }


}
