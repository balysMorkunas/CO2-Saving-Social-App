package gogreenserver.service;

import gogreenserver.repository.FoodLogRepository;
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
public class FoodLogRepositoryTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;

    @Autowired
    private FoodLogRepository repo;

    Person test1 = new Person(5, "goerge@test.com","Gorge", "La", "12345", 200);

    ActionLog actionLog = new ActionLog(1, "vegetarian", "Cool description", 50, null, 5, "Bob");

    @org.junit.jupiter.api.Test
    void saveActionLog() {
        repo.save(actionLog);
        assertEquals(50, repo.findByPerson_id(5).get(0).getPoints());
    }

    @org.junit.jupiter.api.Test
    void getActionLogEmpty() {
        repo.save(actionLog);
        assertEquals(0, repo.findByPerson_id(6).size());
    }

    @org.junit.jupiter.api.Test
    void findByPerson_idAndType() {

        repo.save(actionLog);
        assertEquals(50, repo.findByPerson_idAndType(5, "vegetarian").get(0).getPoints());
    }

    @org.junit.jupiter.api.Test
    void findByPerson_idAndTypeEmpty() {
        repo.save(actionLog);
        assertEquals(0, repo.findByPerson_idAndType(5, "veg").size());
    }
}
