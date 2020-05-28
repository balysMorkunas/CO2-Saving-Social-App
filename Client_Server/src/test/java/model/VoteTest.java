package model;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoteTest {
    Vote testVote = new Vote(1, 2, 5);

    @org.junit.jupiter.api.Test
    void getId() {
        Assertions.assertEquals(1, testVote.getId());
    }

    @org.junit.jupiter.api.Test
    void setId() {
        testVote.setId(2);
        Assertions.assertEquals(2, testVote.getId());
    }

    @org.junit.jupiter.api.Test
    void getActionLogId() {
        Assertions.assertEquals(2, testVote.getActionLogId());
    }

    @org.junit.jupiter.api.Test
    void setActionLogId() {
        testVote.setActionLogId(7);
        Assertions.assertEquals(7, testVote.getActionLogId());
    }

    @org.junit.jupiter.api.Test
    void getPersonId() {
        Assertions.assertEquals(5, testVote.getPersonId());
    }

    @org.junit.jupiter.api.Test
    void setPersonId() {
        testVote.setPersonId(11);
        Assertions.assertEquals(11, testVote.getPersonId());
    }

    @org.junit.jupiter.api.Test
    void emptyConstructor() {
        Vote vote = new Vote();
        assertNotNull(vote);
    }

    @org.junit.jupiter.api.Test
    void getToString() {
        String toString = testVote.toString();
        assertTrue(toString.contains("id=" + 1));
    }
}
