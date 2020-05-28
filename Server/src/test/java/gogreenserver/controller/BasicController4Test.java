package gogreenserver.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import gogreenserver.repository.*;
import model.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
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
import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BasicController.class, secure = false)
public class BasicController4Test {

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

    Date dateTime = new Date();

    ActionLog actionLog = new ActionLog(0, "vegetarian", "Description", 50, dateTime, 5, "Bob");


    Follower follow1 = new Follower(1, 1, 2);

    Follower follow2 = new Follower(2,1,3);

    ArrayList<Person> followerList = new ArrayList<Person>();

    @org.junit.jupiter.api.Test
    public void changeHiddenTrue1() throws Exception {
        test1.setHidden(false);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        JsonObject json = new JsonObject();
        json.addProperty("Hidden", "true");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/hidden").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test //if it's already false in the person.getHidden and the mapping or if it's already true in the person.getHidden and the mapping
    public void changeHiddenFalse1() throws Exception {
        test1.setHidden(true);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        JsonObject json = new JsonObject();
        json.addProperty("Hidden", "true");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/hidden").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test //if hidden mapping is not true or false
    public void changeHiddenFalse2() throws Exception {
        test1.setHidden(true);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        JsonObject json = new JsonObject();
        json.addProperty("Hidden", "blue");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/hidden").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test //if authkey is false
    public void changeHiddenFalse3() throws Exception {
        test1.setHidden(true);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        JsonObject json = new JsonObject();
        json.addProperty("Hidden", "blue");
        json.addProperty("AuthKey", key.getAuthKey() + "False");
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/hidden").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test //if hidden mapping is not true or false
    public void changeHiddenFalse4() throws Exception {
        test1.setHidden(false);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        JsonObject json = new JsonObject();
        json.addProperty("Hidden", "false");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/hidden").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void changeHiddenTrue2() throws Exception {
        test1.setHidden(true);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        JsonObject json = new JsonObject();
        json.addProperty("Hidden", "false");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/hidden").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void stats() throws Exception {
        Mockito.when(foodLogService.findStatsByCategory("meal")).thenReturn(150 );
        Mockito.when(foodLogService.findStatsByCategory("transport")).thenReturn(50);
        Mockito.when(foodLogService.findStatsByCategory("household")).thenReturn(50);

        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/stats").accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Integer[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Integer[].class);


        assertEquals(150, testList[0].intValue());
        assertEquals(50, testList[1].intValue());
        assertEquals(50, testList[2].intValue());

    }

    @org.junit.jupiter.api.Test
    public void statsNull() throws Exception {
        Mockito.when(foodLogService.findStatsByCategory("meal")).thenReturn(null);
        Mockito.when(foodLogService.findStatsByCategory("transport")).thenReturn(null);
        Mockito.when(foodLogService.findStatsByCategory("household")).thenReturn(null);

        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/stats").accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Integer[] testList = mapper.readValue(result2.getResponse().getContentAsString(),  Integer[].class);


        assertEquals(0, testList[0].intValue());
        assertEquals(0, testList[1].intValue());
        assertEquals(0, testList[2].intValue());

    }







}

