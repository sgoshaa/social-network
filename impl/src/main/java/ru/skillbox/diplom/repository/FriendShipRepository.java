package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.diplom.model.FriendShip;
import ru.skillbox.diplom.model.Person;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Integer>, JpaSpecificationExecutor<FriendShip> {

    @Query("SELECT p FROM FriendShip f INNER JOIN FriendShipStatus fs ON f.status = fs.id " +
            "INNER JOIN Person p ON f.dstPersonId = p.id " +
            "WHERE  " +
            "f.srcPersonId=:idCurrentUser " +
            "AND fs.code ='FRIEND'")
    List<Person> findByCurrentId(Integer idCurrentUser);

    List<FriendShip> findBySrcPersonId(Integer id);

    List<FriendShip> findByDstPersonId(Integer dstId);

    List<FriendShip> findAllBySrcPersonIdIn(Iterable<Integer> srcPersonId);

    List<FriendShip> findAllByDstPersonIdIn(Iterable<Integer> dstPersonId);

    Optional<FriendShip> findBySrcPersonIdAndDstPersonId(Integer srcPersonId,Integer dstPersonId);

    void deleteBySrcPersonIdAndDstPersonId(Integer srcPersonId,Integer dstPersonId);

    @Query("SELECT p FROM Person p WHERE p.id IN " +
            "(SELECT f2.dstPersonId FROM FriendShip f2 INNER JOIN FriendShipStatus fs2 on f2.status = fs2.id " +
            "   WHERE fs2.code='FRIEND' " +
            "   AND f2.srcPersonId in (SELECT f.dstPersonId FROM FriendShip f INNER JOIN FriendShipStatus fs on f.status = fs.id " +
            "                       WHERE f.srcPersonId =:idCurrentUser AND fs.code ='FRIEND')" +
            "   AND f2.dstPersonId NOT IN (SELECT f3.dstPersonId FROM FriendShip f3 INNER JOIN FriendShipStatus fs3 on f3.status = fs3.id" +
            "                           WHERE f3.srcPersonId = :idCurrentUser AND fs3.code ='FRIEND') " +
            "   AND f2.dstPersonId != :idCurrentUser " +
            "   GROUP BY f2.dstPersonId)")
    List<Person> getListPossibleFriends(Integer idCurrentUser);
}
