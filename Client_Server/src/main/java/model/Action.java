package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "points")
    private int points;

    private String category;

    /**
     * Constructor for action.
     *
     * @param id          used for updating a row in database if needed otherwise
     *                    just used for adding a another row in the table.
     * @param type        the type of the food action (vegetarian, vegan)
     * @param description The description of the food action.
     * @param points      the amount of points this action is worth.
     * @param category    The Category of this action
     */

    public Action(int id, String type, String description,
                  int points, String category) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.points = points;
        this.category = category;
    }

    public Action() {
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * In order to see all of the items in the Action, call this method instead of toString().
     * The purpose of this is because in the GUI, when you select to display actions you see
     * what is in the toString method. And we want to see only the description or type of
     * the action. Thats why there are two display methods.
     *
     * @return All items in the Actions object as a String.
     */
    public String displayItems() {
        return "FoodLog{"
                + "id=" + id
                + ", type='" + type + '\''
                + ", description='" + description + '\''
                + ", points=" + points
                + ", category='" + category + '\''
                + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
