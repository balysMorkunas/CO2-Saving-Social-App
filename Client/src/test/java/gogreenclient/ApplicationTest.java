package gogreenclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import org.dom4j.IllegalAddException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;


import javax.security.auth.message.AuthException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class ApplicationTest {

    private MockRestServiceServer mockServer;

    Person testPerson = new Person(1,"test@hotmail.com","Dwight","Schrute", "$2a$10$lFeXOQy3eVW6lu5O9s.ogOdDkUQ9ZPuWP0vD6rCfqPJOVy.6M3vYu", 1000);
    Person testPerson2 = new Person(2,"test2@hotmail.com","Dwight2","Schrute", "$2a$10$lFeXOQy3eVW6lu5O9s.ogOdDkUQ9ZPuWP0vD6rCfqPJOVy.6M3vYu", 1000);
    ActionLog actionLog = new ActionLog(1, "vegetarian",  "cool description", 50, null, 5 ,"Bob");

    Action action = new Action(1, "vegetarian",  "cool description", 70, "meal");

    AchievementLog achievement = new AchievementLog(1, 2, 3);


    ArrayList<Person> personList = new ArrayList<>(Arrays.asList(testPerson, testPerson2));
    ArrayList<ActionLog> actionList = new ArrayList<>();

    @org.junit.jupiter.api.Test
    void loginCorrect() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());
        String jsonInString = mapper.writeValueAsString(key);

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();
        server.expect(manyTimes(), requestTo("https://localhost:8080/login?email=" + testPerson.getEmail() + "&password=" + testPerson.getPassword())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));


        assertEquals(testPerson.getEmail(), Application.login(testPerson.getEmail(), testPerson.getPassword()).getEmail());
    }

    @org.junit.jupiter.api.Test
    void loginNull() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = "";

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();
        server.expect(manyTimes(), requestTo("https://localhost:8080/login?email=" + testPerson.getEmail() + "&password=" + testPerson.getPassword())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertThrows(AuthException.class, () -> {
            Application.login(testPerson.getEmail(), testPerson.getPassword());
        });
    }

    @org.junit.jupiter.api.Test
    void getPerson() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(testPerson);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getPerson?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(testPerson.getEmail(), Application.getPerson(key).getEmail());
    }


    @org.junit.jupiter.api.Test
    void getPersonNull() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());
        String jsonInString = "";

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        server.expect(manyTimes(), requestTo("https://localhost:8080/getPerson?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertThrows(AuthException.class, () -> {
            Application.getPerson(key);
        });
    }

    @org.junit.jupiter.api.Test
    void getPersonListTest() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(personList);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getPersonList?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(personList.get(0).getEmail(), Application.getPersonList(key).get(0).getEmail());
    }

    @org.junit.jupiter.api.Test
    void getFoodLog() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        ArrayList<ActionLog> list = new ArrayList<>();
        list.add(actionLog);
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(list);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getActionLog?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(actionLog.getPoints(), Application.getActionLogs(key).get(0).getPoints());
    }

    @org.junit.jupiter.api.Test
    void getActions() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        ArrayList<Action> list = new ArrayList<>();
        list.add(action);
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(list);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getAction")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(action.getPoints(), Application.getActions()[0].getPoints());
    }

    @org.junit.jupiter.api.Test
    void getFoodLogEmpty() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        ArrayList<ActionLog> list = new ArrayList<>();
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(list);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getActionLog?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(0, Application.getActionLogs(key).size());
    }

    @org.junit.jupiter.api.Test
    void postFoodLogIncorrect() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Integer success = 0;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/actionLog")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertThrows(AuthException.class, () -> {
            Application.sendAction("vegetarian", "test", key, null);
        });
    }

    @org.junit.jupiter.api.Test
    void registerIncorrect() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/register")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        File file = null;
        assertThrows(IllegalArgumentException.class, () -> {
            Application.register("hi", "hi", "hi", "hi", file);
        });
    }

    @org.junit.jupiter.api.Test
    void register() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/register")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        File file = null;
        Application.register("hi", "hi", "hi", "hi", file);
    }

    @org.junit.jupiter.api.Test
    void registerWithImage() throws AuthException, JsonProcessingException {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File image = new File(classLoader.getResource("test.jpg").getFile());

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/register")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.register("hi", "hi", "hi", "hi", image);
    }

    @org.junit.jupiter.api.Test
    void registerWithWrongImage() throws AuthException, JsonProcessingException {

        File image = new File("hasdfjlfjasdklfjioasfdjsl.jpg");

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/register")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.register("hi", "hi", "hi", "hi", image);
    }

    @org.junit.jupiter.api.Test
    void postFoodLog() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Integer success = 1;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/actionLog")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.sendAction("vegetarian", "test", key, null);
    }

    @org.junit.jupiter.api.Test
    void postFoodLogWithImage() throws AuthException, JsonProcessingException {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File image = new File(classLoader.getResource("test.jpg").getFile());

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Integer success = 1;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/actionLog")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.sendAction("vegetarian", "test", key, image);
    }

    @org.junit.jupiter.api.Test
    void postFoodLogWithWrongImage() throws AuthException, JsonProcessingException {

        File image = new File("asdjkfaskdfklasd.jpg");

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Integer success = 1;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/actionLog")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.sendAction("vegetarian", "test", key, image);
    }

    @org.junit.jupiter.api.Test
    void getFollowingList() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        ArrayList<Person> list = new ArrayList<>();
        list.add(testPerson);
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(list);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getFollowingList?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(testPerson.getEmail(), Application.getFollowingList(key).get(0).getEmail());
    }

    @org.junit.jupiter.api.Test
    void getSuggestedList() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        ArrayList<Person> list = new ArrayList<>();
        list.add(testPerson);
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(list);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getSuggestedList?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(testPerson.getEmail(), Application.getSuggestedList(key).get(0).getEmail());
    }

    @org.junit.jupiter.api.Test
    void follow() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/follow")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.follow("2", key);
    }

    @org.junit.jupiter.api.Test
    void followInvalidAuth() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/follow")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        assertThrows(AuthException.class, () -> {
            Application.follow("2", key);
        });
    }

    @org.junit.jupiter.api.Test
    void followEmail() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/followEmail")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.followEmail("test2@hotmail.com", key);
    }

    @org.junit.jupiter.api.Test
    void followInvalidEmailAuth() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/followEmail")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        assertThrows(AuthException.class, () -> {
            Application.followEmail("test2@hotmail.com", key);
        });
    }

    @org.junit.jupiter.api.Test
    void hidden1() throws  AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/hidden")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.hidden("true", key);
    }

    @org.junit.jupiter.api.Test
    void hidden2() throws  AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/hidden")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.hidden("false", key);
    }

    @org.junit.jupiter.api.Test
    void hiddenInvalidAuth() throws  AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/hidden")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertThrows(AuthException.class, () -> {
            Application.hidden("blue", key);
        });
    }

    @org.junit.jupiter.api.Test
    void unfollowInvalidAuth() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/unfollow")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertThrows(AuthException.class, () -> {
            Application.unfollow("2", key);
        });
    }

    @org.junit.jupiter.api.Test
    void unfollow() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/unfollow")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.unfollow("2", key);
    }

    @org.junit.jupiter.api.Test
    void getFollowersActionsTest() throws AuthException, JsonProcessingException {

        actionList.add(actionLog);
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(actionList);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getActionLogOfFollowers?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(actionList.get(0).getFirstName(), Application.getFollowersActions(key).get(0).getFirstName());
    }

    @org.junit.jupiter.api.Test
    void requestPasswordChange() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/requestChange?email=" + testPerson.getEmail())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(true, Application.resetPassword(testPerson.getEmail()));
    }

    @org.junit.jupiter.api.Test
    void requestPasswordChangeFalse() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/requestChange?email=" + testPerson.getEmail())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(false, Application.resetPassword(testPerson.getEmail()));
    }

    @org.junit.jupiter.api.Test
    void getAchievement() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Achievement[] achievements = new Achievement[0];
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(achievements);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getAchievements")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(0, Application.getAchievements().length);
    }

    @org.junit.jupiter.api.Test
    void getAchievementLog() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        AchievementLog[] achievements = new AchievementLog[]{achievement};
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(achievements);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getAchievementLog?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))

                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(1, Application.getAchievementsLog(key).length);
    }
    @org.junit.jupiter.api.Test
    void getPersonByIdTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(testPerson);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getPersonById?id=" + testPerson.getId())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(testPerson.getId(), Application.getPersonById(testPerson.getId()).getId());
    }

    @org.junit.jupiter.api.Test
    void getPersonByIdTestNull() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = "";

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        server.expect(manyTimes(), requestTo("https://localhost:8080/getPersonById?id=" + testPerson.getId())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        assertEquals(null, Application.getPersonById(testPerson.getId()));

    }

    @org.junit.jupiter.api.Test
    void getFollowingListbyId() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(personList);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getFollowingListById?id=" + testPerson.getId())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));


        assertEquals(personList.get(0).getEmail(), Application.getFollowingListById(testPerson.getId()).get(0).getEmail());
    }

    @org.junit.jupiter.api.Test
    void getWhoFollowsId() throws AuthException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(2);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getWhoFollowsId?id=" + testPerson.getId())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertEquals(personList.size(), Application.getWhoFollowsId(testPerson.getId()));
    }

    @org.junit.jupiter.api.Test
    void updateImage() throws Exception{

        ObjectMapper mapper = new ObjectMapper();

        File image  = new File("C:\\Users\\Fly\\Desktop\\test.jpg");

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/updateImage")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        Application.updateImage(key, image);
    }

    @org.junit.jupiter.api.Test
    void updateImageNull() throws Exception{

        ObjectMapper mapper = new ObjectMapper();

        File image  = new File("C:\\Users\\Fly\\Desktop\\test.jpg");

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;

        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/updateImage")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        assertThrows(IllegalArgumentException.class, () -> {
            Application.updateImage(key, image);
        });
    }

    @org.junit.jupiter.api.Test
    void getStats() throws  Exception{
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<Integer> list = new ArrayList<>();
        list.add(150);
        list.add(50);
        list.add(50);
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(list);

        server.expect(manyTimes(), requestTo("https://localhost:8080/stats")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        int meal = Application.getStats().get(0);

        assertEquals(150, meal);
    }

    @org.junit.jupiter.api.Test
    void encodeFileNull() {
        File image = null;

        String imageStr = Application.encodeFile(image);
        assertEquals(null, imageStr);

    }


    @org.junit.jupiter.api.Test
    void postActionUpvote() throws AuthException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = true;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/postActionUpvotes")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertTrue(Application.postActionUpvote(key, "1"));
    }

    @org.junit.jupiter.api.Test
    void postActionUpvoteIncorrect() throws AuthException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/postActionUpvotes")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertThrows(AuthException.class, () -> {
            Application.postActionUpvote(key, "1");
        });
    }

    @org.junit.jupiter.api.Test
    void getAlreadyUpvotedTest() throws AuthException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getUpvoted?actionId=1" + "&authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertFalse(Application.getAlreadyUpvoted(key, "1"));
    }

    @org.junit.jupiter.api.Test
    void getNewAchievementTest() throws AuthException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Boolean success = false;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getNewAchievements?authKey=" + key.getAuthKey())).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));

        assertFalse(Application.getNewAchievement(key));
    }

    @org.junit.jupiter.api.Test
    void getActionUpvotesTest() throws AuthException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = new AuthKey("secretkey", testPerson.getEmail());

        Integer success = 5;
        MockRestServiceServer server = MockRestServiceServer.bindTo(Application.restTemplate).build();

        String jsonInString = mapper.writeValueAsString(success);

        server.expect(manyTimes(), requestTo("https://localhost:8080/getActionUpvotes?actionId=1")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonInString, MediaType.APPLICATION_JSON));
        Integer test = 5;
        assertEquals(test, Application.getActionUpvotes("1"));
    }
}
