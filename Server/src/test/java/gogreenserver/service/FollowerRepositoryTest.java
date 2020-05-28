
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


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FollowerRepositoryTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;

    @Autowired
    private FollowerRepository repo;

    @Autowired
    private UserRepository repo1;

    Follower follower = new Follower(9,5,10);

    Person person1 = new Person();

    Person person2 = new Person();

    Follower follower1 = new Follower(8, 0, 1);

    @org.junit.jupiter.api.Test
    void findByPerson_idAndFollower_id() {
        repo.save(follower);

        assertEquals(5, repo.findByPerson_idAndFollower_id(5, 10).getPersonId());
    }

    @org.junit.jupiter.api.Test
    void findByPerson_idAndFollower_idEmpty() {
        repo.save(follower);

        assertEquals(null, repo.findByPerson_idAndFollower_id(5, 11));
    }

}