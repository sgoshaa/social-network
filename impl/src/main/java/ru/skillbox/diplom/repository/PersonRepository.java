package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.Person;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {

    Optional<Person> findByEmail(String email);

    Optional<Person> findByConfirmationCode(String confirmationCode);

    @Modifying
    @Query("delete from Person p where p = :person")
    @Transactional
    void deletePerson(Person person);

}
