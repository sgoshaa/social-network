package ru.skillbox.diplom.mappers;

import javax.annotation.processing.Generated;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.BaseEntity;
import ru.skillbox.diplom.model.FriendShipDTO;
import ru.skillbox.diplom.model.Notification;
import ru.skillbox.diplom.model.NotificationsDataDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-03T16:00:42+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class NotificationsMapperImpl implements NotificationsMapper {

    private final DateConverter dateConverter = new DateConverter();

    @Override
    public NotificationsDataDTO notificationsToDTO(Notification notification, FriendShipDTO person, String info) {
        if ( notification == null && person == null && info == null ) {
            return null;
        }

        NotificationsDataDTO notificationsDataDTO = new NotificationsDataDTO();

        if ( notification != null ) {
            notificationsDataDTO.setSentTime( dateConverter.convertRegDate( notification.getSentTime() ) );
            notificationsDataDTO.setId( notification.getId() );
        }
        if ( person != null ) {
            notificationsDataDTO.setEntityId( person );
        }
        if ( info != null ) {
            notificationsDataDTO.setInfo( info );
        }
        notificationsDataDTO.setTypeId( notification.getType().name() );

        return notificationsDataDTO;
    }

    @Override
    public NotificationsDataDTO notificationsToDTO(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationsDataDTO notificationsDataDTO = new NotificationsDataDTO();

        notificationsDataDTO.setSentTime( dateConverter.convertRegDate( notification.getSentTime() ) );
        notificationsDataDTO.setEntityId( baseEntityToFriendShipDTO( notification.getEntity() ) );
        notificationsDataDTO.setId( notification.getId() );

        notificationsDataDTO.setTypeId( notification.getType().name() );
        notificationsDataDTO.setInfo( notification.getType().getDescription() );

        return notificationsDataDTO;
    }

    protected FriendShipDTO baseEntityToFriendShipDTO(BaseEntity baseEntity) {
        if ( baseEntity == null ) {
            return null;
        }

        FriendShipDTO friendShipDTO = new FriendShipDTO();

        if ( baseEntity.getId() != null ) {
            friendShipDTO.setId( baseEntity.getId() );
        }

        return friendShipDTO;
    }
}
