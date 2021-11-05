package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.api.request.IsFriendsRequest;
import ru.skillbox.diplom.model.api.response.friendship.FriendShipResponse;
import ru.skillbox.diplom.model.api.response.friendship.IsFriendsResponse;
import ru.skillbox.diplom.model.api.response.friendship.OkResponse;
import ru.skillbox.diplom.resources.FriendShipController;
import ru.skillbox.diplom.service.FriendShipService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/")
public class ApiFriendShipControllerImpl implements FriendShipController {

    private final FriendShipService friendShipService;

    @Autowired
    public ApiFriendShipControllerImpl(FriendShipService friendShipService) {
        this.friendShipService = friendShipService;
    }

    @Override
    @GetMapping("friends")
    public FriendShipResponse getListFriends(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") Integer itemPerPage,
            @RequestParam(value = "name", defaultValue = " ") String name) {
        return friendShipService.getListFriends(offset, itemPerPage, name);
    }

    @Override
    @DeleteMapping("friends/{id}")
    public OkResponse removeUserFromFriends(@PathVariable String id) {
        return friendShipService.removeUserFromFriends(Integer.valueOf(id));
    }

    @Override
    @PostMapping("friends/{id}")
    public OkResponse acceptOrAddFriendShip(@PathVariable String id) {
        return friendShipService.acceptOrAddFriendShip(Integer.valueOf(id));
    }

    @Override
    @GetMapping("friends/request")
    public FriendShipResponse getListIncomingRequests(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") Integer itemPerPage,
            @RequestParam(value = "name", defaultValue = " ") String name) {
        return friendShipService.getListIncomingRequests(offset, itemPerPage, name);
    }

    @Override
    @GetMapping("friends/recommendations")
    public FriendShipResponse getListRecommendations(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") Integer itemPerPage) {
        return friendShipService.getListRecommendation(offset, itemPerPage);
    }

    @Override
    @PostMapping("is/friends")
    public IsFriendsResponse isFriends(@RequestBody IsFriendsRequest isFriendsRequest) {
        return friendShipService.isFriends(isFriendsRequest);
    }
}
