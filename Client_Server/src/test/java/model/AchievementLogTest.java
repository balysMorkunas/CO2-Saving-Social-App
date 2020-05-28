package model;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AchievementLogTest {
    AchievementLog testAchievement = new AchievementLog(1,2, 2);

    @org.junit.jupiter.api.Test
    void getId() {
        Assertions.assertEquals(1, testAchievement.getId());
    }

    @org.junit.jupiter.api.Test
    void setId() {
        testAchievement.setId(2);
        Assertions.assertEquals(2, testAchievement.getId());
    }

    @org.junit.jupiter.api.Test
    void getPersonId() {
        Assertions.assertEquals(2, testAchievement.getPersonId());
    }

    @org.junit.jupiter.api.Test
    void setPersonId() {
        testAchievement.setPersonId(3);
        Assertions.assertEquals(3, testAchievement.getPersonId());
    }

    @org.junit.jupiter.api.Test
    void getToString() {
        String toString = testAchievement.toString();
        assertTrue(toString.contains("id=" + 1));
    }

    @org.junit.jupiter.api.Test
    void emptyConstructor() {
        AchievementLog achievementLog = new AchievementLog();
        assertNotNull(achievementLog);
    }

    @org.junit.jupiter.api.Test
    void getAchievementId() {
        Assertions.assertEquals(2, testAchievement.getAchievementId());
    }

    @org.junit.jupiter.api.Test
    void setAchiementId() {
        testAchievement.setAchievementId(5);
        Assertions.assertEquals(5, testAchievement.getAchievementId());
    }
}
