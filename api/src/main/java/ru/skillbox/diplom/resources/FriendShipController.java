package ru.skillbox.diplom.resources;

import ru.skillbox.diplom.model.api.request.IsFriendsRequest;
import ru.skillbox.diplom.model.api.response.friendship.FriendShipResponse;
import ru.skillbox.diplom.model.api.response.friendship.IsFriendsResponse;
import ru.skillbox.diplom.model.api.response.friendship.OkResponse;

public interface FriendShipController {

    FriendShipResponse getListFriends(Integer offset, Integer itemPerPage, String name);

    OkResponse removeUserFromFriends(String id);

    OkResponse acceptOrAddFriendShip(String id);

    FriendShipResponse getListIncomingRequests(Integer offset, Integer itemPerPage, String name);

    FriendShipResponse getListRecommendations(Integer offset, Integer itemPerPage);

    IsFriendsResponse isFriends(IsFriendsRequest isFriendsRequest);


}
