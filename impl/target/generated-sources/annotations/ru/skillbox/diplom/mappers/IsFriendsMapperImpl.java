package ru.skillbox.diplom.mappers;

import javax.annotation.processing.Generated;
import ru.skillbox.diplom.model.FriendShip;
import ru.skillbox.diplom.model.IsFriendsDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class IsFriendsMapperImpl implements IsFriendsMapper {

    @Override
    public IsFriendsDTO friendShitToIsFriendsDTO(FriendShip friendShip) {
        if ( friendShip == null ) {
            return null;
        }

        IsFriendsDTO isFriendsDTO = new IsFriendsDTO();

        isFriendsDTO.setUserId( friendShip.getDstPersonId() );

        isFriendsDTO.setStatus( friendShip.getStatus().getCode().name() );

        return isFriendsDTO;
    }
}
