package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.model.FriendShip;
import ru.skillbox.diplom.model.IsFriendsDTO;

@Mapper
public interface IsFriendsMapper {

    @Mapping(target = "userId", source = "friendShip.dstPersonId")
    @Mapping(target = "status", expression = "java(friendShip.getStatus().getCode().name())")
    IsFriendsDTO friendShitToIsFriendsDTO(FriendShip friendShip);
}
