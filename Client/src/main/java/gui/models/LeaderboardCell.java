package gui.models;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import gogreenclient.Application;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import model.Achievement;
import model.Person;

import java.util.ArrayList;


public class LeaderboardCell<P> extends ListCell<Person> {

    Label name = new Label();
    Pane pane = new Pane();
    Label points = new Label();
    HBox box = new HBox();
    FontAwesomeIcon leafIcon = new FontAwesomeIcon();
    ImageView rankIcon = new ImageView();

    /**
     * Class used to modify the style of leaderboard cell.
     */
    public  LeaderboardCell() {
        super();
        leafIcon.setIconName("PAGELINES");
        leafIcon.setSize("1.8em");
        leafIcon.setFill(Color.rgb(80,80,80));

        box.getChildren().addAll(rankIcon, name, pane, points, leafIcon);
        box.setHgrow(pane, Priority.ALWAYS);
    }

    @Override
    public void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);
        setText(null);
        setGraphic(null);

        //Checks what rank each person should be

        if (person != null && !empty) {

            ArrayList<Achievement> achievementsList = new ArrayList<>();
            Achievement[] temp = Application.getAchievements();

            for (Achievement achiev : temp) {
                achievementsList.add(achiev);
            }

            String iconName = "new"; //New users have <100 points so makes sense I guess.
            for (Achievement achievement : achievementsList) {
                if (person.getPoints() > achievement.getRequiredPoints()
                        && achievement.getType().equals("points")) {
                    iconName = achievement.getName().toLowerCase();
                    System.out.println(iconName);
                }
            }

            String path = String.format("image/badges/%s.png", iconName);
            System.out.println(path);
            Image rankImage = new Image(getClass().getClassLoader()
                    .getResourceAsStream(path), 40, 40, false, false);
            rankIcon.setImage(rankImage);
            name.setText(person.getFirstName());
            name.setStyle("-fx-label-padding: 10;");
            points.setText(String.valueOf(person.getPoints()));
            setGraphic(box);

        }
    }
}
