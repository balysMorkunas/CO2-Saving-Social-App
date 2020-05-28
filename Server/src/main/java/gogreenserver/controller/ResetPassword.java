package gogreenserver.controller;

import model.Person;

import java.util.Date;

public class ResetPassword {
    private String token;
    private Date validUntil;
    private Person person;

    /**
     * Constructs a ResetPassword object, used for keeping track of the password change requests.
     * @param token The token of that identifies the request
     * @param validUntil The date until which this ResetPassword object is valid
     * @param person The person that wants his password to be changed
     */
    public ResetPassword(String token, Date validUntil, Person person) {
        this.token = token;
        this.validUntil = validUntil;
        this.person = person;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return "ResetPassword{"
                +
                "token='" + token + '\''
                +
                ", validUntil=" + validUntil
                +
                ", person=" + person
                +
                '}';
    }
}
