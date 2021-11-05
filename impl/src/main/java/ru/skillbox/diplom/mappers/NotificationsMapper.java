package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.*;
import ru.skillbox.diplom.model.enums.Type;

import java.time.LocalDateTime;

@Mapper(uses = DateConverter.class)
public interface NotificationsMapper {

    @Mapping(target = "typeId",expression = "java(notification.getType().name())")
    @Mapping(target = "sentTime",source = "notification.sentTime", qualifiedByName = "convertRegDate")
    @Mapping(target = "info", source = "info")
    @Mapping(target = "entityId",source = "person")
    @Mapping(target = "id",source = "notification.id")
    NotificationsDataDTO notificationsToDTO(Notification notification,FriendShipDTO person,String info);

    @Mapping(target = "typeId",expression = "java(notification.getType().name())")
    @Mapping(target = "sentTime",source = "notification.sentTime", qualifiedByName = "convertRegDate")
    @Mapping(target = "info", expression = "java(notification.getType().getDescription())")
    @Mapping(target = "entityId",source = "notification.entity")
    @Mapping(target = "id",source = "notification.id")
    NotificationsDataDTO notificationsToDTO(Notification notification);

}
