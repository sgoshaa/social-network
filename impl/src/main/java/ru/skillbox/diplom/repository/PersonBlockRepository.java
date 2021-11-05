package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonBlock;

@Repository
public interface PersonBlockRepository extends JpaRepository<PersonBlock, Integer> {

    boolean existsByPersonId_IdAndBlocked_Id(int person, int blocking);
    PersonBlock getByPersonId_IdAndBlocked_Id(int person, int blocking);

    @Modifying
    @Query("DELETE FROM PersonBlock p where p.blocked = :person or p.personId = :person ")
    void deletePersonBlockBy(@Param("person") Person person);
}
