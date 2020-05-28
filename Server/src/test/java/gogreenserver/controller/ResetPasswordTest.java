package gogreenserver.controller;


import model.Person;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResetPasswordTest {
    Person testPerson = new Person(1,"test@hotmail.com","Dwight","Schrute", "123", 1000);
    long time = new Date().getTime();
    Date expirationTime = new Date(time + 60000);
    String generatedString = RandomStringUtils.randomAlphanumeric(20);

    ResetPassword resetPassword = new ResetPassword(generatedString, expirationTime, testPerson);

    @org.junit.jupiter.api.Test
    void getToken() {
        assertEquals(generatedString, resetPassword.getToken());
    }

    @org.junit.jupiter.api.Test
    void setToken() {
        resetPassword.setToken("token");
        assertEquals("token", resetPassword.getToken());
    }

    @org.junit.jupiter.api.Test
    void getDate() {
        assertEquals(expirationTime, resetPassword.getValidUntil());
    }

    @org.junit.jupiter.api.Test
    void setDate() {
        Date expirationTime2 = new Date(time);
        resetPassword.setValidUntil(expirationTime2);
        assertEquals(expirationTime2, resetPassword.getValidUntil());
    }

    @org.junit.jupiter.api.Test
    void getPerson() {
        assertEquals(testPerson, resetPassword.getPerson());
    }
}
