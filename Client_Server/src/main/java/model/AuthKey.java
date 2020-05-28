package model;

public class AuthKey {
    String authKey;
    String email;

    public AuthKey(String authKey, String email) {
        this.authKey = authKey;
        this.email = email;
    }

    public AuthKey() {

    }

    public String getAuthKey() {
        return authKey;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "AuthKey{"
            + "authKey='"
            + authKey
            + '\''
            + ", email='"
            + email
            + '\''
            + '}';
    }
}
