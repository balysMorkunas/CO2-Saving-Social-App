package gui.controllers;

import static java.util.Collections.reverseOrder;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import gogreenclient.Application;
import gui.models.FollowerCell;
import gui.models.LeaderboardCell;
import gui.models.MainCell;
import gui.models.SuggestedCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro8.JMetro;
import model.Achievement;
import model.Action;
import model.ActionLog;
import model.AuthKey;
import model.Person;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.security.auth.message.AuthException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MainController {

    public static AuthKey authKey;
    public static double mainPaneWidth;

    private Stage stage;
    private int headerSize = 28;
    private int intScore = 0;
    private double coordinateX;
    private double coordinateY;
    private boolean fullScreen = false;

    @FXML private StackPane mainPane;

    @FXML private AnchorPane userPane;
    @FXML private FontAwesomeIcon defaultUserIcon;
    @FXML private Circle imageCircle;
    @FXML private Label name;
    @FXML private Label score;
    @FXML private Label co2Saved;
    @FXML private Label mainLabel;

    @FXML private Button mealsButton;
    @FXML private Button transportationButton;
    @FXML private Button householdButton;

    @FXML private Button settingsButton;
    @FXML private Button browseImage;
    @FXML private CheckBox privateCheck = new CheckBox();

    @FXML private Button logoutButton;

    @FXML private ListView leaderboardListView;
    @FXML private ListView<ActionLog> historyListView;

    @FXML private GridPane achievementsPane;
    @FXML
    private ImageView icon00;

    @FXML
    private ImageView icon10;

    @FXML
    private ImageView icon20;

    @FXML
    private ImageView icon30;

    @FXML
    private ImageView icon01;

    @FXML
    private ImageView icon11;

    @FXML
    private ImageView icon21;

    @FXML
    private ImageView icon31;

    @FXML
    private ImageView icon02;

    @FXML
    private ImageView icon12;

    @FXML
    private ImageView icon22;

    @FXML
    private ImageView icon32;

    ArrayList<ImageView> achievementsImageViews = new ArrayList<>(Arrays.asList(
            icon00,
            icon10,
            icon20,
            icon30,
            icon01,
            icon11,
            icon21,
            icon31,
            icon02,
            icon12,
            icon22,
            icon32
    ));


    private ArrayList<ActionLog> actionLogs;
    private ArrayList<Person> personList;
    private ArrayList<Person> followingList = new ArrayList();
    private ArrayList<Person> suggestedList = new ArrayList();

    private ArrayList<Action> actionArrayList = new ArrayList<>(); //All available actions
    private ArrayList<ActionLog> historyList = new ArrayList<>();
    private Person person;
    private File actionImage = null;


    private ArrayList<Action> mealActions = new ArrayList<>();
    private ArrayList<Action> transportActions = new ArrayList<>();
    private ArrayList<Action> householdActions = new ArrayList<>();

    private ArrayList<Achievement> achievements = new ArrayList<>(); //All Achievements from DB
    private ArrayList<String> accomplishedAchievements = new ArrayList<>();
    private String highestAchievement;
    private int highestAchievementIndex;




    /**
     * Initializes the mainscreen with the data of the user.
     * @param authKey Authkey is the authentication key
     */
    @FXML
    public void initialize(AuthKey authKey) {
        System.out.println("Mainscreen opened with username = " + authKey.getEmail());
        this.authKey = authKey;
        name.setText(authKey.getEmail());
        mainPaneWidth = mainPane.getWidth();
        try {
            //Retrieves person from Person entity from database
            this.person = Application.getPerson(this.authKey);
            score.setText(Integer.toString(this.person.getPoints()));
            double kgSaved = (double) this.person.getPoints() / 25;
            System.out.println(this.person.getPoints() + "/25= " + kgSaved);
            co2Saved.setText(Double.toString(kgSaved));

            //Retrieves profile picture
            if (this.person.getRealImage() == null) {
                System.out.println("User has no profile image set");
            } else if (this.person.getRealImage() != null) {
                System.out.println("User has a profile image set");
                File imageFile = this.person.getRealImage();
                Image image = new Image(imageFile.toURI().toString(),
                        240, // requested width
                        240, // requested height
                        true, // preserve ratio
                        true, // smooth rescaling
                        false); //load in background
                imageCircle.setFill(new ImagePattern(image));
                userPane.getChildren().remove(defaultUserIcon);
            }


            name.setText(this.person.getFirstName() + " " + this.person.getLastName());

            //Retrieves the actionLog of the person and his followers and merges it.
            this.actionLogs = Application.getActionLogs(authKey);
            ArrayList<ActionLog> followersLogs = Application.getFollowersActions(authKey);
            this.actionLogs.addAll(followersLogs);
            Collections.sort(actionLogs, reverseOrder(ActionLog.dateComparator));
            ObservableList<ActionLog> actionLogsList =
                    FXCollections.observableArrayList(actionLogs);
            historyListView.setItems(actionLogsList);
            historyListView.setCellFactory(param -> new MainCell<ActionLog>() {});

            //Retrieves all available actions from the database and sorts them by category
            for (Action action: Application.getActions()) {
                this.actionArrayList.add(action);
            }
            System.out.println("All actions:" + this.actionArrayList.toString());

            for (Action action: actionArrayList) {
                if (action.getCategory().equals("meal")) {
                    this.mealActions.add(action);
                } else if (action.getCategory().equals("transport")) {
                    this.transportActions.add(action);
                } else if (action.getCategory().equals("household")) {
                    this.householdActions.add(action);
                } else {
                    System.out.println("Not filtered action: " + action.toString());
                }
            }
            System.out.println(mealActions.toString() + "\n"
                    + transportActions.toString() + "\n" + householdActions.toString());

            //Retrieves achievements from database
            for (Achievement achievement : Application.getAchievements()) {
                this.achievements.add(achievement);
            }



            //Retrieves all social stuff, people you follow and suggested people
            this.followingList = new ArrayList<>(Application.getFollowingList(authKey));
            this.suggestedList = new ArrayList<>(Application.getSuggestedList(authKey));

            //Retrieves Settings
            Boolean hidden = this.person.getHidden();
            privateCheck.setSelected(hidden);

            displayLeaderboard();

        } catch (AuthException e) {
            e.printStackTrace();
        }


    }

    /**
     * Shows your history of all ecological things you have done in list form.
     * TODO: If you press an item in a certain category, it should be added to the historylist.
     * Which should be displayed here in the historyListView.
     */
    public void goToHomePane() {
        mainLabel.setText("History");
        if (mainPane.getChildren() != null) {
            mainPane.getChildren().clear();
            //TODO replace mainPane.getChildren().addAll(historyButton, historyListView);
            mainPane.getChildren().add(historyListView);
            try {
                //Refreshes news feed
                this.actionLogs = Application.getActionLogs(authKey);
                ArrayList<ActionLog> followersLogs = Application.getFollowersActions(authKey);
                this.actionLogs.addAll(followersLogs);
                Collections.sort(actionLogs, reverseOrder(ActionLog.dateComparator));
                ObservableList<ActionLog> actionLogsList =
                        FXCollections.observableArrayList(actionLogs);
                historyListView.setItems(actionLogsList);
                historyListView.setCellFactory(param -> new MainCell<ActionLog>() {});
                //Refreshes the leader board.
                displayLeaderboard();

            } catch (AuthException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("mainPane.getChildren() is null");
        }
    }

    /**
     * If you click "logs" button, a menu appears on the mainPane of the mainscreen, where
     * you can choose a category.
     */
    public void goToLogsPane() {
        final int buttonTextSize = 70;
        final double buttonWidth = mainPane.getWidth();
        final double buttonHeight = mainPane.getHeight() / 3;

        mealsButton = new Button("Meals");
        mealsButton.setStyle(String.format("-fx-font-size: %dpx;", buttonTextSize));
        mainPane.setAlignment(mealsButton, Pos.TOP_CENTER);
        mealsButton.setPrefSize(buttonWidth, buttonHeight);
        mealsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                displayCategory(mealActions,1);
            }
        });


        transportationButton = new Button("Transportation");

        transportationButton.setStyle(String.format("-fx-font-size: %dpx;",
                (int) buttonTextSize));
        transportationButton.setPrefSize(buttonWidth, buttonHeight);
        mainPane.setAlignment(transportationButton, Pos.CENTER);
        transportationButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                displayCategory(transportActions,2);
            }
        });

        householdButton = new Button("Household");
        householdButton.setPrefSize(buttonWidth, buttonHeight);
        householdButton.setStyle(String.format("-fx-font-size: %dpx;", buttonTextSize));
        mainPane.setAlignment(householdButton, Pos.BOTTOM_CENTER);
        householdButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                displayCategory(householdActions ,3);
            }
        });

        mainPane.getChildren().addAll(mealsButton, transportationButton, householdButton);
    }

    /**
     * Displays the list for the specific category.
     */
    public void displayCategory(ArrayList<Action> actions, int callerId) {
        mainPane.getChildren().clear();
        mainLabel.setText("Actions");
        ListView<Action> categoryListView = new ListView();
        categoryListView.setMaxSize(mainPane.getWidth(), mainPane.getHeight());
        mainPane.setAlignment(categoryListView, Pos.TOP_CENTER);
        categoryListView.setStyle("-fx-background-color:  #71E3AC;" + "-fx-border-width: 0");
        ObservableList<Action> observableList = FXCollections.observableArrayList(actions);
        categoryListView.setItems(observableList);
        categoryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                actionImage = null;
                final Action selection = categoryListView.getSelectionModel().getSelectedItem();
                final VBox addPopup = new VBox();
                FontAwesomeIcon closeIcon = new FontAwesomeIcon();
                final HBox iconBox = new HBox();
                closeIcon.setIconName("CLOSE");
                closeIcon.setCursor(Cursor.HAND);
                closeIcon.setFill(Color.valueOf("#ee5253"));
                closeIcon.setSize("1.6em");
                closeIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                });
                iconBox.getChildren().add(closeIcon);
                iconBox.setAlignment(Pos.CENTER_RIGHT);
                addPopup.setMaxSize(415,210);
                addPopup.setAlignment(Pos.CENTER);
                addPopup.setStyle("-fx-background-color: #57B98A");
                Stage newStage = new Stage();
                newStage.setTitle("Create account");
                HBox starBox = new HBox();
                starBox.setAlignment(Pos.BASELINE_RIGHT);
                TextArea description = new TextArea();
                description.setWrapText(true);
                description.setMaxSize(400, 230);
                TextField secondaryDescription = new TextField();
                browseImage = null;
                if (callerId == 2) {
                    secondaryDescription.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<?
                                extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                                secondaryDescription.setText(oldValue);
                            }
                        }
                    });
                } else if (callerId == 3) {
                    description.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<?
                                extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                                description.setText(oldValue);
                            }
                        }
                    });
                }
                switch (callerId) {
                    case 1:
                        browseImage = new Button("Upload a picture of your meal");
                        browseImage.setStyle("-fx-font-size: 12" + "-fx-");
                        browseImage.setAlignment(Pos.CENTER);
                        browseImage.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                JFileChooser fileChooser = new JFileChooser(FileSystemView
                                        .getFileSystemView().getHomeDirectory());
                                fileChooser.setCurrentDirectory(
                                        new File(System.getProperty("user.home")));
                                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                fileChooser.setMultiSelectionEnabled(false);
                                fileChooser.setAcceptAllFileFilterUsed(false);
                                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                        "JPG image", "jpg");
                                fileChooser.addChoosableFileFilter(filter);
                                int result = fileChooser.showOpenDialog(null);
                                if (result == JFileChooser.APPROVE_OPTION) {
                                    actionImage = fileChooser.getSelectedFile();
                                }
                            }
                        });
                        description.setPromptText("Description of your meal*");
                        break;
                    case 2:
                        description.setPromptText("Description of your transport");
                        secondaryDescription.setPromptText("Distance of your transport in km*");
                        break;
                    case 3:
                        if (selection.getType().equals("solar")) {
                            description.setPromptText(
                                    "How many solar panels do you have installed?");
                        } else if (selection.getType().equals("heating")) {
                            description.setPromptText(
                                    "With how many degrees did you lower your thermostat?");
                        }
                        break;
                    default:
                        break;
                }
                Button addButton = new Button("Add description");
                addButton.setAlignment(Pos.BOTTOM_CENTER);
                Label message = new Label();
                message.setTextFill(Color.RED);
                addButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addButtonAction(description, addPopup,
                            secondaryDescription, message, callerId, selection, newStage);
                    }
                });
                addPopup.getChildren().addAll(iconBox, description);
                if (callerId == 2) {
                    addPopup.getChildren().add(secondaryDescription);
                }
                if (browseImage != null) {
                    addPopup.getChildren().addAll(browseImage);
                }
                addPopup.getChildren().addAll(addButton,message);
                Scene stageScene = new Scene(
                        addPopup, addPopup.getMaxWidth(), addPopup.getMaxHeight());
                newStage.setScene(stageScene);
                newStage.initStyle(StageStyle.UNDECORATED);
                new JMetro(JMetro.Style.LIGHT).applyTheme(stageScene);
                newStage.show();
            }
        });
        mainPane.getChildren().add(categoryListView);
    }

    /**
     * Method is gui part of the add action method.
     * @param description the description of the action.
     * @param addPopup VBox.
     * @param secondaryDescription description of user.
     * @param message The message.
     * @param callerId The id of the person that called this method.
     * @param selection the action selected.
     * @param newStage the new stage.
     */
    public void addButtonAction(TextArea description, VBox addPopup, TextField secondaryDescription,
                                Label message, int callerId, Action selection, Stage newStage) {
        String descriptionText = description.getText().replaceAll("\t", "");
        if (descriptionText.length() < 1 || (addPopup.getChildren()
                .contains(secondaryDescription)
                && secondaryDescription.getText().length() < 1)) {
            message.setText("Please set a description");
        } else {
            int indexOfLastChar = 0;
            for (int i = descriptionText.length() - 1; i >= 0; i--) {
                if (descriptionText.charAt(i) != ' ') {
                    indexOfLastChar = i;
                    break;
                }
            }
            descriptionText = descriptionText.substring(0, indexOfLastChar + 1);
            switch (callerId) {
                case 1:
                    try {
                        setDescriptionAndSend(selection,
                                descriptionText, actionImage);
                    } catch (AuthException e) {
                        System.out.println(e);
                    }
                    newStage.close();
                    break;
                case 2:
                    try {
                        String transport = ". Distance of transport: ";
                        setDescriptionAndSend(selection,
                                descriptionText + transport + secondaryDescription
                                        .getText() + "km", actionImage);
                    } catch (AuthException e) {
                        e.printStackTrace();
                    }
                    newStage.close();
                    break;
                case 3:
                    try {
                        setDescriptionAndSend(selection,
                                descriptionText, actionImage);
                    } catch (AuthException e) {
                        e.printStackTrace();
                    }
                    newStage.close();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Shows a list of the people you follow and people to follow on the mainscreen.
     * TODO: Retrieve list of followers and toFollow from database
     */
    public void goToSocialPane() {
        mainPane.getChildren().clear();
        int numOfPanes = 2;
        mainLabel.setText("Social");
        StackPane followingPane = new StackPane();
        followingPane.setMaxSize(mainPane.getWidth(),mainPane.getHeight() / numOfPanes);
        mainPane.setAlignment(followingPane,Pos.TOP_CENTER);
        mainPane.getChildren().add(followingPane);

        mainLabel.setText("Following");

        this.followingList = Application.getFollowingList(this.authKey);
        ObservableList<Person> followingObList =
                FXCollections.observableArrayList(this.followingList);
        ListView<Person> followingListView = new ListView<>(followingObList);
        double listViewHeight = followingPane.getMaxHeight();
        followingListView.setMaxSize(mainPane.getWidth(),listViewHeight);
        followingListView.setStyle("-fx-background-color: #71E3AC;" + "-fx-border-width: 0");
        followingPane.setAlignment(followingListView, Pos.BOTTOM_CENTER);
        followingListView.setCellFactory(param -> new FollowerCell<Person>(this) {});

        followingPane.getChildren().add(followingListView);


        StackPane suggestedPane = new StackPane();
        suggestedPane.setMaxSize(mainPane.getWidth(),mainPane.getHeight() / numOfPanes);
        mainPane.setAlignment(suggestedPane,Pos.BOTTOM_CENTER);
        mainPane.getChildren().add(suggestedPane);

        Button suggestedButton = new Button("Suggested people");
        suggestedButton.setStyle(String.format("-fx-font-size: %dpx;",
                (int) headerSize) + "-fx-background-color: #2E8B70;");
        suggestedButton.setMaxSize(mainPane.getWidth(), 60);
        suggestedButton.setFont(Font.font("Segoe UI", 28));
        mainPane.setAlignment(suggestedButton, Pos.TOP_CENTER);

        this.suggestedList = Application.getSuggestedList(this.authKey);
        ObservableList<Person> suggestedFollowObList =
                FXCollections.observableArrayList(this.suggestedList);
        ListView<Person> suggestedPeopleListView = new ListView<>(suggestedFollowObList);
        double suggestedHeight = suggestedPane.getMaxHeight() - suggestedButton.getMaxHeight();
        suggestedPeopleListView.setMaxSize(mainPane.getWidth(),suggestedHeight);
        suggestedPeopleListView.setStyle("-fx-background-color: #71E3AC;" + "-fx-border-width: 0");
        suggestedPane.setAlignment(suggestedPeopleListView, Pos.BOTTOM_CENTER);
        suggestedPeopleListView.setCellFactory(param -> new SuggestedCell<Person>(this) {});

        suggestedPane.getChildren().addAll(suggestedButton, suggestedPeopleListView);
    }

    /**
     * Shows information on how points are decided.
     * TODO Add actual information
     */
    public void goToInfoPane() {
        mainPane.getChildren().clear();

        mainLabel.setText("Information");
        ScrollPane textPane = new ScrollPane();
        textPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        textPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        textPane.setStyle("-fx-background: #71E3AC;" + "-fx-padding: 0;");
        textPane.getStyleClass().add("edge-to-edge");
        textPane.setMaxSize(mainPane.getWidth(), mainPane.getHeight() / 3);
        mainPane.setAlignment(textPane, Pos.TOP_CENTER);

        Text text = new Text();
        int textSize = 18;
        text.setStyle(String.format("-fx-font-size: %dpx;",
                (int) textSize));
        text.setText(" The goal of this application is to incentivise the user to "
                + "lower his CO2 emissions" + " \n in order to become more \"green\". "
                + "The application allows the user to log their actions \n daily "
                + "where they will receive points. "
                + "Points are given when you've done something that\n benefits the environment"
                + " and lowers your CO2 emissions. \n "
                + "On average 1 KG of CO2 saved = 25 points added "
                + "The amount of points you \n receive for an action is directly linked"
                + " with the amount of CO2 saved, in kilograms.\n"
                + " This means that if you take the bike instead of the bus for a long trip "
                + "you will receive \n more points "
                + "than if you took the bike for a shorter trip. The higher the points "
                + "you have, \n the more CO2 you have saved. "
                + "You can follow other users and compare your points \n with them "
                + "to see how you are doing compared to your friends and others. ");
        textPane.setContent(text);

        Pane statisticsPane = new Pane();
        statisticsPane.setStyle("-fx-background-color: #71E3B3");
        statisticsPane.setMaxSize(mainPane.getWidth(), 2 * (mainPane.getHeight() / 3));
        Label test = new Label("Global Stats");
        test.setFont(new Font(headerSize));
        test.setStyle("-fx-background-color: #57B98A");
        test.setAlignment(Pos.CENTER);
        test.setPrefSize(mainPane.getWidth(), 40);


        VBox chartBox = new VBox();
        chartBox.setPrefSize(mainPane.getWidth(),statisticsPane.getMaxHeight() - 40);
        chartBox.setStyle("-fx-background-color: transparent");

        int mealsPoints = Application.getStats().get(0);
        int transportPoints = Application.getStats().get(1);
        int householdPoints = Application.getStats().get(2);
        int totalPoints = mealsPoints + transportPoints + householdPoints;


        double totalCO2Saved = totalPoints / 25;

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Meals", mealsPoints / 25),
                        new PieChart.Data("Transportation", transportPoints / 25),
                        new PieChart.Data("Household", householdPoints / 25));
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("");
        chart.setLegendSide(Side.RIGHT);
        Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 24 arial;");

        for (PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent error) {
                            caption.setTranslateX(error.getSceneX() - mainPane.getWidth() + 70);
                            caption.setTranslateY(error.getSceneY() - mainPane.getHeight());
                            caption.setText(String.valueOf(data.getPieValue())
                                + "kgs of CO2("
                                + (data.getPieValue() / totalCO2Saved * 100) + "%)");
                            System.out.println("caption position: "
                                + caption.getLayoutX() + "  , " + caption.getLayoutY()
                                + "translate: " +  caption.getTranslateX() + ",  "
                                + caption.getTranslateY());
                        }
                    });
        }
        chartBox.getChildren().addAll(chart, caption);
        chartBox.setAlignment(Pos.CENTER);
        VBox totalBox = new VBox();
        totalBox.setPrefWidth(mainPane.getWidth());
        totalBox.setLayoutY(420);
        totalBox.setAlignment(Pos.BOTTOM_CENTER);
        Label totalCO2 = new Label("Total amount of CO2 Saved: " + totalCO2Saved + " kgs of CO2");
        totalCO2.setFont(new Font(20));
        totalCO2.setAlignment(Pos.CENTER);
        totalBox.getChildren().add(totalCO2);



        statisticsPane.getChildren().addAll(test,chartBox, totalBox);
        mainPane.setAlignment(statisticsPane, Pos.BOTTOM_CENTER);

        mainPane.getChildren().addAll(textPane, statisticsPane);
    }

    /**
     * Shows all achievements and what achievements the user has accomplished.
     */
    public void goToAchievementsPane() {
        mainPane.getChildren().clear();


        Parent root;
        try {

            System.out.println("achievements are shown");

            GridPane achievementsPane = FXMLLoader.load(
                getClass().getClassLoader().getResource("views/achievements.fxml"));
            mainPane.getChildren().add(achievementsPane);
            //Finds out what the highest Achievement the user has achieved
            for (Achievement achievement : this.achievements) {
                if (person.getPoints() > achievement.getRequiredPoints()
                    && achievement.getType().equals("points")) {
                    this.highestAchievement = achievement.getName();
                    this.highestAchievementIndex = achievement.getId();
                }
            }



            Image image00 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/rookie.png"));
            Image image10 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/beginner.png"));
            Image image20 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/amateur.png"));
            Image image30 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/semi-pro.png"));
            Image image01 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/pro.png"));
            Image image11 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/world-class.png"));
            Image image21 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/god.png"));
            Image image31 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/bike.png"));
            Image image02 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/followers.png"));
            Image image12 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/friends.png"));
            Image image22 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/login.png"));
            Image image32 = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/picture.png"));
            Image imageLock = new Image(getClass().getClassLoader()
                .getResourceAsStream("image/badges/lock.png"));

            ArrayList<Image> achievementImages = new ArrayList<>(Arrays.asList(
                    image00,
                    image10,
                    image20,
                    image30,
                    image01,
                    image11,
                    image21,
                    image31,
                    image02,
                    image12,
                    image22,
                    image32
            ));


            //Assigns an imageview to every imageview in the fxml.
            for (int i = 0; i < achievementsImageViews.size(); i++) {
                ImageView imageView = (ImageView) achievementsPane.getChildren().get(i);
                achievementsImageViews.set(i, imageView);
            }

            //Sets the image of each imageview in the achievements
            for (int i = 0; i < achievementImages.size(); i++) {
                Image image = achievementImages.get(i);
                achievementsImageViews.get(i).setImage(image);

                //For point based achievements,
                // it locks achievements that you don't have the points for.
                if (i >= highestAchievementIndex & i <= 6) {
                    achievementsImageViews.get(i).setImage(imageLock);
                } else if (i < 7) {
                    Tooltip.install(achievementsImageViews.get(i), new Tooltip("For getting "
                        + Application.getAchievements()[i].getRequiredPoints() + " points."));
                }
            }

            final Achievement[] received = Application.getAchievementsLog(authKey);
            //Checks conditions for the other badges
            final ImageView imageViewBike = achievementsImageViews.get(7);
            final ImageView imageViewPopular = achievementsImageViews.get(8);
            final ImageView imageViewFriends = achievementsImageViews.get(9);
            final ImageView imageViewPlantASeed = achievementsImageViews.get(10);
            final ImageView imageViewTimeForAChange = achievementsImageViews.get(11);
            imageViewBike.setImage(imageLock);
            imageViewPopular.setImage(imageLock);
            imageViewFriends.setImage(imageLock);
            imageViewPlantASeed.setImage(imageLock);
            imageViewTimeForAChange.setImage(imageLock);


            for (int i = 0; i < received.length; i++) {

                if (received[i].getType().equals("bike")) {
                    imageViewBike.setImage(image31);
                }

                if (received[i].getType().equals("followers")) {
                    imageViewPopular.setImage(image02);
                }

                if (received[i].getType().equals("friends")) {
                    imageViewFriends.setImage(image12);
                }
                if (received[i].getType().equals("login")) {
                    imageViewPlantASeed.setImage(image22);
                }
                if (received[i].getType().equals("picture")) {
                    imageViewTimeForAChange.setImage(image32);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Opens the settings pane where you can toggle different settings.
     * Settings: hidden(make profile private).
     */
    public void goToSettingsPane() {
        final double hboxWidth = mainPane.getWidth();
        final int hboxHeight = 100;
        final int toggleWidth = 100;
        final int textSize = 42;
        final int descriptionSize = 12;
        mainPane.getChildren().clear();
        mainLabel.setText("Settings");
        mainPane.setStyle("-fx-background-color:  #71E3AC");

        VBox settingsBox = new VBox();
        mainPane.getChildren().add(settingsBox);


        HBox privateBox = new HBox();
        privateBox.setAlignment(Pos.CENTER);
        privateBox.setPrefSize(hboxWidth,hboxHeight);
        Label privateLabel = new Label("Private profile");
        privateLabel.setFont(new Font(textSize));
        Label privateDescription = new Label("Others won't see your profile/actions");
        privateDescription.setFont(new Font(descriptionSize));
        privateDescription.setTextFill(Color.GRAY);
        Region privateRegion = new Region();
        privateBox.setHgrow(privateRegion,Priority.ALWAYS);
        privateCheck.setPrefSize(toggleWidth,hboxHeight);

        privateBox.getChildren().addAll(privateLabel,privateDescription,privateRegion,
                privateCheck);


        System.out.println("privateCheck:" + privateCheck.isSelected());
        privateCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String hidden = privateCheck.isSelected() ? "true" : "false";
                    Application.hidden(hidden, authKey);
                    System.out.println("Settings are set: \n" + "Hidden =  " + hidden);
                } catch (AuthException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox imageBox = new HBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefSize(hboxWidth,hboxHeight);
        Label imageLabel = new Label("Change profile picture");
        imageLabel.setFont(new Font(textSize));
        Label imageDescription = new Label("Upload a new profile image");
        imageDescription.setFont(new Font(descriptionSize));
        imageDescription.setTextFill(Color.GRAY);
        Region imageRegion = new Region();
        imageBox.setHgrow(imageRegion,Priority.ALWAYS);
        Button browseImage = new Button("Upload");

        Label imageFile = new Label();

        imageBox.getChildren().addAll(imageLabel,imageDescription,imageRegion,
                imageFile,browseImage);

        browseImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile;
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
                    Application.updateImage(authKey, selectedFile);

                    Image image = new Image(
                            selectedFile.toURI().toString(),
                            240, 240,
                            true,
                            true,
                            false);
                    imageCircle.setFill(new ImagePattern(image));
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    userPane.getChildren().remove(defaultUserIcon);
                }



            }
        });



        settingsBox.getChildren().addAll(privateBox, imageBox);

    }


    /**
     * Logout button redirects you to logout screen.
     * It exits the mainscreen.
     * If resource can't be found  @throws IOEXception
     */
    public void logout() throws IOException {
        stage = (Stage) logoutButton.getScene().getWindow();
        GridPane root;
        String resource = "views/loginscreen_alt.fxml";
        root = FXMLLoader.load(getClass().getClassLoader().getResource(resource));
        Scene scene = new Scene(root);
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        stage.setScene(scene);
        System.out.println("LoginScreen opened");
    }

    /**
     * Create a new ActionLog item and send it to the database for storing.
     * @param selection Action itself.
     * @param description The information the user has provided.
     * @throws AuthException If authentication unsuccessful.
     */
    public void setDescriptionAndSend(Action selection,
                                      String description, File image) throws AuthException {
        System.out.println("Points: " + selection.getPoints());
        System.out.println("added");

        historyListView.setMaxSize(mainPane.getWidth(),mainPane.getHeight());
        mainPane.setAlignment(historyListView, Pos.TOP_CENTER);


        String finalDescription =  selection.getCategory();
        finalDescription = finalDescription.substring(0, 1)
                .toUpperCase() + finalDescription.substring(1);

        if (description.length() > 0) {
            finalDescription += ": " + description;
        }
        //TODO: send image.
        System.out.println("Sending action");
        Application.sendAction(selection.getType(), finalDescription, authKey, image);
        this.person = Application.getPerson(authKey);
        int intScore = this.person.getPoints();
        score.setText(Integer.toString(intScore));
        double kgSaved = (double) this.person.getPoints() / 25;
        System.out.println(this.person.getPoints() + "/25= " + kgSaved);
        co2Saved.setText(Double.toString(kgSaved));

        mainPane.getChildren().clear();

        //Plz review this line because i changed "" to dateTime
        this.actionLogs = Application.getActionLogs(authKey);
        ArrayList<ActionLog> followersLogs = Application.getFollowersActions(authKey);
        this.actionLogs.addAll(followersLogs);
        Collections.sort(actionLogs, reverseOrder(ActionLog.dateComparator));
        ObservableList<ActionLog> actionLogsList =
                FXCollections.observableArrayList(actionLogs);
        historyListView.setItems(actionLogsList);
        historyListView.setCellFactory(param -> new MainCell<ActionLog>() {});

        mainPane.getChildren().remove(historyListView);
        mainPane.getChildren().add(historyListView);
        displayLeaderboard();
        boolean unlockedAchievement = Application.getNewAchievement(authKey);

        if (unlockedAchievement) {
            System.out.println("Achievement unlocked");
            displayAchievement("a");
        }
    }

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - coordinateX);
        stage.setY(event.getScreenY() - coordinateY);

        mainPaneWidth = mainPane.getWidth();
    }

    @FXML
    void pressed(MouseEvent event) {

        coordinateX = event.getSceneX();
        coordinateY = event.getSceneY();

    }

    @FXML
    void maximize(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        if (fullScreen) {
            stage.setMaximized(false);
            fullScreen = false;
            mainPaneWidth = mainPane.getWidth();
        } else {
            stage.setMaximized(true);
            fullScreen = true;
            mainPaneWidth = mainPane.getWidth();
        }
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);

    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    /**
     * Method takes all Person objects from the database
     * and displays them in the Leaderboards pane.
     */
    public void displayLeaderboard() throws AuthException {
        try {
            this.personList = Application.getFollowingList(authKey);
            Person myself = Application.getPerson(authKey);
            personList.add(myself);

            Collections.sort(personList, Person.pointComparator);

            ObservableList<Person> leaderboardObList =
                    FXCollections.observableList(personList);
            leaderboardListView.setItems(leaderboardObList);
            leaderboardListView.setCellFactory(param -> new LeaderboardCell<Person>() {});

        } catch (AuthException e) {
            e.printStackTrace();
        }
    }

    void displayAchievement(String achievement) {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        if (!stage.isShowing()) {
            stage.show();
        }
        if (!stage.isFocused()) {
            stage.requestFocus();
        }
        System.out.println(achievements);

        final VBox addPopup = new VBox();
        FontAwesomeIcon closeIcon = new FontAwesomeIcon();
        final HBox iconBox = new HBox();
        closeIcon.setIconName("CLOSE");
        closeIcon.setCursor(Cursor.HAND);
        closeIcon.setFill(Color.valueOf("#ee5253"));
        closeIcon.setSize("1.6em");
        closeIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }
        });
        iconBox.getChildren().add(closeIcon);
        iconBox.setAlignment(Pos.CENTER_RIGHT);

        addPopup.setMaxSize(350,65);
        addPopup.setAlignment(Pos.TOP_CENTER);
        addPopup.setStyle("-fx-background-color: #57B98A");

        Stage newStage = new Stage();
        newStage.setTitle("Achievement unlocked!");

        Label message = new Label();
        Achievement[] achievements = Application.getAchievementsLog(authKey);
        String achievementString = achievements[achievements.length - 1 ].getName();
        message.setText("New achievement unlocked: " + achievementString);
        message.setStyle("-fx-font-weight: bold;");

        System.out.println(message.getText());
        addPopup.getChildren().addAll(iconBox, message);

        Scene stageScene = new Scene(addPopup, addPopup.getMaxWidth(),
                addPopup.getMaxHeight());

        newStage.setScene(stageScene);
        newStage.initStyle(StageStyle.UNDECORATED);
        // load stylesheet
        new JMetro(JMetro.Style.LIGHT).applyTheme(stageScene);
        newStage.show();
    }
}
