package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * model.Person class for application users.
 */
@Entity
public class Person {

    /**
     * Variable is used for sorting the PersonList in the Leaderboards.
     */
    public static Comparator<Person> pointComparator = new Comparator<Person>() {
        /**
         * Method compares two integer values.
         *
         * @param o1 Object this of comparison.
         * @param o2 Object other of comparison.
         * @return Returns the difference of points.
         */
        public int compare(Person o1, Person o2) {
            int pointsFirst = o1.getPoints();
            int pointsSecond = o2.getPoints();

            return pointsSecond - pointsFirst;
        }
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    private int points;

    @Column(name = "Hidden")
    private Boolean hidden;

    @Transient
    private String image;

    @Transient
    private File realImage;

    /**
     * Constructor for model.Person object.
     * @param id The id of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param password The password of the user (to be changed?)
     * @param points The total points of the user.
     */
    public Person(int id, String email,
                  String firstName, String lastName, String password, int points) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.points = points;
    }

    public Person() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Saves a temp image of a given person to the SSD/HDD.
     * @return The path of the file on disk
     */
    @JsonIgnore
    public File getRealImage() {
        // if (getImage().length() < 1){
        //     return null;
        // }
        System.out.println("Image: " + getImage());
        new File(System.getProperty("user.home") + "\\test\\").mkdirs();
        File file = new File(System.getProperty("user.home") + "\\test\\", getId() + ".jpg");
        try {
            file.createNewFile();
            byte[] decodedBytes = Base64.getDecoder().decode(getImage());
            FileUtils.writeByteArrayToFile(file, decodedBytes);
            file.deleteOnExit();
        } catch (IOException | NullPointerException e) {
            return null;
        }
        return file;
    }

    @Override
    public String toString() {
        return "Person{"
            + "id=" + id
            + ", email='" + email + '\''
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", password='" + password + '\''
            + ", points=" + points
            + '}';
    }
}
