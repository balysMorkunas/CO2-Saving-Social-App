package gogreenserver.service;

import gogreenserver.repository.ActionRepository;
import gogreenserver.repository.FoodLogRepository;
import model.Action;
import model.ActionLog;
import model.Person;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActionRepositoryTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;

    @Autowired
    private ActionRepository repo;

    Action action = new Action(1, "vegetarian", "Cool description", 50, "meal");
    Action action2 = new Action(8, "gas", "cool description",  50, "meal");

    @org.junit.jupiter.api.Test
    void saveAction() {
        repo.save(action);
        assertEquals(50, repo.findAll().get(0).getPoints());
    }

    @org.junit.jupiter.api.Test
    void getAction() {
        repo.save(action2);
        assertEquals(50, repo.findWithType("gas").getPoints());
    }

}
