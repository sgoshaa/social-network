package ru.skillbox.diplom.mappers;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.FriendShipDTO;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.RegisterRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class PersonMapperImpl implements PersonMapper {

    private final DateConverter dateConverter = new DateConverter();

    @Override
    public Person toPersonEntity(RegisterRequest registerRequest, String encryptedPassword, LocalDateTime localDateTime, String confirmationCode) {
        if ( registerRequest == null && encryptedPassword == null && localDateTime == null && confirmationCode == null ) {
            return null;
        }

        Person person = new Person();

        if ( registerRequest != null ) {
            person.setFirstName( registerRequest.getFirstName() );
            person.setEmail( registerRequest.getEmail() );
            person.setLastName( registerRequest.getLastName() );
        }
        if ( encryptedPassword != null ) {
            person.setPassword( encryptedPassword );
        }
        if ( localDateTime != null ) {
            person.setRegDate( localDateTime );
            person.setLastOnlineTime( localDateTime );
        }
        if ( confirmationCode != null ) {
            person.setConfirmationCode( confirmationCode );
        }
        person.setIsApproved( false );
        person.setMessagesPermission( ru.skillbox.diplom.model.enums.MessagesPermission.ALL );
        person.setIsBlocked( false );
        person.setIsDeleted( false );

        return person;
    }

    @Override
    public FriendShipDTO personToFriendShipDTO(Person person) {
        if ( person == null ) {
            return null;
        }

        FriendShipDTO friendShipDTO = new FriendShipDTO();

        friendShipDTO.setRegDate( dateConverter.convertRegDate( person.getRegDate() ) );
        friendShipDTO.setBirthDate( dateConverter.convertRegDate( person.getBirthDate() ) );
        friendShipDTO.setLastOnlineTime( dateConverter.convertRegDate( person.getLastOnlineTime() ) );
        if ( person.getId() != null ) {
            friendShipDTO.setId( person.getId() );
        }
        friendShipDTO.setFirstName( person.getFirstName() );
        friendShipDTO.setLastName( person.getLastName() );
        friendShipDTO.setEmail( person.getEmail() );
        friendShipDTO.setPhone( person.getPhone() );
        friendShipDTO.setPhoto( person.getPhoto() );
        friendShipDTO.setAbout( person.getAbout() );
        if ( person.getMessagesPermission() != null ) {
            friendShipDTO.setMessagesPermission( person.getMessagesPermission().name() );
        }

        return friendShipDTO;
    }

    @Override
    public PersonDTO toPersonDTO(Person user) {
        if ( user == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        personDTO.setId( user.getId() );
        personDTO.setFirstName( user.getFirstName() );
        personDTO.setLastName( user.getLastName() );
        personDTO.setRegDate( user.getRegDate() );
        personDTO.setBirthDate( user.getBirthDate() );
        personDTO.setEmail( user.getEmail() );
        personDTO.setPhone( user.getPhone() );
        personDTO.setPhoto( user.getPhoto() );
        personDTO.setAbout( user.getAbout() );
        if ( user.getMessagesPermission() != null ) {
            personDTO.setMessagesPermission( user.getMessagesPermission().name() );
        }
        personDTO.setLastOnlineTime( user.getLastOnlineTime() );

        return personDTO;
    }

    @Override
    public PersonDTO toPersonDTO(User user) {
        if ( user == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        personDTO.setId( user.getId() );
        personDTO.setFirstName( user.getFirstName() );
        personDTO.setEmail( user.getEmail() );

        return personDTO;
    }

    @Override
    public PersonDTO toPersonDTO(Person person, String token) {
        if ( person == null && token == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        if ( person != null ) {
            personDTO.setId( person.getId() );
            personDTO.setRegDate( person.getRegDate() );
            personDTO.setLastOnlineTime( person.getLastOnlineTime() );
            if ( person.getIsBlocked() != null ) {
                personDTO.setBlocked( person.getIsBlocked() );
            }
            if ( person.getMessagesPermission() != null ) {
                personDTO.setMessagesPermission( person.getMessagesPermission().name() );
            }
            personDTO.setFirstName( person.getFirstName() );
            personDTO.setLastName( person.getLastName() );
            personDTO.setEmail( person.getEmail() );
            personDTO.setPhone( person.getPhone() );
            personDTO.setPhoto( person.getPhoto() );
            personDTO.setAbout( person.getAbout() );
        }
        if ( token != null ) {
            personDTO.setToken( token );
        }

        return personDTO;
    }
}
