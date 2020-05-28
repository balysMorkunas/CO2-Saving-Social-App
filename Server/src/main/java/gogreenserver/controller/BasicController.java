package gogreenserver.controller;

import gogreenserver.repository.AchievementLogRepository;
import gogreenserver.repository.AchievementRepository;
import gogreenserver.repository.ActionRepository;
import gogreenserver.repository.FollowerRepository;
import gogreenserver.repository.FoodLogRepository;
import gogreenserver.repository.UserRepository;
import gogreenserver.repository.VoteRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import model.Achievement;
import model.AchievementLog;
import model.Action;
import model.ActionLog;
import model.AuthKey;
import model.Follower;
import model.Person;
import model.Vote;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.security.Key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@RestController
public class BasicController {
    
    static Key key = MacProvider.generateKey();

    @Autowired
    AchievementRepository achievementService;

    @Autowired
    UserRepository personService;

    @Autowired
    FoodLogRepository foodLogService;

    @Autowired
    ActionRepository actionService;

    @Autowired
    AchievementLogRepository achievementLogService;

    @Autowired
    FollowerRepository followService;

    @Autowired
    VoteRepository voteService;

    HashMap<String, ResetPassword> passwordReset = new HashMap<String, ResetPassword>();

    /**
     * Verifies a given jwt token and returns the claims.
     * @param jwt The jwt key to be verified
     * @return the claims that are in the jwt token
     * @throws SignatureException thrown when the jwt ke is invalid
     */
    public static Claims decodeJwt(String jwt) throws SignatureException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt).getBody();
            return claims;
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.ExpiredJwtException ex) {
            throw new SignatureException("Failed");
        }
    }

    /**
     * Returns model of given username when correct username is provided.
     * @param authKey authKey of the user
     * @return model object
     */
    @RequestMapping("/getPerson")
    public Person getPerson(@RequestParam(value = "authKey", defaultValue = "") String authKey) {
        try {
            Claims claims = decodeJwt(authKey);

            Person person = personService.findByEmail(claims.getSubject());
            person.setImage(getEncodedImagePerson(person));
            return person;
        } catch (SignatureException e) {
            return null;
        }
    }

    /**
     * Method for getting overall statistics for different categories.
     * @return Array list with meal, transport, household savings respectively.
     */
    @GetMapping("/stats")
    public ArrayList<Integer> getStats() {
        Map<String, Integer> map = new HashMap<>();

        Integer meal = foodLogService.findStatsByCategory("meal");
        Integer transport = foodLogService.findStatsByCategory("transport");
        Integer household = foodLogService.findStatsByCategory("household");

        if (meal == null) {
            meal = 0;
        }
        if (transport == null) {
            transport = 0;
        }
        if (household == null) {
            household = 0;
        }

        ArrayList<Integer> sumPoints = new ArrayList<>();
        sumPoints.add(meal);
        sumPoints.add(transport);
        sumPoints.add(household);

        return sumPoints;
    }


    /**
     * Returns a Person object with a given id attribute.
     * @param id The id of the person.
     * @return Person object.
     * */
    @RequestMapping("/getPersonById")
    public Person getPersonById(@RequestParam(value = "id", defaultValue = "") int id) {
        Person person = personService.findById(id);
        person.setImage(getEncodedImagePerson(person));
        return person;

    }


    /**
     * Uses a person object to get the image related to the person in an encoded string form.
     * @param person The person that you want to image of
     * @return The profile picture of a person encoded in a string.
     */
    private String getEncodedImagePerson(Person person) {
        try {
            final File file = new File(System.getProperty("user.home")
                    + "\\GoGreenServerStorage\\profile\\"
                    + person.getEmail() + "\\" + person.getEmail() + ".jpg");
            byte[] fileContent = new byte[0];
            fileContent = FileUtils.readFileToByteArray(file);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            return encodedString;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Transform an actionlog image into an encoded string.
     * @param actionlog The action that you want the encoded image of
     * @return The image encoded in a string
     */
    public static String getEncodedImageActionLog(ActionLog actionlog) {
        try {
            if (actionlog.getFilePath() != null) {
                final File file = new File(actionlog.getFilePath());
                byte[] fileContent = new byte[0];
                fileContent = FileUtils.readFileToByteArray(file);
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                return encodedString;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Sends an email to a given user with the provided code.
     * @param code The code that needs to be sent to the user
     * @param person The user that the link with the provided secret code needs to be send to.
     */
    private void sendEmail(String code, Person person) {
        String username = "gogreennoreply@gmail.com";
        String password = "Gogreen123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Session mailConnection = Session.getInstance(props, null);
        Message msg = new MimeMessage(mailConnection);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gogreennoreply@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(person.getEmail()));
            message.setSubject("Reset Password");
            message.setText("Dear User,"
                    + "\n https://localhost:8080/changePassword?secreturl=" + code);
            Transport.send(message);
        } catch (javax.mail.MessagingException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Resets a users password to the provided one.
     * @param password The new password that needs to be used.
     * @param secret The secret from the user (Generated upon requesting a password change)
     * @return A string with a response, can be shown to the user.
     */
    @RequestMapping("/resetpassword")
    public String resetPassword(@RequestParam("password") String password,
                                @RequestParam("secretkey") String secret) {
        ResetPassword resetPassword = passwordReset.get(secret);

        if (resetPassword != null
                && resetPassword.getValidUntil().getTime() > new Date().getTime()) {
            resetPassword.getPerson().setPassword(hashPassword(password));
            personService.save(resetPassword.getPerson());

            return "Password changed.";
        }
        return "Token expired, please request a new link.";
    }

    /**
     * Displays the changepassword page.
     * TODO: Create an html file instead of this function
     */
    @RequestMapping("/changePassword")
    public String changePassword(@RequestParam("secreturl") String secreturl) {
        return "<html>\n"
                +
                "<head>\n"
                +
                "</head>\n"
                +
                "<body>\n"
                +
                "<h3>Welcome, Enter your new password</h3>\n"
                +
                "<form action=\"/resetpassword\" method=\"POST\">\n"
                +
                "\n"
                +
                "Password:<br>\n"
                +
                "<input type=\"text\" name=\"password\">\n"
                +
                "<input type=\"hidden\"  name=\"secretkey\" value=\"" + secreturl + "\">\n"
                +
                "\n"
                +
                "</form>\n"
                +
                "\n"
                +
                "</body>\n"
                +
                "</html>";
    }

    /**
     * Request a password change for the user that signed up with the provided email.
     * @param email The email of the user that wants his password to be changes
     * @return True if action was completed (even if the email is not in the DB)
     */
    @RequestMapping(method = RequestMethod.GET, value = "/requestChange")
    public Boolean requestPasswordChange(@RequestParam(value = "email") String email) {

        Person person = personService.findByEmail(email);
        if (person == null) {
            return true;
        }

        String generatedString = RandomStringUtils.randomAlphanumeric(20);
        long time = new Date().getTime();
        Date expirationTime = new Date(time + 60000);

        ResetPassword resetPassword = new ResetPassword(generatedString, expirationTime, person);
        passwordReset.put(generatedString, resetPassword);
        sendEmail(generatedString, person);
        return true;
    }

    /**
     * Returns all Person objects from the database.
     * @param authKey authKey of the user.
     * @return ArrayList of Person objects.
     */
    @RequestMapping("/getPersonList")
    public ArrayList<Person> getPersonList(@RequestParam(value = "authKey", defaultValue = "")
                                                       String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            ArrayList<Person> personList = personService.findAll();


            for (int i = 0; i < personList.size(); i++) {
                Person person = personList.get(i);
                person.setImage(getEncodedImagePerson(person));
            }

            return personList;
        } catch (SignatureException e) {
            return null;
        }
    }

    /**
     * Returns the given password hashed with bcrypt.
     * @param password The password that needs to be hashed
     * @return the hashed password + salt (bcrypt)
     * @throws IllegalArgumentException when password is null
     */
    public static String hashPassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("The password can't be null");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     *  Checks if a given password matches the given hash.
     * @param password The unhashed password that needs to be matches
     * @param hash The hash that needs to be matched with the password
     * @return A boolean, true when hash & password match otherwise false
     * @throws IllegalArgumentException when password and/or hash are null
     */
    public static boolean matchHash(String password, String hash) throws IllegalArgumentException {
        if (password == null || hash == null || password.isEmpty() || hash.isEmpty()) {
            throw new IllegalArgumentException("The password and/or hash can't be null");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, hash);
    }

    /**
     * Generates a jwts token with a given subject and exp date.
     * @param subject The subject
     * @param exp The date at which the token becomes invalid
     * @return The generated jwts token
     */
    public static String generateToken(String subject, Date exp) {
        /*    Claims claim = Jwts.claims();
        claim.put("GivenName", "Johnny");
        claim.put("Surname", "Rocket");
        claim.put("Email", "jrocket@example.com"); */

        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(subject).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, key).compact();
    }

    /**
     * This method ensures that HTTP requests to /login are mapped to the login() method.
     * @param email a String that is compared with the email in the personService class
     * @param password a String that is compared with the password in the personService class
     * @return null if name doesn't exist in the personService class,
     *         return persons object if both param are true or return null if password is false.
     */
    @RequestMapping( method = RequestMethod.GET, value = "/login")
    public AuthKey login(@RequestParam(value = "email", defaultValue = "") String email,
                        @RequestParam(value = "password", defaultValue = "") String password) {

        if (personService.findByEmail(email) == null) {
            return null;
        }

        if (matchHash(password, personService.findByEmail(email).getPassword())) {

            long time = new Date().getTime();
            Date expirationTime = new Date(time + 600000);

            String token = generateToken(email, expirationTime);

            return new AuthKey(token, email);
        } else {
            return null;
        }
    }

    /**
     * Method for getting all achievement objects from the database.
     * @return Return an arraylist of all Achievement objects in the database.
     */
    @RequestMapping("/getAchievements")
    public ArrayList<Achievement> getAchievement() {
        ArrayList<Achievement> achievements = achievementService.findAll();
        return achievements;
    }

    /**
     * Method for getting all achievement objects from
     * the database that correspond with the authkey.
     * @param authKey The authorization key used for accessing data.
     * @return Return an arraylist of achievementLogs.
     */
    @RequestMapping("/getAchievementLog")
    public ArrayList<Achievement> getAchievementLog(@RequestParam
                 (value = "authKey", defaultValue = "") String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            Person user = personService.findByEmail(claims.getSubject());
            ArrayList<Achievement> achievements = achievementService.findByPerson_id(user.getId());
            return achievements;
        } catch (SignatureException e) {
            return null;
        }
    }


    /**
     * Method for getting all action objects from the database that correspond with the authkey.
     * @param authKey The authorization key used for accessing data.
     * @return Return an arraylist of actionLog object that correspong with user.
     */
    @RequestMapping("/getActionLog")
    public ArrayList<ActionLog> getActionLog(@RequestParam(value = "authKey", defaultValue = "")
           String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            Person user = personService.findByEmail(claims.getSubject());
            ArrayList<ActionLog> actionLogs = foodLogService.findByPerson_id(user.getId());

            for (int i = 0; i < actionLogs.size(); i++) {
                ActionLog actionLog = actionLogs.get(i);
                actionLog.setImage(getEncodedImageActionLog(actionLog));
            }
            return actionLogs;
        } catch (SignatureException e) {
            return null;
        }
    }

    /**
     * Method for getting foodlog object from the database that correspond with the authkey.
     * @return Return an arraylist of foodlog object that correspong with user.
     */
    @RequestMapping("/getAction")
    public ArrayList<Action> getActionLog() {
        ArrayList<Action>  actions = actionService.findAll();
        return actions;
    }

    /**
     * Method return the actions of all users followers.
     *
     * @param authKey The authorization key used for accessing data.
     * @return Array list of type ActionLog with all of the followers actions.
     */
    @RequestMapping("/getActionLogOfFollowers")
    public ArrayList<ActionLog> getActionLogOfFollowers(
            @RequestParam(value = "authKey", defaultValue = "") String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            Person user = personService.findByEmail(claims.getSubject());
            ArrayList<ActionLog> followersActions = foodLogService
                    .findActionLogOfFollowers(user.getId());

            for (int i = 0; i < followersActions.size(); i++) {
                ActionLog actionLog = followersActions.get(i);
                actionLog.setImage(getEncodedImageActionLog(actionLog));
            }
            for (ActionLog actionLog: followersActions) {
                System.out.println("Image: " + actionLog.getRealImage());
            }
            return  followersActions;
        } catch (SignatureException e) {
            System.out.println("Could not return the actions of people you follow.");
            return null;
        }
    }

    /**
     * Checks if someone already upvoted an action.
     * @param actionId The actionid
     * @return The amount of upvotes the action has.
     */
    @RequestMapping("/getUpvoted")
    public Boolean getUpvoted(
            @RequestParam(value = "actionId", defaultValue = "") Integer actionId,
            @RequestParam(value = "authKey", defaultValue = "") String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            Person user = personService.findByEmail(claims.getSubject());

            if (voteService.countbyActionIdAndPerson_Id(actionId, user.getId()) > 0) {
                return false;
            } else {
                return  true;
            }
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Checks if any of the new achievements are achieved by the user.
     * @param authKey The authkey of the user.
     * @return Boolean true if a new achievement was achieved, false if not.
     */
    @RequestMapping("/getNewAchievements")
    public Boolean verifyAchievements(
            @RequestParam(value = "authKey", defaultValue = "") String authKey) {
        Person user;
        try {
            Claims claims = decodeJwt(authKey);
            user = personService.findByEmail(claims.getSubject());
        } catch (SignatureException e) {
            return false;
        }

        return achievementCheck(user);
    }

    /**
     * Method checks which achievements the user has completed.
     * @param user User.
     * @return boolean.
     */
    public boolean achievementCheck(Person user) {
        ArrayList<Achievement> achievements = achievementService.findNotByPerson_id(user.getId());
        Date dateTime = new Date();

        Boolean newAchievement = false;

        int followerCount = followService.countByPersonId_Followers(user.getId());
        int followingCount = followService.countByPersonId_Followed(user.getId());

        for (int i = 0; i < achievements.size(); i++) {
            if (user.getPoints() > achievements.get(i).getRequiredPoints()
                    && achievements.get(i).getType().equals("points")) {
                storeAchievements(user.getId(), achievements.get(i).getId(),
                        user.getFirstName(), achievements.get(i).getName(), dateTime);
                newAchievement = true;
            }

            if (followerCount > 2 && achievements.get(i).getType().equals("followers")) {
                storeAchievements(user.getId(), achievements.get(i).getId(),
                        user.getFirstName(), achievements.get(i).getName(), dateTime);
                newAchievement = true;
            }

            if (followerCount > 0 && followingCount > 0
                    && achievements.get(i).getType().equals("friends")) {
                storeAchievements(user.getId(), achievements.get(i).getId(),
                        user.getFirstName(), achievements.get(i).getName(), dateTime);
                newAchievement = true;
            }

            if (achievements.get(i).getType().equals("login")) {
                storeAchievements(user.getId(), achievements.get(i).getId(),
                        user.getFirstName(), achievements.get(i).getName(), dateTime);
                newAchievement = true;
            }

            if (getEncodedImagePerson(user) != null
                    && achievements.get(i).getType().equals("picture")) {
                storeAchievements(user.getId(), achievements.get(i).getId(),
                        user.getFirstName(), achievements.get(i).getName(), dateTime);
                newAchievement = true;
            }

            if (foodLogService.countByPerson_idAndType(user.getId(), "bike") > 2
                    && achievements.get(i).getType().equals("bike")) {
                storeAchievements(user.getId(), achievements.get(i).getId(),
                        user.getFirstName(), achievements.get(i).getName(), dateTime);
                newAchievement = true;
            }
        }
        return newAchievement;
    }

    /**
     * Insert achievement actionlog into the database.
     * @param userId The userid that got the achievement.
     * @param achievementId The achievement id.
     * @param userFirstname The firstname of the user.
     * @param achievementName The achievement name.
     * @param date The date on which the achievement was received.
     */
    void storeAchievements(int userId, int achievementId,
                           String userFirstname, String achievementName, Date date) {
        AchievementLog achievementLog = new AchievementLog(0, userId, achievementId);
        achievementLogService.save(achievementLog);
        ActionLog achievement = new ActionLog(0, "Achievement",
                userFirstname + " received a new achievement: " + achievementName,
                0, date, userId, userFirstname);
        foodLogService.save(achievement);
    }

    /**
     * Get the amount of upvotes a action has.
     * @param actionId The actionid
     * @return The amount of upvotes the action has.
     */
    @RequestMapping("/getActionUpvotes")
    public Integer getActionUpvotes(
            @RequestParam(value = "actionId", defaultValue = "") Integer actionId) {
        return voteService.countbyActionId(actionId);
    }

    /**
     * Add a new upvote to an action.
     * @param type Json object containing authkey and actionid
     * @return False when authkey invalid or already voted, true if went ok.
     */
    @PostMapping("/postActionUpvotes")
    public Boolean postUpvotes(@RequestBody String type) {

        JSONObject jsontype = new JSONObject(type);
        String authKey = jsontype.getString("Authkey");
        int actionId = jsontype.getInt("ActionId");

        try {
            Claims claims = decodeJwt(authKey);
            Person user = personService.findByEmail(claims.getSubject());
            Vote vote = new Vote(0, actionId,user.getId());
            voteService.save(vote);
            return  true;
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Updates the profile picture of a user.
     * @param type json object containing the authkey and iamge
     */
    @PostMapping("/updateImage")
    public Boolean updateImage(@RequestBody String type) {
        try {
            JSONObject jsontype = new JSONObject(type);
            String encodedImage = jsontype.getString("Image");
            Claims claims = decodeJwt(jsontype.getString("AuthKey"));

            Person user = personService.findByEmail(claims.getSubject());

            setImage(user, encodedImage);
            return true;

        } catch (SignatureException e) {
            return false;
        }
    }


    /**
     * Registers the given model object, adds it to the DB.
     * @param person The model object that needs to be saved to the DB
     */
    @PostMapping("/register")
    public Boolean addPerson(@RequestBody Person person) {
        if (personService.findByEmail(person.getEmail()) != null) {
            System.out.println("User with email already found");
            return false;
        }

        setImage(person, person.getImage());

        System.out.println(person.toString());

        person.setPassword(hashPassword(person.getPassword()));

        personService.save(person);
        return true;
    }

    /**
     * Sets the image for a person object.
     * @param person Person Object.
     * @param image Encoded image.
     */
    public static void setImage(Person person, String image) {
        new File(System.getProperty("user.home")
                + "\\GoGreenServerStorage\\profile\\" + person.getEmail()).mkdirs();

        final File file = new File(System.getProperty("user.home")
                + "\\GoGreenServerStorage\\profile\\"
                + person.getEmail() + "\\" + person.getEmail() + ".jpg");

        try {
            if (image != null) {
                file.createNewFile();
                byte[] decodedBytes = Base64.getDecoder().decode(image);
                FileUtils.writeByteArrayToFile(file, decodedBytes);
            }
        } catch (IOException e) {
            System.out.println("No Image provided");
        }
    }


    /**
     * Method that takes a foodlog json and save it in the database with the user.
     * @param type the type of foodlog.
     */
    @PostMapping("/actionLog")
    public Boolean addActionLog(@RequestBody String type) {
        try {
            int points = 0;
            ArrayList<String>  mealCategories = new ArrayList(Arrays.asList("vegetarian",
                    "vegan", "local"));
            ArrayList<String> transportCategories = new ArrayList(Arrays.asList("bike",
                    "bus", "train")); // NOT COMPLETE
            ArrayList<String> householdCategories =
                    new ArrayList(Arrays.asList("solar", "heating")); // NOT COMPLETE

            JSONObject jsontype = new JSONObject(type);
            String actionDescription = jsontype.getString("Description");
            Claims claims = decodeJwt(jsontype.getString("AuthKey"));

            String actionType = jsontype.getString("Type");
            if (mealCategories.contains(actionType)) {
                points = actionService.findWithType(jsontype.getString("Type")).getPoints();
                actionDescription = "I ate " + actionType
                        + " meal: " + actionDescription.replaceAll("Meal: ", "");
            } else if (transportCategories.contains(actionType)) {
                String doubleString = jsontype.getString("Description");
                System.out.println("Description: " + doubleString);
                String vehicle = doubleString.substring(0,doubleString.indexOf(":"));
                doubleString = doubleString.substring(doubleString.indexOf(":") + 1);
                doubleString = doubleString.substring(doubleString.indexOf(":") + 1);
                double distance = Double.valueOf(doubleString.replaceAll("km", ""));
                points = getPointsFromApi( 1, distance, vehicle);
                actionDescription = actionDescription
                        .replaceAll("Transport", "Transport with " + actionType);
                actionDescription = actionDescription.replaceAll("train", "train/tram");

            } else if (householdCategories.contains(actionType)) {
                points = actionService.findWithType(jsontype.getString("Type")).getPoints();
                String action = jsontype.getString("Type");
                if (action.equals("solar")) {
                    String description = jsontype.getString("Description").replaceAll(" ", "");
                    int solarPanels = Integer
                            .parseInt(description.substring(description.indexOf(":") + 1));
                    points *= solarPanels;
                    actionDescription = "Installed " + solarPanels + " solar panel";
                    if (solarPanels != Math.abs(1)) {
                        actionDescription += "s";
                    }
                } else {
                    String loweredDegrees = jsontype
                            .getString("Description").replaceAll(" ", "");
                    int loweredDegreesAmount = Integer
                            .parseInt(loweredDegrees.substring(loweredDegrees.indexOf(":") + 1));
                    points *= loweredDegreesAmount;
                    actionDescription = "Lowered the thermostat by "
                            + loweredDegreesAmount + " degree";
                    if (loweredDegreesAmount != Math.abs(1)) {
                        actionDescription += "s";
                    }
                }
            } else {
                System.out.println("ActionType is not in any of the categories");
            }
            Person user = personService.findByEmail(claims.getSubject());
            user.setPoints(user.getPoints() + points);
            personService.save(user);

            Date dateTime = new Date();

            // get rid of whitespaces after last word
            ActionLog actionLog = new ActionLog(0, jsontype.getString("Type"),
                    actionDescription,
                     points, dateTime, user.getId(), user.getFirstName());

            int size = foodLogService.findByPerson_id(user.getId()).size();

            size = size + 1;

            if (!jsontype.isNull("Image")) {
                actionLog.setImage(jsontype.getString("Image"));
            } else {
                actionLog.setImage(null);
            }


            new File(System.getProperty("user.home")
                    + "\\GoGreenServerStorage\\actions\\" + user.getEmail()).mkdirs();

            File file = new File(System.getProperty("user.home")
                    + "\\GoGreenServerStorage\\actions\\"
                    + user.getEmail(), "image" + size + ".jpg");

            String filePath = file.getPath();

            try {
                file.createNewFile();
                byte[] decodedBytes = Base64.getDecoder().decode(actionLog.getImage());
                FileUtils.writeByteArrayToFile(file, decodedBytes);
                actionLog.setFilePath(filePath);

            } catch (IOException | NullPointerException e) {
                System.out.println("No Image provided");
            }
            foodLogService.save(actionLog);
            return true;
        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The postmapping method to follow user by email.
     * @param email The email of the user.
     * @return true if follow user exist and there is no follow class,
               return false if follow user doesn't exist
               return false if you are already followed.
     */
    @PostMapping("/followEmail")
    public boolean followByEmail(@RequestBody String email) {
        try {
            JSONObject jsonemail = new JSONObject(email);

            Claims claims = decodeJwt(jsonemail.getString("AuthKey"));

            Person user = personService.findByEmail(claims.getSubject());

            Person follower = personService.findByEmail(jsonemail.getString("Email"));

            if (follower == null) {
                return false;
            }

            Follower alreadyFollowed = followService.findByPerson_idAndFollower_id(user.getId(),
                    follower.getId());

            if (alreadyFollowed != null) {
                return false;
            } else {
                Follower follower1 = new Follower(0, user.getId(), follower.getId());
                followService.save(follower1);
                return true;
            }
        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Follow a person based on a personID.
     * @param personId A json object containing the id of the person to follow
     * @return return true if follow action went through.
       return false if row exist, return false if id don't exist.
     */
    @PostMapping("/follow")
    public boolean followById(@RequestBody String personId) {
        try {
            JSONObject jsonEmail = new JSONObject(personId);

            Claims claims = decodeJwt(jsonEmail.getString("AuthKey"));

            Person user = personService.findByEmail(claims.getSubject());

            int followerId = Integer.parseInt(jsonEmail.getString("FollowingID"));

            Person follower = personService.findById(followerId);

            if (follower == null) {
                return false;
            }

            Follower alreadyFollowed = followService.findByPerson_idAndFollower_id(user.getId(),
                    follower.getId());

            if (alreadyFollowed != null) {
                return false;
            } else {
                Follower follower1 = new Follower(0, user.getId(), follower.getId());
                followService.save(follower1);
                return true;
            }
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Changes hidden attribute of person.
     * Makes a user profile private or not.
     * @param hidden boolean, state of hidden.
     * @return whether user profile is private.
     */
    @PostMapping("/hidden")
    public boolean changeHidden(@RequestBody String hidden) {
        try {
            JSONObject jsonEmail = new JSONObject(hidden);

            Claims claims = decodeJwt(jsonEmail.getString("AuthKey"));

            Person user = personService.findByEmail(claims.getSubject());

            String boolString = jsonEmail.getString("Hidden");

            if (boolString.equalsIgnoreCase("true")) {
                boolean hiddenBool = Boolean.parseBoolean(boolString);

                if (user.getHidden().equals(hiddenBool)) {
                    return  false;
                }

                user.setHidden(hiddenBool);
                personService.save(user);
                return true;
            } else if (boolString.equalsIgnoreCase("false")) {
                boolean hiddenBool = Boolean.parseBoolean(boolString);

                if (user.getHidden().equals(hiddenBool)) {
                    return false;
                }

                user.setHidden(hiddenBool);
                personService.save(user);
                return true;
            } else {
                return false;
            }
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * post mapping method to unfollow a given person by personId.
     * @param personId The personId of the person to unfollow
     * @return Return true if the person was unfollowed successfully
               return false if the authkey is invalid
               return false if person to unfollow doesn't exist
     */
    @PostMapping("/unfollow")
    public boolean unfollowById(@RequestBody String personId) {
        try {
            JSONObject jsonEmail = new JSONObject(personId);

            Claims claims = decodeJwt(jsonEmail.getString("AuthKey"));

            Person user = personService.findByEmail(claims.getSubject());

            int unfollowerId = Integer.parseInt(jsonEmail.getString("FollowingID"));

            Person unfollower = personService.findById(unfollowerId);

            if (unfollower == null) {
                return false;
            }

            Follower alreadyFollowed = followService.findByPerson_idAndFollower_id(user.getId(),
                    unfollower.getId());

            if ( alreadyFollowed != null) {
                followService.delete(alreadyFollowed);
                return true;
            }

            return true;

        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Method that return an arrayList of person that the user is following.
     * @param authKey The authKey from the user
     * @return return an arrayList with all the person that the user has followed
     */
    @RequestMapping("/getFollowingList")
    public ArrayList<Person> getFollowingList(@RequestParam(value = "authKey", defaultValue = "")
                                                     String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            int userId = personService.findByEmail(claims.getSubject()).getId();


            ArrayList<Person> personList = personService.findAllFollowerByPerson_id(userId);
            for (int i = 0; i < personList.size(); i++) {
                Person person = personList.get(i);
                person.setImage(getEncodedImagePerson(person));
            }
            for (int i = 0; i < personList.size(); i ++) {
                personList.get(i).setPassword("NoPasswordToBeFound");
                personList.get(i).setEmail("ThisIsATrap");
            }

            return personList;

        } catch (SignatureException e) {
            return null;
        }
    }

    /**
     * Method that return an arrayList of person that user hasn't followed.
     * @param authKey The authKey from the user
     * @return return an arrayList with all the persons that the user hasn't followed
     *      return null if the provided Authkey was invalid.
     */
    @RequestMapping("/getSuggestedList")
    public ArrayList<Person> getSuggestedList(@RequestParam(value = "authKey", defaultValue = "")
                                                      String authKey) {
        try {
            Claims claims = decodeJwt(authKey);
            int userId = personService.findByEmail(claims.getSubject()).getId();

            ArrayList<Person> personList = personService.findAllNonFollowerByPerson_id(userId);
            for (int i = 0; i < personList.size(); i++) {
                Person person = personList.get(i);
                person.setImage(getEncodedImagePerson(person));
            }

            for (int i = 0; i < personList.size(); i++) {
                personList.get(i).setPassword(null);
                personList.get(i).setEmail(null);
            }

            return personList;

        } catch (SignatureException e) {
            return null;
        }
    }

    /**
     * Uses the brighterplanet api to get the amount of co2 you would produce
     * for a certain category with/without distance and information.
     * @param category The category you want to get the CO2 amount of
     * @param distance The total distance (with bike, car, train, bus)
     * @param information Any extra information
     * @return The total amount of co2 produced by this action.
     */
    public static int getPointsFromApi(int category, double distance, String information) {
        int result = 0;
        String url = "";
        // Category 1 = transport, 2 = households
        switch (category) {
            case 1:
                // Get the amount of co2 you would produce using the car
                String carUrl = "http://impact.brighterplanet.com/automobiles.json"
                        + "?daily_distance=" + distance + "&timeframe=2019-01-01/2019-01-02&"
                        + "key=1cd15260-18b9-449e-9a47-6190882bda7c";
                RestTemplate rstm = new RestTemplate();
                String carReply = rstm.getForObject(carUrl, String.class);
                JSONObject temp = new JSONObject(carReply);

                String carCarbonOutputString = temp.getJSONObject("decisions")
                        .getJSONObject("carbon").getString("description");

                int carCutAt = 0;
                while (carCarbonOutputString.charAt(carCutAt) != ' ') {
                    carCutAt++;
                }

                carCarbonOutputString = carCarbonOutputString.substring(0, carCutAt);
                double carCarbonOutput = Double.valueOf(carCarbonOutputString);

                if (information.contains("bike")) {
                    // DO NOTHING AS RIDING THE BIKE PRODUCES NO CO2
                } else if (information.contains("bus")) {
                    url = "http://impact.brighterplanet.com/bus_trips.json?distance=" + distance + ""
                            + "&timeframe=2019-01-01/2019-01-02&"
                            + "key=1cd15260-18b9-449e-9a47-6190882bda7c";

                } else if (information.contains("train")) {
                    url = "http://impact.brighterplanet.com/rail_trips.json?"
                            + "distance=" + distance
                            + "&timeframe="
                            + "2019-01-01/2019-01-02&key=1cd15260-18b9-449e-9a47-6190882bda7c";
                }

                double carbonOutput = 0;
                if (!url.equals("")) {
                    RestTemplate rstm2 = new RestTemplate();
                    String reply = rstm.getForObject(url, String.class);
                    JSONObject emissions = new JSONObject(reply);

                    String carbonOutputString = emissions.getJSONObject("decisions")
                            .getJSONObject("carbon").getString("description");

                    int cutAt = 0;
                    while (carbonOutputString.charAt(cutAt) != ' ') {
                        cutAt++;
                    }

                    carbonOutputString = carbonOutputString.substring(0, cutAt);
                    carbonOutput = Double.valueOf(carbonOutputString);
                }
                double carbonResult = carCarbonOutput - carbonOutput;
                result = (int) (carbonResult * 25);
                return result;
            default:
                break;
        }
        return result;
    }

    /**
     * Method that return an arrayList of person that the user is following using id.
     * @param id The if of the user you want to see list of.
     * @return return an arrayList with all the person that the user has followed
     */
    @RequestMapping("/getFollowingListById")
    public ArrayList<Person> getFollowingListById(@RequestParam(value = "id", defaultValue = "")
                                                          Integer id) {
        ArrayList<Person> personList = personService.findAllFollowerByPerson_id(id);
        for (int i = 0; i < personList.size(); i++) {
            Person person = personList.get(i);
            person.setImage(getEncodedImagePerson(person));
        }

        for (int i = 0; i < personList.size(); i ++) {
            personList.get(i).setPassword("NoPasswordToBeFound");
            personList.get(i).setEmail("ThisIsATrap");
        }

        return personList;
    }

    /**
     * Method gets the follower count of the given id.
     * @param id The id of the person to get followers from.
     * @return Integer.
     */
    @RequestMapping("/getWhoFollowsId")
    public int getWhoFollowsId(@RequestParam(value = "id", defaultValue = "")
                                       Integer id) {
        int numberOfFollowers = personService.findWhoFollowsId(id);
        return numberOfFollowers;
    }
}
