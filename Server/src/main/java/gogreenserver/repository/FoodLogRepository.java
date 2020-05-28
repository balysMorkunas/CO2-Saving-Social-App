package gogreenserver.repository;

import model.ActionLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;


public interface FoodLogRepository extends CrudRepository<ActionLog, Integer> {

    @Query(value = "SELECT * FROM action_log t WHERE t.person_id=:pid", nativeQuery = true)
    ArrayList<ActionLog> findByPerson_id(@Param("pid") Integer personId);


    @Query(value = "SELECT * FROM action_log t WHERE t.person_id=:pid AND t.type = :ptype",
           nativeQuery = true)
    ArrayList<ActionLog> findByPerson_idAndType(@Param("pid") Integer personId,
                                              @Param("ptype") String type);

    @Query( value = "SELECT * FROM action_log WHERE action_log.person_id IN "
            + "(SELECT follower_id FROM follower WHERE person_id = :pid)", nativeQuery = true)
    ArrayList<ActionLog> findActionLogOfFollowers(@Param("pid") Integer personId);

    @Query(value = "SELECT SUM(points) FROM action_log WHERE"
            + " type IN (SELECT type FROM action WHERE category = :pid)", nativeQuery = true)
    Integer findStatsByCategory(@Param("pid") String category);


    @Query(value = "SELECT COUNT(id) FROM action_log t WHERE t.person_id=:pid AND t.type = :ptype",
            nativeQuery = true)
    Integer countByPerson_idAndType(@Param("pid") Integer personId,
                                                @Param("ptype") String type);
}
