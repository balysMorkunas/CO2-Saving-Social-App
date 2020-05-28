package gogreenserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogreenserver.repository.*;
import model.AuthKey;
import model.Person;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BasicController.class, secure = false)
public class BasicControllerPasswordResetTest {

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

    ResetPassword resetPassword = new ResetPassword("123", new Date(), test1);

    @org.junit.jupiter.api.BeforeAll
    public static void setUp() {
        Mailbox.clearAll();
    }

    @org.junit.jupiter.api.Test
    public void requestPasswordChange() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/changePassword?secreturl=" + resetPassword.getToken()).accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("Welcome, Enter your new password"));
        assertTrue(result.getResponse().getContentAsString().contains( "<input type=\"hidden\"  name=\"secretkey\" value=\"" + resetPassword.getToken() + "\">\n"));
    }

    @org.junit.jupiter.api.Test
    public void testSendInRegualarJavaMail() throws MessagingException, IOException, javax.mail.MessagingException, NoSuchProviderException, Exception {
        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requestChange?email=" + test1.getEmail()).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Boolean success = mapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertTrue(success);

        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("pop3");
        store.connect("test.com", "goerge", "password");

        Folder folder = store.getFolder("inbox");

        folder.open(Folder.READ_ONLY);
        Message[] msg = folder.getMessages();

        assertEquals(1, msg.length);
        assertEquals("Reset Password", msg[0].getSubject());
        assertTrue(msg[0].getContent().toString().contains("Dear User,"));
        folder.close(true);
        store.close();
    }

    @org.junit.jupiter.api.Test
    public void testNoPerson() throws MessagingException, IOException, javax.mail.MessagingException, NoSuchProviderException, Exception {
        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requestChange?email=" + test1.getEmail()).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Boolean success = mapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertTrue(success);
    }

    @org.junit.jupiter.api.Test
    public void changePassword() throws MessagingException, IOException, javax.mail.MessagingException, NoSuchProviderException, Exception {
        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requestChange?email=" + test1.getEmail()).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Boolean success = mapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertTrue(success);

        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("pop3");
        store.connect("test.com", "goerge", "password");

        Folder folder = store.getFolder("inbox");

        folder.open(Folder.READ_ONLY);
        Message[] msg = folder.getMessages();

        String code = msg[0].getContent().toString().split("=")[1];

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/resetpassword?password=123&secretkey=" + code).accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        assertTrue(result2.getResponse().getContentAsString().contains("Password changed."));

        folder.close(true);
        store.close();
    }

    @org.junit.jupiter.api.Test
    public void changePasswordTooLate() throws MessagingException, IOException, javax.mail.MessagingException, NoSuchProviderException, Exception {
        Mockito.when(personService.findByEmail(Mockito.anyString())).thenReturn(test1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requestChange?email=" + test1.getEmail()).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Boolean success = mapper.readValue(result.getResponse().getContentAsString(), Boolean.class);

        assertTrue(success);

        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("pop3");
        store.connect("test.com", "goerge", "password");

        Folder folder = store.getFolder("inbox");

        folder.open(Folder.READ_ONLY);
        Message[] msg = folder.getMessages();

        String code = msg[0].getContent().toString().split("=")[1];

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/resetpassword?password=123&secretkey=1" + code).accept(MediaType.APPLICATION_JSON);
        MvcResult result2 = mvc.perform(requestBuilder2).andReturn();

        assertTrue(result2.getResponse().getContentAsString().contains("Token expired, please request a new link."));

        folder.close(true);
        store.close();
    }
}
