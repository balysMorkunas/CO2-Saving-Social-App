package gogreenserver.repository;

import model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface VoteRepository extends CrudRepository<Vote, Integer> {

    @Query(value = "SELECT COUNT(id) FROM vote WHERE vote.action_log_id = :aid",
            nativeQuery = true)
    Integer countbyActionId(@Param("aid") Integer actionId);

    @Query(value = "SELECT COUNT(id) FROM vote WHERE "
        + "vote.action_log_id = :aid AND vote.person_id = :pid",
            nativeQuery = true)
    Integer countbyActionIdAndPerson_Id(@Param("aid")
                                            Integer actionId, @Param("pid") Integer personId);
}
