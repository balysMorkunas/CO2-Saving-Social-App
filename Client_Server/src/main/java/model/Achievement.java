package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int requiredPoints;

    private String type;

    /**
     * Constructor for an Achievement object.
     *
     * @param id                used for updating a row in database if needed otherwise
     *                          just used for adding a another row in the table.
     * @param name              name of the achievement
     * @param requiredPoints    amount of points required to accomplish the achievement.
     */
    public Achievement(int id, String name, int requiredPoints, String type) {
        this.id = id;
        this.name = name;
        this.requiredPoints = requiredPoints;
        this.type = type;
    }

    public Achievement() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Achievement{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", requiredPoints=" + requiredPoints
                + '}';
    }
}




