package ru.skillbox.diplom.mappers;

import javax.annotation.processing.Generated;
import ru.skillbox.diplom.model.Message;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.api.response.dialogs.DialogPerson;
import ru.skillbox.diplom.model.api.response.dialogs.LastMessage;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class MessageMapperImpl implements MessageMapper {

    @Override
    public LastMessage toLastMessage(Message message, DialogPerson recipient, DialogPerson author) {
        if ( message == null && recipient == null && author == null ) {
            return null;
        }

        LastMessage lastMessage = new LastMessage();

        if ( message != null ) {
            lastMessage.setId( message.getId() );
            lastMessage.setTime( message.getTime() );
            lastMessage.setMessageText( message.getMessageText() );
            lastMessage.setReadStatus( message.getReadStatus() );
        }
        if ( recipient != null ) {
            lastMessage.setRecipient( recipient );
        }
        if ( author != null ) {
            lastMessage.setAuthor( author );
        }

        return lastMessage;
    }

    @Override
    public DialogPerson toRecipient(Person person) {
        if ( person == null ) {
            return null;
        }

        DialogPerson dialogPerson = new DialogPerson();

        dialogPerson.setId( person.getId() );
        dialogPerson.setFirstName( person.getFirstName() );
        dialogPerson.setLastName( person.getLastName() );
        dialogPerson.setLastOnlineTime( person.getLastOnlineTime() );
        dialogPerson.setPhoto( person.getPhoto() );

        return dialogPerson;
    }
}
