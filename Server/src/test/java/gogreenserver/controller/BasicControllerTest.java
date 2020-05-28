package gogreenserver.controller;

import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import static org.junit.jupiter.api.Assertions.*;

class BasicControllerTest {


    @org.junit.jupiter.api.Test
    void bcrypthashdefault() {
        String hashpwd = BasicController.hashPassword("12345");
        Boolean match = BasicController.matchHash("12345", hashpwd);
        assertTrue(match);
    }

    @org.junit.jupiter.api.Test
    void bcrypthashdefault2() {
        String hashpwd = BasicController.hashPassword("12345");
        Boolean match = BasicController.matchHash("123456", hashpwd);
        assertFalse(match);
    }

    @org.junit.jupiter.api.Test
    void bcrypthashnull() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicController.hashPassword(null);
        });
    }

    @org.junit.jupiter.api.Test
    void bcrypthashempty() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicController.hashPassword("");
        });
    }

    @org.junit.jupiter.api.Test
    void bcrypthash_hash_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicController.matchHash("xyz", null);
        });
    }

    @org.junit.jupiter.api.Test
    void bcrypthash_password_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicController.matchHash(null, "xyz");
        });
    }

    @org.junit.jupiter.api.Test
    void bcrypthash_hash_empty() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicController.matchHash("xyz", "");
        });
    }

    @org.junit.jupiter.api.Test
    void bcrypthash_password_empty() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicController.matchHash("", "xyz");
        });
    }

    @org.junit.jupiter.api.Test
    void generateJWTTest() {
        Date date = new Date();
        long t = date.getTime();
        Date expirationTime = new Date(t + 10000);

        String token = BasicController.generateToken("Token", expirationTime);

        Claims claim = BasicController.decodeJwt(token);
        assertEquals("Token", claim.getSubject());
    }

    @org.junit.jupiter.api.Test
    void generateJWTTestExpired() {
        Date date = new Date();
        long t = date.getTime();
        Date expirationTime = new Date(t - 10);

        String token = BasicController.generateToken("Token", expirationTime);

         assertThrows(SignatureException.class, () -> {
            Claims claim = BasicController.decodeJwt(token);
            claim.getSubject();
        });
    }
}
