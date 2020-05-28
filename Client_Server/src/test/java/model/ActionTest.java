package model;

import static org.junit.jupiter.api.Assertions.*;


class ActionTest {

    Action action = new Action(8, "vegan", "cool description",  5, "meal");

    @org.junit.jupiter.api.Test
    void getId() {
        assertEquals(8, action.getId());
    }

    @org.junit.jupiter.api.Test
    void setId() {
        action.setId(2);
        assertEquals(2, action.getId());
    }


    @org.junit.jupiter.api.Test
    void getType() {
        assertEquals("vegan", action.getType());
    }

    @org.junit.jupiter.api.Test
    void setType() {
        action.setType("transport");
        assertEquals("transport", action.getType());
    }


    @org.junit.jupiter.api.Test
    void getDescription() {
        assertEquals("cool description", action.getDescription());
    }


    @org.junit.jupiter.api.Test
    void setDescription() {
        action.setDescription("bicycle");
        assertEquals("bicycle", action.getDescription());
    }

    @org.junit.jupiter.api.Test
    void getPoints() {
        assertEquals(5, action.getPoints());
    }

    @org.junit.jupiter.api.Test
    void setPoints() {
        action.setPoints(3);
        assertEquals(3, action.getPoints());
    }

    @org.junit.jupiter.api.Test
    void getCategory() {
        assertEquals("meal", action.getCategory());
    }

    @org.junit.jupiter.api.Test
    void setCategory() {
        action.setCategory("transport");
        assertEquals("transport", action.getCategory());
    }

    @org.junit.jupiter.api.Test
    void getToString() {
        String toString = action.displayItems();
        assertTrue(toString.contains("points=" + 5));

    }

    @org.junit.jupiter.api.Test
    void emptyConstructor() {
        Action f = new Action();
        assertNotNull(f);
    }
}

