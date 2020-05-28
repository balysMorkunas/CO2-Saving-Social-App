package gogreenserver.repository;

import model.Action;
//import model.ActionLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;


public interface ActionRepository extends CrudRepository<Action, Integer> {

    @Query(value = "SELECT * FROM action", nativeQuery = true)
    ArrayList<Action> findAll();

    @Query(value = "SELECT * FROM action WHERE type=:type LIMIT 1", nativeQuery = true)
    Action findWithType(@Param("type") String type);

}
