package gogreenserver.repository;

import model.Achievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface AchievementRepository extends CrudRepository<Achievement, Integer> {

    @Query(value = "SELECT * FROM achievement", nativeQuery = true)
    ArrayList<Achievement> findAll();

    @Query(value = "SELECT achievement.id, achievement.name,"
            + " achievement.required_points, achievement.type FROM "
            + "achievement JOIN achievement_log ON "
            + "achievement.id=achievement_log.achievement_id WHERE"
            + " achievement_log.person_id= :pid ORDER BY achievement_log.id",
            nativeQuery = true)
    ArrayList<Achievement> findByPerson_id(@Param("pid") Integer personId);

    @Query(value = "SELECT * FROM achievement WHERE achievement.id NOT IN "
                        + "(SELECT achievement_id FROM achievement_log WHERE person_id = :pid)",
            nativeQuery = true)
    ArrayList<Achievement> findNotByPerson_id(@Param("pid") Integer personId);
}
