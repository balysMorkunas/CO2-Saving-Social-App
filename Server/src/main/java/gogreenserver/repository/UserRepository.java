package gogreenserver.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;


public interface UserRepository extends CrudRepository<Person, Integer> {
    Person findByEmail(String email);

    Person findById(int id);

    @Query( value = "SELECT * FROM person p WHERE p.id IN ( "
            + "SELECT follower_id FROM follower f WHERE f.person_id = :pid)", nativeQuery = true )
    ArrayList<Person> findAllFollowerByPerson_id(@Param("pid") Integer personId);

    @Query( value = "SELECT * FROM person p WHERE p.id NOT IN( "
            + "SELECT follower_id FROM follower f WHERE f.person_id = :pid) "
            + "AND NOT p.id = :pid AND p.hidden = 0 ", nativeQuery = true )
    ArrayList<Person> findAllNonFollowerByPerson_id(@Param("pid") Integer personId);

    @Query( value = "SELECT * FROM person", nativeQuery = true )
    ArrayList<Person> findAll();

    @Query( value = "SELECT COUNT(*) FROM follower WHERE follower_id = :pid", nativeQuery = true )
    int findWhoFollowsId(@Param("pid") Integer personId);

}
