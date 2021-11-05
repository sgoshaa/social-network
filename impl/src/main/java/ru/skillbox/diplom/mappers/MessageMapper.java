package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.model.Message;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.api.response.dialogs.DialogPerson;
import ru.skillbox.diplom.model.api.response.dialogs.LastMessage;

@Mapper
public interface MessageMapper {

    @Mapping(target = "id", source = "message.id")
    @Mapping(target = "time", source = "message.time")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "messageText", source = "message.messageText")
    @Mapping(target = "readStatus", source = "message.readStatus")
    @Mapping(target = "sentByMe", ignore = true)
    LastMessage toLastMessage(Message message, DialogPerson recipient, DialogPerson author);

    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "firstName", source = "person.firstName")
    @Mapping(target = "lastName", source = "person.lastName")
    @Mapping(target = "lastOnlineTime", source = "person.lastOnlineTime")
    DialogPerson toRecipient(Person person);

}
