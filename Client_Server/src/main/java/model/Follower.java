package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "person_id")
    private int personId;

    @Column(name = "follower_id")
    private int followerId;

    /**
     * The constructor for follower class, creates an follower object.
     * @param id The id for database
     * @param personId The id of the person
     * @param followerId The id of the person that user is following
     */
    public Follower(int id, int personId, int followerId) {
        this.id = id;
        this.personId = personId;
        this.followerId = followerId;
    }

    public Follower() {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return this.personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getFollowerId() {
        return this.followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

}
