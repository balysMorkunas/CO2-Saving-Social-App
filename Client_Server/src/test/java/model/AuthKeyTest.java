package model;

import static org.junit.jupiter.api.Assertions.*;

public class AuthKeyTest {

    AuthKey testAuthKey = new AuthKey("authkey", "test@gmail.com");

    @org.junit.jupiter.api.Test
    void getAuthKey() {
        assertEquals( "authkey", testAuthKey.getAuthKey());
    }

    @org.junit.jupiter.api.Test
    void getEmail() {
        assertEquals( "test@gmail.com", testAuthKey.getEmail());
    }

    @org.junit.jupiter.api.Test
    void testEmptyConstructor() {
        AuthKey emptyAuthKey = new AuthKey();
        assertNull(emptyAuthKey.getEmail());
    }
    @org.junit.jupiter.api.Test
    void getToString() {
        String toString = testAuthKey.toString();
        assertTrue(toString.contains("email='" + testAuthKey.getEmail() + "'"));
    }

}
