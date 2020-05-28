package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;
import org.hibernate.annotations.Type;

import java.io.File;
import java.io.IOException;

import java.util.Base64;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ActionLog {

    public static Comparator<ActionLog> dateComparator = new Comparator<ActionLog>() {
        /**
         * Method compares two integer values.
         *
         * @param o1 Object this of comparison.
         * @param o2 Object other of comparison.
         * @return Returns the difference of points.
         */
        public int compare(ActionLog o1, ActionLog o2) {
            Date dateFirst = o1.getDateTime();
            Date dateSecond = o2.getDateTime();

            return dateFirst.compareTo(dateSecond);
        }
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "points")
    private int points;

    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "person_id")
    private int personId;

    @Column(name = "firstName")
    private String firstName;

    @Transient
    private String image;

    @Column(name = "filePath")
    private String filePath;



    /**
     * Constructor for actionLog.
     *
     * @param id          used for updating a row in database if needed otherwise
     *                    just used for adding a another row in the table.
     * @param type        the type of the food action (vegetarian, vegan)
     * @param description The description of the food action.
     * @param points      the amount of points this action is worth.
     * @param dateTime    the date and time when the action was performed.
     *                    Used for sorting in database.
     */

    public ActionLog(int id, String type, String description,
                   int points, Date dateTime, int personId, String firstName) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.points = points;
        this.dateTime = dateTime;
        this.personId = personId;
        this.firstName = firstName;
    }

    public ActionLog() {
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getPerson_id() {
        return personId;
    }

    public void setPerson_id(int personId) {
        this.personId = personId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    /**
     * Saves a temp image of a given person to the SSD/HDD.
     * @return The path of the file on disk
     */
    @JsonIgnore
    public File getRealImage() {
        new File(System.getProperty("user.home") + "\\test\\").mkdirs();
        File file = new File(System.getProperty("user.home") + "\\test\\", "a" + getId() + ".jpg");
        try {
            if (image != null  && !image.equals("")) {
                file.createNewFile();
                byte[] decodedBytes = Base64.getDecoder().decode(getImage());
                FileUtils.writeByteArrayToFile(file, decodedBytes);
                file.deleteOnExit();
            }
        } catch (IOException | NullPointerException e) {
            return null;
        }
        return file;
    }

    @Override
    public String toString() {
        return "ActionLog{"
                + "id=" + id
                + ", type='" + type + '\''
                + ", description='" + description + '\''
                + ", points=" + points
                + ", dateTime=" + dateTime
                + ", personId=" + personId
                + ", firstName='" + firstName + '\''
                + '}';
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
