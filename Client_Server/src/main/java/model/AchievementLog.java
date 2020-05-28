package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AchievementLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int personId;

    private int achievementId;

    /**
     *  Constructor for AchievementLog.
     * @param id        The Id of the achievement.
     * @param personId  The Id of the person who accomplished the achievement.
     */
    public AchievementLog(int id, int personId, int achievementId) {
        this.id = id;
        this.personId = personId;
        this.achievementId = achievementId;
    }

    public AchievementLog() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    @Override
    public String toString() {
        return "AchievementLog{"
                + "id=" + id
                + ", personId=" + personId
                + '}';
    }
}
