package gogreenclient;

import model.Achievement;
import model.Action;
import model.ActionLog;
import model.AuthKey;
import model.Person;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.security.auth.message.AuthException;

/**
 * Main client class used for connecting with the server.
 */
public class Application {

    public static RestTemplate restTemplate;
    /**
     * KeyStore password.
     */
    private static String keyStorePassword = "appeltaart";

    /**
     * Creates a custom restemplate using the client.p12 keystore (needed to use HTTPS)
     * @return the custom resttempate
     * @throws Exception If something goes wrong.
     */
    @Bean
    public static RestTemplate restTemplate() {
        ClassPathResource classPathResource = new ClassPathResource("client.p12");
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts
                    .custom()
                    .loadTrustMaterial(classPathResource.getFile(),  keyStorePassword.toCharArray())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final CloseableHttpClient client = HttpClients
                .custom()
                .setSSLContext(sslContext)
                .build();
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
    }

    static {
        restTemplate = restTemplate();
    }

    /**
     * Connects to the server and checks if the username/password are correct.
     *
     * @param email The username of the user trying to login
     * @param password The password of the user trying to login
     * @throws AuthException           When the username and/or password are incorrect
     * @throws ResourceAccessException When we can't connect to the server
     */
    public static AuthKey login(String email, String password)
        throws AuthException, ResourceAccessException {

        AuthKey authKey = restTemplate.getForObject("https://localhost:8080/login?email=" + email + "&password=" + password, AuthKey.class);

        if (authKey != null) {
            return authKey;
        } else {
            throw new AuthException("Incorrect password or username");
        }
    }

    /**
     * Register method for registering a user to the database.
     *
     * @param password  The pasword of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email of the user.
     * @throws IllegalArgumentException When one of the person details provided are incorrect
     */
    public static void register(String password,
                                String firstName, String lastName,
                                String email, File image) throws IllegalArgumentException {

        Person newPerson = new Person(0, email, firstName, lastName, password, 0);
        newPerson.setHidden(false);
        if (image != null) {
            newPerson.setImage(encodeFile(image));
        } else {
            newPerson.setImage(null);
        }

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/register", newPerson, Boolean.class);
        if (!response.getBody()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Method used for encoding an image.
     * @param image Image file
     * @return Encoded image string,
     */
    public static String encodeFile(File image) {
        try {
            if (image != null) {
                byte[] fileContent = FileUtils.readFileToByteArray(image);
                return Base64.getEncoder().encodeToString(fileContent);
            }
        } catch (IOException e) {
            System.out.println("No file provided");
        }
        return null;
    }

    /**
     * Updating a persons profile picture.
     *
     * @param image     The picture of a user
     * @throws IllegalArgumentException When one of the person details provided are incorrect
     */
    public static void updateImage(AuthKey authKey, File image) throws IllegalArgumentException {
        Map<String, String> map = new HashMap<>();
        map.put("Image",  encodeFile(image));//request parameters
        map.put("AuthKey", authKey.getAuthKey());

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/updateImage", map, Boolean.class);
        if (!response.getBody()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a model object the a user with the given username.
     *
     * @param authKey A valid authKey object
     * @return A model object
     * @throws AuthException           When the authkey is invalid
     * @throws ResourceAccessException When we can't connect to the server
     */
    public static Person getPerson(AuthKey authKey)
        throws AuthException, ResourceAccessException {
        Person person = restTemplate.getForObject("https://localhost:8080/getPerson?authKey=" + authKey.getAuthKey(), Person.class);

        if (person != null) {
            return person;
        } else {
            throw new AuthException("Auth key failed, user needs to login again");
        }
    }
    /**
     * Returns Person object by the given Id.
     * @param id the given Id.
     * @return Person Object.
     * @throws ResourceAccessException When can't connect to the server.
     */

    public static Person getPersonById(int id) {
        Person person = restTemplate.getForObject("https://localhost:8080/getPersonById?id=" + id, Person.class);
        if (person != null) {
            return person;
        } else {
            return null;
        }
    }
    
    /**
     * Method for getting all of the users YOU are FOLLOWING By ID.
     * @param id the id of the user you want to see followers.
     * @return ArrayList of Person's
     * @throws ResourceAccessException When can't reach the serer.
     */

    public static ArrayList<Person> getFollowingListById(int id)
            throws ResourceAccessException {
        ResponseEntity<ArrayList<Person>> response = restTemplate.exchange("https://localhost:8080/getFollowingListById?id=" + id , HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Person>>() {} );
        ArrayList<Person> list = response.getBody();
        return list;
    }

    /**
     * Method gets the number of people who follow given id.
     * @param id the id of the person.
     * @return ArrayList of type Person.
     * @throws ResourceAccessException when cannot reach the server.
     */
    public static int getWhoFollowsId(int id)
            throws ResourceAccessException {
        Integer response = restTemplate.getForObject("https://localhost:8080/getWhoFollowsId?id=" + id, Integer.class);

        return response;
    }

    /**
     * Sends a reset password request to the server.
     *
     * @param email The email adress of the user
     * @return False when resetrequest failed, true if everything went correct.
     */
    public static Boolean resetPassword(String email) {
        Boolean success = restTemplate.getForObject("https://localhost:8080/requestChange?email=" + email, Boolean.class);
        if (success) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a list of all persons that are in the db.
     * @param authKey A valid authkey
     * @return An arraylist with all persons in the db
     * @throws ResourceAccessException When we can't connect to the server.
     */
    public static ArrayList<Person> getPersonList(AuthKey authKey)
        throws ResourceAccessException {
        ResponseEntity<ArrayList<Person>> response = restTemplate.exchange("https://localhost:8080/getPersonList?authKey=" + authKey.getAuthKey() , HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Person>>() {} );
        ArrayList<Person> list = response.getBody();
        return list;
    }

    /**
     * Method for getting all of the users YOU are FOLLOWING.
     *
     * @param authKey The autKey of the user
     * @return An ArrayList of all the users you do follow.
     * @throws ResourceAccessException When can't connect to server.
     */
    public static ArrayList<Person> getFollowingList(AuthKey authKey)
            throws ResourceAccessException {
        ResponseEntity<ArrayList<Person>> response = restTemplate.exchange("https://localhost:8080/getFollowingList?authKey=" + authKey.getAuthKey() , HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Person>>() {} );
        ArrayList<Person> list = response.getBody();
        return list;
    }

    /**
     * Method for getting all of the users YOU are NOT FOLLOWING.
     *
     * @param authKey The autKey for the user
     * @return An ArrayList of all the users you DO NOT follow.
     * @throws ResourceAccessException When can't connect to server.
     */
    public static ArrayList<Person> getSuggestedList(AuthKey authKey)
            throws ResourceAccessException {
        ResponseEntity<ArrayList<Person>> response = restTemplate.exchange("https://localhost:8080/getSuggestedList?authKey=" + authKey.getAuthKey() , HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Person>>() {} );
        ArrayList<Person> list = response.getBody();
        return list;
    }

    /**
     * Method that sends an user ID to server, it gets removed from the table of people you follow.
     * @param authKey    The authentication key from the user.
     * @param followingId The ID of the person you remove from the people you follow.
     * @throws AuthException When the provided authKey is incorrect or invalid
     */
    public static void unfollow(String followingId, AuthKey authKey) throws AuthException {
        Map<String, String> map = new HashMap<>();
        map.put("FollowingID", followingId);//request parameters
        map.put("AuthKey", authKey.getAuthKey());

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/unfollow", map, Boolean.class);
        if (!response.getBody()) {
            throw new AuthException();
        }
    }

    /**
     * Method that sends an user ID to server, it gets added to the table of people you follow.
     *
     * @param authKey    The authentication key from the user.
     * @param followingId The ID of the person you add to the people you follow.
     * @throws AuthException When the provided authKey is incorrect or invalid
     */
    public static void follow(String followingId, AuthKey authKey) throws AuthException {
        Map<String, String> map = new HashMap<>();
        map.put("FollowingID", followingId);//request
        map.put("AuthKey", authKey.getAuthKey());

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/follow", map, Boolean.class);
        if (!response.getBody()) {
            throw new AuthException();
        }
    }

    /**
     * Method fow following a user by email.
     * @param email The specified email string
     * @param authKey Authentication key
     * @throws AuthException If authorization error.
     */
    public static void followEmail(String email, AuthKey authKey) throws AuthException {
        Map<String, String> map = new HashMap<>();
        map.put("Email", email);
        map.put("AuthKey", authKey.getAuthKey());

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/followEmail", map, Boolean.class);
        if (!response.getBody()) {
            throw new AuthException();
        }
    }

    /**
     * Method that changes user profile to private or not.
     * @param hidden Boolean.
     * @param authKey The authentication key from the user.
     * @throws AuthException When the autkey is incorrect.
     */
    public static void hidden(String hidden, AuthKey authKey) throws AuthException {
        Map<String, String> map = new HashMap<>();
        map.put("Hidden", hidden);
        map.put("AuthKey", authKey.getAuthKey());

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/hidden", map, Boolean.class);
        if (!response.getBody()) {
            throw new AuthException();
        }
    }

    /**
     * Method that sends an actionLog object as json to the server.
     *
     * @param actionType The type of actionLog.
     * @param authKey    The authentication key from the user.
     * @throws AuthException When the provided authKey is incorrect or invalid
     */
    public static void sendAction(String actionType, String description,
                                  AuthKey authKey, File image)
            throws AuthException {

        Map<String, String> map = new HashMap<>();
        map.put("Type", actionType);//request parameters
        map.put("AuthKey", authKey.getAuthKey());
        map.put("Description", description);
        String encodedString = null;

        try {
            if (image != null) {
                byte[] fileContent = FileUtils.readFileToByteArray(image);
                encodedString = Base64.getEncoder().encodeToString(fileContent);
            }
        } catch (IOException e) {
            System.out.println("No file provided");
        }

        map.put("Image", encodedString);

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/actionLog", map, Boolean.class);
        if (!response.getBody()) {
            throw new AuthException();
        }
    }


    /**
     * The stats for the chart. Index 0 is for meal, index 1 is for transport
       and 2 is for household.
     * @return return an arrayList of integer.
     */
    public static ArrayList<Integer> getStats() {
        ResponseEntity<ArrayList<Integer>> response = restTemplate.exchange("https://localhost:8080/stats", HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Integer>>() {
        });
        return response.getBody();
    }

    /**
     * The method for getting ActionLogs from a specific user from the server (database).
     *
     * @param authKey The authentication key from the user.
     * @return An array of ActionLog objects.
     * @throws AuthException           The authentication key has expired or is invalid.
     * @throws ResourceAccessException The specific person with this authentication key
     *                                 is not allowed to access the data.
     */
    public static ArrayList<ActionLog> getActionLogs(AuthKey authKey)
        throws ResourceAccessException {
        ResponseEntity<ArrayList<ActionLog>> response = restTemplate.exchange("https://localhost:8080/getActionLog?authKey=" + authKey.getAuthKey(), HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<ActionLog>>() {} );
        return response.getBody();
    }

    /**
     * Method for getting the actions of your followers.
     *
     * @param authKey Used for identifying a user.
     * @return Returns an ArrayList of actions which have been made by your followers.
     */
    public static ArrayList<ActionLog> getFollowersActions(AuthKey authKey)
        throws ResourceAccessException {
        ResponseEntity<ArrayList<ActionLog>> response = restTemplate.exchange("https://localhost:8080/getActionLogOfFollowers?authKey=" + authKey.getAuthKey(), HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<ActionLog>>() {} );
        return  response.getBody();
    }

    /**
     * Gets all actions currently stored in the DB.
     *( These are the global ones, not related to a person)
     *
     * @return An array of all Action objects in the DB.
     */
    public static Action[] getActions() {
        ResponseEntity<Action[]> response = restTemplate.getForEntity("https://localhost:8080/getAction", Action[].class);
        return response.getBody();
    }

    /**
     * Gets all achievements currently stored in the DB.
     * (These are global, not related to any person)
     * @return An array of Achievement objects.
     */
    public static Achievement[] getAchievements() {
        ResponseEntity<Achievement[]> response = restTemplate.getForEntity("https://localhost:8080/getAchievements" , Achievement[].class);
        return response.getBody();
    }

    /**
     * Checks if someone already upvoted this action.
     *
     * @param actionId The action id of the action.
     * @param authKey the authkey of the person.
     * @return True if the action was already upvoted, false if it was not.
     */
    public static Boolean getAlreadyUpvoted(AuthKey authKey, String actionId) {
        ResponseEntity<Boolean> response = restTemplate.getForEntity("https://localhost:8080/getUpvoted?actionId=" + actionId + "&authKey=" + authKey.getAuthKey(), Boolean.class);
        return response.getBody();
    }

    /**
     * Gets the amount of upvotes a certain action has.
     *
     * @param actionId The action that you want to count the upvotes of.
     * @return The amount of upvotes of a certain action.
     */
    public static Integer getActionUpvotes(String actionId) {
        ResponseEntity<Integer> response = restTemplate.getForEntity("https://localhost:8080/getActionUpvotes?actionId=" + actionId, Integer.class);
        return response.getBody();
    }

    /**
     * Upvote an action.
     * @param authKey The authkey of the person upvoting the action.
     * @param action The actionid the person wants to upvote.
     * @return True if everything when ok, false if user already upvoted the action.
     */
    public static Boolean postActionUpvote(AuthKey authKey, String action)
            throws AuthException {

        Map<String, String> map = new HashMap<>();
        map.put("ActionId", "" + action);
        map.put("Authkey", authKey.getAuthKey());

        ResponseEntity<Boolean> response = restTemplate.postForEntity("https://localhost:8080/postActionUpvotes", map, Boolean.class);
        if (!response.getBody()) {
            throw new AuthException();
        }
        return true;
    }


    /**
     * Checks if a person has gotten any new achievements.
     * @param authKey the authkey of the person.
     * @return True if the person got a new achievement, otherwise false.
     * @throws ResourceAccessException If recource is not found.
     */
    public static Boolean getNewAchievement(AuthKey authKey)
            throws ResourceAccessException {

        ResponseEntity<Boolean> response = restTemplate.exchange("https://localhost:8080/getNewAchievements?authKey=" + authKey.getAuthKey(), HttpMethod.GET, null, Boolean.class );
        return response.getBody();
    }


    /**
     *  Gets all achievements a person has accomplished.
     *
     * @param authKey Used for identifying a user.
     * @return Returns an Array of Achievements.
     * @throws ResourceAccessException When we can't connect to the server.
     */
    public static Achievement[] getAchievementsLog(AuthKey authKey)
        throws ResourceAccessException {

        ResponseEntity<Achievement[]> response = restTemplate.exchange("https://localhost:8080/getAchievementLog?authKey=" + authKey.getAuthKey(), HttpMethod.GET, null, Achievement[].class );

        return response.getBody();
    }
}
