package model;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementTest {
    Achievement testAchievement = new Achievement(1,"Earn 1000 points", 1000, "points");


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
    void getName() {
        Assertions.assertEquals("Earn 1000 points", testAchievement.getName());
    }

    @org.junit.jupiter.api.Test
    void setName() {
        testAchievement.setName("Earn 10000 points");
        Assertions.assertEquals("Earn 10000 points", testAchievement.getName());
    }

    @org.junit.jupiter.api.Test
    void getRequiredPoints() {
        Assertions.assertEquals(1000, testAchievement.getRequiredPoints());
    }

    @org.junit.jupiter.api.Test
    void setRequiredPoints() {
        testAchievement.setRequiredPoints(200);
        Assertions.assertEquals(200, testAchievement.getRequiredPoints());
    }

    @org.junit.jupiter.api.Test
    void getToString() {
        String toString = testAchievement.toString();
        assertTrue(toString.contains("id=" + 1));
    }

    @org.junit.jupiter.api.Test
    void getType() {
        assertEquals("points", testAchievement.getType());
    }

    @org.junit.jupiter.api.Test
    void setType() {
        testAchievement.setType("picture");
        assertEquals("picture", testAchievement.getType());
    }

    @org.junit.jupiter.api.Test
    void emptyConstructor() {
        Achievement achievement = new Achievement();
        assertNotNull(achievement);
    }
}
