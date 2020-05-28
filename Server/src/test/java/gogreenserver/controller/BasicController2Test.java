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



import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BasicController.class, secure = false)
public class BasicController2Test {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository personService;

    @MockBean
    private ActionRepository actionService;

    @MockBean
    private FoodLogRepository foodLogService;

    @MockBean
    private FollowerRepository followerRepository;

    @MockBean
    private AchievementRepository achievementRepository;

    @MockBean
    private AchievementLogRepository achievementLogRepository;

    @MockBean
    private VoteRepository voteService;

    Person test1 = new Person(1, "goerge@test.com","Gorge", "La", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 200);
    Person test2 = new Person(2, "goerge2@test.com","Gorge2", "La2", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 200);

    Person test3 = new Person(3, "goerge3@test.com","Gorge3", "La3", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 200);


    Date dateTime = new Date();

    ActionLog actionLog = new ActionLog(0, "vegetarian", "Description", 50, dateTime, 5, "Bob");

    ActionLog actionLog2 = new ActionLog(0, "bike", "Description", 50, dateTime, 5, "Bob");


    Action action = new Action(0, "vegetarian", "Description", 70, "meal");

    Achievement achievement4 = new Achievement(0, "Rockie", 150, "points");

    Achievement achievement = new Achievement(0, "Rookie", 200, "points");

    Achievement achievement2 = new Achievement(0, "Rookies", 205, "points");

    Achievement achievement3 = new Achievement(0, "Rookiess", 210, "points");


    ArrayList<ActionLog> foodList = new ArrayList<ActionLog>();

    ArrayList<Action> actionList = new ArrayList<>();

    ArrayList<Achievement> achievements = new ArrayList<>();

    @org.junit.jupiter.api.Test
    public void getAchievement() throws Exception {
        achievements.add(achievement);
        Mockito.when(achievementRepository.findAll()).thenReturn(achievements);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAchievements").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Achievement[] achievements = mapper.readValue(result.getResponse().getContentAsString(), Achievement[].class);
        System.out.println(achievements.length);
        assertTrue(achievements.length > 0);
        assertEquals(200, achievements[0].getRequiredPoints());
    }

    @org.junit.jupiter.api.Test
    public void getAchievemenLog() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test3);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge3@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        achievements.add(achievement);
        Mockito.when(achievementRepository.findByPerson_id(Mockito.anyInt())).thenReturn(achievements);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getAchievementLog?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Achievement[] achievements = mapper.readValue(result2.getResponse().getContentAsString(), Achievement[].class);

        System.out.println(achievements.length);
        assertTrue(achievements.length > 0);
        assertEquals(200, achievements[0].getRequiredPoints());
    }

    @org.junit.jupiter.api.Test
    public void getAchievemenLogFalse() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test3);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge3@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        achievements.add(achievement);
        Mockito.when(achievementRepository.findByPerson_id(Mockito.anyInt())).thenReturn(achievements);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getAchievementLog?authKey=FALSEAUTHKEY" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        assertNull(result2.getResponse().getContentType());
    }

    @org.junit.jupiter.api.Test
    public void loginCorrectLogin() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        assertEquals("goerge@test.com", key.getEmail());
    }

    @org.junit.jupiter.api.Test
    public void loginNoPassword() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        assertThrows(org.springframework.web.util.NestedServletException.class, () -> {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?username=&password=").accept(MediaType.ALL);

            MvcResult result = mvc.perform(requestBuilder).andReturn();

            Object result1 = result.getResponse().getContentType();
        });
    }

    @org.junit.jupiter.api.Test
    public void loginNull() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=Wessel&password=Incorrect").accept(MediaType.ALL);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        Object result1 = result.getResponse().getContentType();


    }

    @org.junit.jupiter.api.Test
    public void loginWrongPassword() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=0000").accept(MediaType.ALL);

        MvcResult result = mvc.perform(requestBuilder).andReturn();


        Object result1 = result.getResponse().getContentType();

        assertNull(result1);
    }

    @org.junit.jupiter.api.Test
    public void getPersonAfterLogin() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        assertEquals("goerge@test.com", key.getEmail());

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getPerson?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Person person = mapper.readValue(result2.getResponse().getContentAsString(), Person.class);

        assertEquals(200, person.getPoints());
    }

    @org.junit.jupiter.api.Test
    public void getPersonAfterLoginWrongPassword() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getPerson?authKey=1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        Object result1 = result.getResponse().getContentType();
        assertNull(result1);
    }
    @org.junit.jupiter.api.Test
    public void getPersonListTest() throws Exception {

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(test1);
        personList.add(test2);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAll()).thenReturn(personList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getPersonList?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);


        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Person[] testList = mapper.readValue(result2.getResponse().getContentAsString(), Person[].class);

        assertEquals("Gorge Gorge2", testList[0].getFirstName() + " " + testList[1].getFirstName());
    }
    @org.junit.jupiter.api.Test
    public void getPersonListTestNull() throws Exception {

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(test1);
        personList.add(test2);

        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( personService.findAll()).thenReturn(personList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getPersonList?authKey=" + key.getAuthKey() + "false").accept(MediaType.APPLICATION_JSON);


        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Object testObj = result2.getResponse().getContentType();

        assertNull(testObj);
    }


    @org.junit.jupiter.api.Test
    public void postFoodLogCorrect() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "vegetarian");
        json.addProperty("Description", "vegetarian meal");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog2() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "bike");
        json.addProperty("Description", "riding with a bike: 20km");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog3() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "train");
        json.addProperty("Description", "Going with the train: 20km");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog4() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "bus");
        json.addProperty("Description", "Going with the bus: 20km");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }


    @org.junit.jupiter.api.Test
    public void postFoodLogFalse() throws Exception {


        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "vegetarian");
        json.addProperty("AuthKey", key.getAuthKey() + "fake");
        json.addProperty("Description", "test");
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertFalse(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void getFoodLogCorrect() throws Exception {
        foodList.add(actionLog);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( foodLogService.findByPerson_id(Mockito.anyInt())).thenReturn(foodList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "vegetarian");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getActionLog?authKey=" + key.getAuthKey()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        ActionLog[] testAction = mapper.readValue(result2.getResponse().getContentAsString(),  ActionLog[].class);
        assertEquals(50, testAction[0].getPoints());
      }

    @org.junit.jupiter.api.Test
    public void getFoodLogFalse() throws Exception {
        foodList.add(actionLog);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( foodLogService.findByPerson_id(Mockito.anyInt())).thenReturn(foodList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);


        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getActionLog?authKey=" + key.getAuthKey() + "false").accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        Object result1 = result2.getResponse().getContentType();
        assertNull(result1);
    }

    @org.junit.jupiter.api.Test
    public void getActions() throws Exception {
        actionList.add(action);

        Mockito.when( actionService.findAll()).thenReturn(actionList);

        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAction").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        Action[] testList = mapper.readValue(result.getResponse().getContentAsString(),  Action[].class);
        assertEquals(70, testList[0].getPoints());
    }

    @org.junit.jupiter.api.Test
    public void getActionEmpty() throws Exception {

        Mockito.when( actionService.findAll()).thenReturn(actionList);

        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAction").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        Action[] testList = mapper.readValue(result.getResponse().getContentAsString(),  Action[].class);
        assertEquals(0, testList.length);
    }

    @org.junit.jupiter.api.Test
    public void getPersonByIdTest() throws Exception {

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(test1);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/getPersonById?id=" + test1.getId()).accept(MediaType.APPLICATION_JSON);

        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();
        ObjectMapper mapper = new ObjectMapper();

        Person result = mapper.readValue(result2.getResponse().getContentAsString(), Person.class);

        assertEquals(1, result.getId());
    }

    @org.junit.jupiter.api.Test
    public void getPersonByIdTestNull() throws Exception {

        Mockito.when( personService.findById(Mockito.anyInt())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getPersonById?id=A").accept(MediaType.APPLICATION_JSON);

        MvcResult result1 = mvc.perform(requestBuilder).andReturn();
        Object results = result1.getResponse().getContentType();

        assertNull(results);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog5() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "solar");
        json.addProperty("Description", "2");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog6() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "heating");
        json.addProperty("Description", "2");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog7() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "weez");
        json.addProperty("Description", "2");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog8() throws Exception {

        achievements.add(achievement);
        achievements.add(achievement2);
        achievements.add(achievement3);
        achievements.add(achievement4);

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        Mockito.when(achievementRepository.findNotByPerson_id(Mockito.anyInt())).thenReturn(achievements);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "Train");
        json.addProperty("Description", "40");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();


        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog9() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        JsonObject json = new JsonObject();
        json.addProperty("Type", "solar");
        json.addProperty("Description", "1");
        json.addProperty("Image", "");
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();


        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }

    @org.junit.jupiter.api.Test
    public void sendActionLog10() throws Exception {

        Mockito.when( personService.findByEmail(Mockito.anyString())).thenReturn(test1);
        Mockito.when( actionService.findWithType(Mockito.anyString())).thenReturn(action);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?email=goerge@test.com&password=12345").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        AuthKey key = mapper.readValue(result.getResponse().getContentAsString(), AuthKey.class);
        String empty = null;
        JsonObject json = new JsonObject();
        json.addProperty("Type", "heating");
        json.addProperty("Description", "1");
        json.addProperty("Image", empty);
        json.addProperty("AuthKey", key.getAuthKey());
        String jsonAsString = json.toString();

        RequestBuilder requestBuilder2 = post("/actionLog").accept(MediaType.APPLICATION_JSON).content( jsonAsString);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        Boolean sendFood = mapper.readValue(result2.getResponse().getContentAsString(), Boolean.class);

        assertTrue(sendFood);
    }
}

