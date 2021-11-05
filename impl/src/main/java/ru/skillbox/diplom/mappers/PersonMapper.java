package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.FriendShipDTO;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.RegisterRequest;

import java.time.LocalDateTime;

@Mapper(uses = DateConverter.class)
public interface PersonMapper {

    @Mapping(target = "firstName", source = "registerRequest.firstName")
    @Mapping(target = "password", source = "encryptedPassword")
    @Mapping(target = "email", source = "registerRequest.email")
    @Mapping(target = "lastName", source = "registerRequest.lastName")
    @Mapping(target = "regDate", source = "localDateTime")
    @Mapping(target = "lastOnlineTime", source = "localDateTime")
    @Mapping(target = "confirmationCode", source = "confirmationCode")
    @Mapping(target = "isApproved", expression = "java(false)")
    @Mapping(target = "messagesPermission", expression = "java(ru.skillbox.diplom.model.enums.MessagesPermission.ALL)")
    @Mapping(target = "isBlocked", expression = "java(false)")
    @Mapping(target = "isDeleted", expression = "java(false)")
    Person toPersonEntity(RegisterRequest registerRequest, String encryptedPassword, LocalDateTime localDateTime, String confirmationCode);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "regDate", source = "person.regDate", qualifiedByName = "convertRegDate")
    @Mapping(target = "birthDate", source = "person.birthDate", qualifiedByName = "convertRegDate")
    @Mapping(target = "lastOnlineTime", source = "person.lastOnlineTime", qualifiedByName = "convertRegDate")
    FriendShipDTO personToFriendShipDTO(Person person);

    PersonDTO toPersonDTO(Person user);

    @Mapping(target = "city", ignore = true)
    @Mapping(target = "country", ignore = true)
    PersonDTO toPersonDTO(User user);

    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "regDate", source = "person.regDate")
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "lastOnlineTime", source = "person.lastOnlineTime")
    @Mapping(target = "blocked", source = "person.isBlocked")
    @Mapping(target = "messagesPermission", source = "person.messagesPermission")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "firstName", source = "person.firstName")
    @Mapping(target = "lastName", source = "person.lastName")
    @Mapping(target = "email", source = "person.email")
    @Mapping(target = "phone", source = "person.phone")
    @Mapping(target = "photo", source = "person.photo")
    @Mapping(target = "about", source = "person.about")
    PersonDTO toPersonDTO(Person person, String token);
}