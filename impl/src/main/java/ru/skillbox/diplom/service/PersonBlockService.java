package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonBlock;
import ru.skillbox.diplom.model.enums.ActionType;
import ru.skillbox.diplom.repository.PersonBlockRepository;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;


@Service
public class PersonBlockService implements SocialNetworkService {

    private final PersonBlockRepository personBlockRepository;

    @Autowired
    public PersonBlockService(PersonBlockRepository personBlockRepository) {
        this.personBlockRepository = personBlockRepository;
    }

    public void personBlocking(Person person, Person blocking) {
        log(PersonBlockService.class, LoggerLevel.INFO, "personBlocking", LoggerValue.BLOCKING, person.getEmail() + " " + blocking.getEmail());
        PersonBlock newBlock;
        if(personBlockRepository.existsByPersonId_IdAndBlocked_Id(person.getId(), blocking.getId())){
            newBlock = personBlockRepository.getByPersonId_IdAndBlocked_Id(person.getId(), blocking.getId());
            newBlock.setAction(newBlock.getAction() == ActionType.BLOCK ? ActionType.UNBLOCK : ActionType.BLOCK);
        }
        else if(!person.getId().equals(blocking.getId())){
            newBlock = new PersonBlock(person, blocking, ActionType.BLOCK);
            personBlockRepository.save(newBlock);
        } else return;
        personBlockRepository.save(newBlock);
    }
}
