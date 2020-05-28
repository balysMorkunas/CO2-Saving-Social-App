package gogreenserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogreenserver.repository.*;
import model.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.google.gson.JsonObject;

import java.io.DataInput;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BasicController.class, secure = false)
public class BasicController3Test {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository personService;

    @MockBean
    private ActionRepository actionService;

    @MockBean
    private FoodLogRepository foodLogService;

    @MockBean
    private FollowerRepository followerService;

    @MockBean
    private AchievementRepository achievementRepository;

    @MockBean
    private AchievementLogRepository achievementLogRepository;

    @MockBean
    private VoteRepository voteService;

    Person test1 = new Person(1, "goerge@test.com","Gorge", "La", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 200);

    Person test2 = new Person(2, "blue@test.com","Blue", "Red", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 250);

    Person test3 = new Person(3,"test@hotmail.com", "Steve", "Smith", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 50);

    Person test4 = new Person(4,"carlemail@hotmail.com", "Carl", "Midge", "$2a$10$XH6Pj.z1YHEMNJ4hh/cfgupqEtU94vRlaNVFKdv1s8S91pe1XG2OO", 500);

    Person test5 = new Person(5,"beans@hotmail.com", "Elisabet", "Frits", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 70);

    Person test6 = new Person();

    Follower follow1 = new Follower(1, 1, 2);

    Follower follow2 = new Follower(2,1,3);

    Follower follow3 = new Follower();

    ActionLog testLog1 = new ActionLog(1, "vegetarian", "Eating a veg meal", 50, new Date(), 1, "Steve");

    ArrayList<Person> followerList = new ArrayList<Person>();

    ArrayList<ActionLog> actionLogsList = new ArrayList<>();

    @org.junit.jupiter.api.Test
    public void getFollowerListCorrect() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAllFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getFollowingList?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Person[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Person[].class);
        assertEquals(250, testList[0].getPoints());
    }

    @org.junit.jupiter.api.Test
    public void getFollowerListFalse() throws Exception {
        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAllFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getFollowingList?authKey=" + key.getAuthKey() + "false").accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Object result1 = result2.getResponse().getContentType();
        assertNull(result1);
    }

    @org.junit.jupiter.api.Test
    public void getFollowerListCorrectTwo() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAllFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getFollowingList?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Person[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Person[].class);
        assertEquals(300, testList[0].getPoints() + testList[1].getPoints());
    }
    @org.junit.jupiter.api.Test
    public void getActionLogOfFollowersTest() throws Exception {
        actionLogsList.add(testLog1);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when(foodLogService.findActionLogOfFollowers(Mockito.anyInt())).thenReturn(actionLogsList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getActionLogOfFollowers?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        ActionLog[] testList = mapper.readValue(result2.getResponse().getContentAsString(), ActionLog[].class);
        assertEquals("vegetarian", testList[0].getType());
    }

    @org.junit.jupiter.api.Test
    public void getActionLogOfFollowersTestNull() throws Exception {
        actionLogsList.add(testLog1);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when(foodLogService.findActionLogOfFollowers(Mockito.anyInt())).thenReturn(actionLogsList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getActionLogOfFollowers?authKey=" + key.getAuthKey() + "false").accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Object testList = result2.getResponse().getContentType();
        assertNull(testList);
    }

    @org.junit.jupiter.api.Test
    public void getSuggestedList() throws Exception {

        followerList.add(test4);
        followerList.add(test5);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAllNonFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getSuggestedList?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Person[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Person[].class);
        assertEquals(500, testList[0].getPoints());
    }

    @org.junit.jupiter.api.Test
    public void getSuggestedListCorrectTwo() throws Exception {

        followerList.add(test4);
        followerList.add(test5);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAllNonFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getSuggestedList?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Person[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Person[].class);
        assertEquals(570, testList[0].getPoints() + testList[1].getPoints());
    }

    @org.junit.jupiter.api.Test
    public void getSuggestedListFalse() throws Exception {
        followerList.add(test4);
        followerList.add(test5);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAllNonFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getSuggestedList?authKey=" + key.getAuthKey() + "false").accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Object result1 = result2.getResponse().getContentType();
        assertNull(result1);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerIDCorrect() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(test4);

        Mockito.when( personService.findAllFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        Mockito.when( followerService.findByPerson_idAndFollower_id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "4");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/follow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerIDFalse() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(test2);

        Mockito.when( personService.findAllFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        Mockito.when( followerService.findByPerson_idAndFollower_id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(follow1);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/follow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerIDFalse2() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/follow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerIDFalse3() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey() + "False");
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/follow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }


    @org.junit.jupiter.api.Test
    public void postUnfollowIDCorrect() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(test2);

        Mockito.when( followerService.findByPerson_idAndFollower_id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(follow1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/unfollow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postUnfollowIDCorrect2() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(test2);

        Mockito.when( followerService.findByPerson_idAndFollower_id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/unfollow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postUnfollowIDFalse() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/unfollow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postUnfollowIDFalse2() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("FollowingID", "2");
        json.addProperty("AuthKey", key.getAuthKey() + "False");
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/unfollow").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerEmailCorrect() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail("goerge@test.com")).thenReturn(test1);

        Mockito.when( personService.findByEmail("beans@hotmail.com")).thenReturn(test5);

        Mockito.when( followerService.findByPerson_idAndFollower_id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Email", "beans@hotmail.com");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/followEmail").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerEmailFalse() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail("goerge@test.com")).thenReturn(test1);

        Mockito.when( personService.findByEmail("blue@test.com")).thenReturn(test2);

        Mockito.when( followerService.findByPerson_idAndFollower_id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(follow1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Email", "blue@test.com");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/followEmail").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerEmailFalse2() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail("goerge@test.com")).thenReturn(test1);

        Mockito.when( personService.findByEmail("blue123@test.com")).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Email", "blue123@test.com");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/followEmail").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void postFollowerEmailFalse3() throws Exception {

        followerList.add(test2);
        followerList.add(test3);

        Mockito.when( personService.findByEmail("goerge@test.com")).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Email", "blue123@test.com");
        json.addProperty("AuthKey", key.getAuthKey() + "False");
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/followEmail").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void getFollowingListById() throws Exception {

        followerList.add(test4);
        followerList.add(test5);

        Mockito.when( personService.findAllFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);
        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getFollowingListById?id=" + 1).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Person[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Person[].class);
        assertEquals(500, testList[0].getPoints());
    }

    @org.junit.jupiter.api.Test
    public void getFollowingListByIdNull() throws Exception {
        followerList.add(test4);
        followerList.add(test5);

        Mockito.when( personService.findAllNonFollowerByPerson_id(Mockito.anyInt())).thenReturn(followerList);

        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getFollowingListById?id=" + "false").accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Object result1 = result2.getResponse().getContentType();
        assertNull(result1);
    }

    @org.junit.jupiter.api.Test
    public void getWhoFollowsIdTest() throws Exception {
        Mockito.when(personService.findWhoFollowsId(Mockito.anyInt())).thenReturn(1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getWhoFollowsId?id=1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        int count =  mapper.readValue(result.getResponse().getContentAsString(), Integer.class);

        assertEquals(1, count);
    }

    @org.junit.jupiter.api.Test
    public void getWhoFollowsIdTestNull() throws Exception {
        Mockito.when(personService.findWhoFollowsId(Mockito.anyInt())).thenReturn(-1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getWhoFollowsId?id=-1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        //int count =  mapper.readValue(result.getResponse().getContentAsString(), Integer.class);

        assertEquals("-1", result.getResponse().getContentAsString());
    }

    @org.junit.jupiter.api.Test
    public void verifyAchievementFalse() throws Exception {

        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        ArrayList<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(0, "Insane", 200000, "points"));
        Mockito.when(achievementRepository.findNotByPerson_id(Mockito.anyInt())).thenReturn(achievements);

        Mockito.when(followerService.countByPersonId_Followed(Mockito.anyInt())).thenReturn(0);
        Mockito.when(followerService.countByPersonId_Followers(Mockito.anyInt())).thenReturn(0);
        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getNewAchievements?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean newAchievement = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(newAchievement);
    }

    @org.junit.jupiter.api.Test
    public void verifyAchievementTrue() throws Exception {

        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        ArrayList<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(0, "Insane", 0, "points"));
        Mockito.when(achievementRepository.findNotByPerson_id(Mockito.anyInt())).thenReturn(achievements);

        Mockito.when(followerService.countByPersonId_Followed(Mockito.anyInt())).thenReturn(0);
        Mockito.when(followerService.countByPersonId_Followers(Mockito.anyInt())).thenReturn(0);
        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getNewAchievements?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean newAchievement = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(newAchievement);
    }

    @org.junit.jupiter.api.Test
    public void verifyAchievementAuthKey() throws Exception {

        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        ArrayList<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(0, "Insane", 0, "points"));
        Mockito.when(achievementRepository.findNotByPerson_id(Mockito.anyInt())).thenReturn(achievements);

        Mockito.when(followerService.countByPersonId_Followed(Mockito.anyInt())).thenReturn(0);
        Mockito.when(followerService.countByPersonId_Followers(Mockito.anyInt())).thenReturn(0);
        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getNewAchievements?authKey=FALSE" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean newAchievement = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(newAchievement);
    }

    @org.junit.jupiter.api.Test
    public void verifyAchievementOtherAchievements() throws Exception {
        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);
        test1.setImage("");
        ArrayList<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(0, "Insane", 0, "points"));
        achievements.add(new Achievement(0, "Insane", 0, "followers"));
        achievements.add(new Achievement(0, "Insane", 0, "friends"));
        achievements.add(new Achievement(0, "Insane", 0, "login"));
        achievements.add(new Achievement(0, "Insane", 0, "picture"));
        achievements.add(new Achievement(0, "Insane", 0, "bike"));

        Mockito.when(achievementRepository.findNotByPerson_id(Mockito.anyInt())).thenReturn(achievements);
        Mockito.when(foodLogService.countByPerson_idAndType(Mockito.anyInt(), Mockito.anyString())).thenReturn(5);
        Mockito.when(followerService.countByPersonId_Followed(Mockito.anyInt())).thenReturn(5);
        Mockito.when(followerService.countByPersonId_Followers(Mockito.anyInt())).thenReturn(5);
        Mockito.when(personService.findByEmail("goerge@test.com")).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getNewAchievements?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean newAchievement = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(newAchievement);
    }

    @org.junit.jupiter.api.Test
    public void getActionUpvotes() throws Exception {

        Mockito.when(voteService.countbyActionId(1)).thenReturn(5);
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getActionUpvotes?actionId=1").accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        int upvotes = mapper.readValue(result2.getResponse().getContentAsString(), Integer.class);
        assertEquals(5, upvotes);
    }

    @org.junit.jupiter.api.Test
    public void postUpvote() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("ActionId", "1");
        json.addProperty("Authkey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/postActionUpvotes").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean upVote = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(upVote);
    }

    @org.junit.jupiter.api.Test
    public void postUpvoteIncorrect() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("ActionId", "1");
        json.addProperty("Authkey", "False" + key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/postActionUpvotes").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean upVote = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(upVote);
    }

    @org.junit.jupiter.api.Test
    public void getUpvoted() throws Exception {
        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when(voteService.countbyActionIdAndPerson_Id(1, test1.getId())).thenReturn(1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getUpvoted?actionId=1&authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Boolean upvoted = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);
        assertFalse(upvoted);
    }

    @org.junit.jupiter.api.Test
    public void getUpvotedIncorrect2() throws Exception {
        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when(voteService.countbyActionIdAndPerson_Id(1, test1.getId())).thenReturn(0);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getUpvoted?actionId=1&authKey=FALSE" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Boolean upvoted = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);
        assertFalse(upvoted);
    }

    @org.junit.jupiter.api.Test
    public void getUpvotedCorrect() throws Exception {
        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when(voteService.countbyActionIdAndPerson_Id(1, test1.getId())).thenReturn(0);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getUpvoted?actionId=1&authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Boolean upvoted = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);
        assertTrue(upvoted);
    }
}
