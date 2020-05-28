package gogreenserver.service;

import gogreenserver.repository.FollowerRepository;
import gogreenserver.repository.UserRepository;
import model.Follower;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;

    @Autowired
    private UserRepository repo;

    @Autowired
    private FollowerRepository repo1;

    Person test1 = new Person(3,"goerge@test.com","Gorge", "La", "12345", 200);

    Person test2 = new Person(4, "blue@test.com","Blue", "Red", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 250);

    Person test3 = new Person(7, "blue123@test.com","Blue", "Red", "$2a$10$XbwoFtp0b0IcuMeVVBC6VeKQu.U9V/vJIsOOsU/Vd6sZ7kpXVmEom", 540);


    @org.junit.jupiter.api.Test
    void saveNewUser() {
        repo.save(test1);
        assertEquals(repo.findByEmail("goerge@test.com").getFirstName(), test1.getFirstName());
    }

    @org.junit.jupiter.api.Test
    void updateOldUser() {
        repo.save(test1);

        test1.setPoints(5);

        repo.save(test1);
        assertEquals(5, repo.findByEmail("goerge@test.com").getPoints());
    }

    @org.junit.jupiter.api.Test
    void findByID() {
        repo.save(test1);
        assertEquals(200, repo.findById(3).getPoints());
    }

    @org.junit.jupiter.api.Test
    void findByUsername() {
        repo.save(test1);
        assertEquals(200, repo.findByEmail("goerge@test.com").getPoints());
    }

    @org.junit.jupiter.api.Test
    void findByUsernameNull() {
        repo.save(test1);
        assertNull(repo.findByEmail("goerge@test.comeeee"));
    }

    @org.junit.jupiter.api.Test
    void findByIDNull() {
        repo.save(test1);
        assertNull(repo.findById(40));
    }

    @org.junit.jupiter.api.Test
    void getFollowerEmpty() {
        repo.save(test1);
        repo.save(test2);

        assertEquals(0, repo.findAllFollowerByPerson_id(2).size());
    }

    @org.junit.jupiter.api.Test
    void getFollower() {
        repo.save(test1);

        assertEquals(200, repo.findAllFollowerByPerson_id(1).get(1).getPoints());
        assertEquals(500, repo.findAllFollowerByPerson_id(1).get(0).getPoints());
    }

    @org.junit.jupiter.api.Test
    void getSuggested() {
        test1.setHidden(false);
        test2.setHidden(false);

        repo.save(test1);
        repo.save(test2);






        assertEquals(250, repo.findAllNonFollowerByPerson_id(1).get(0).getPoints());

    }

    /*
    @org.junit.jupiter.api.Test
    void getSuggestedEmpty() {
        repo.save(test1);

        assertEquals(null, repo.findAllNonFollowerByPerson_id(1).get(0));
    }
    */

}
