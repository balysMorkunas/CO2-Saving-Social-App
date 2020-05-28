package gogreenserver.repository;

import model.Follower;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends CrudRepository<Follower, Integer> {

    @Query(value = "SELECT * FROM follower f WHERE f.person_id=:pid AND f.follower_id = :fid",
            nativeQuery = true)
    Follower findByPerson_idAndFollower_id(@Param("pid") Integer personId,
                                           @Param("fid") Integer followerId);

    @Query(value = "SELECT COUNT(id) FROM follower f WHERE f.follower_id=:pid",
            nativeQuery = true)
    Integer countByPersonId_Followers(@Param("pid") Integer personId);

    @Query(value = "SELECT COUNT(id) FROM follower f WHERE f.person_id=:pid",
            nativeQuery = true)
    Integer countByPersonId_Followed(@Param("pid") Integer personId);
}
