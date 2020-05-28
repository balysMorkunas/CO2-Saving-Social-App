package model;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class FollowerTest {

    Follower test = new Follower(1, 4, 5);

    @org.junit.jupiter.api.Test
    void getId() {
        assertEquals(1, test.getId());
    }

    @org.junit.jupiter.api.Test
    void setId() {
        test.setId(4);
        assertEquals(4, test.getId());
    }

    @org.junit.jupiter.api.Test
    void getPersonId() {
        assertEquals(4, test.getPersonId());
    }

    @org.junit.jupiter.api.Test
    void setPersonId() {
        test.setPersonId(6);
        assertEquals(6,test.getPersonId());
    }

    @org.junit.jupiter.api.Test
    void getFollowerId() {
        assertEquals(5, test.getFollowerId());
    }

    @org.junit.jupiter.api.Test
    void setFollowerId() {
        test.setFollowerId(8);
        assertEquals(8, test.getFollowerId());
    }







}