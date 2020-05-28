package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"action_log_id", "person_id"})}
)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "action_log_id")
    private int actionLogId;

    @Column(name = "person_id")
    private int personId;

    /**
     * Constructor for class Vote.
     * @param id id of the vote.
     * @param actionLogId actionLog id on which the vote is being used for.
     * @param personId person who votes.
     */
    public Vote(int id, int actionLogId, int personId) {
        this.id = id;
        this.actionLogId = actionLogId;
        this.personId = personId;
    }

    public Vote() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionLogId() {
        return actionLogId;
    }

    public void setActionLogId(int actionLogId) {
        this.actionLogId = actionLogId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Vote{"
                + "id=" + id
                + ", actionLogId=" + actionLogId
                + ", personId=" + personId
                + '}';
    }
}

