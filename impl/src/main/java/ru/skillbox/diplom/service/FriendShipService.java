package ru.skillbox.diplom.service;


import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.exception.InvalidRequest;
import ru.skillbox.diplom.mappers.IsFriendsMapper;
import ru.skillbox.diplom.mappers.PersonMapper;
import ru.skillbox.diplom.model.*;
import ru.skillbox.diplom.model.api.request.IsFriendsRequest;
import ru.skillbox.diplom.model.api.response.friendship.FriendShipResponse;
import ru.skillbox.diplom.model.api.response.friendship.IsFriendsResponse;
import ru.skillbox.diplom.model.api.response.friendship.OkResponse;
import ru.skillbox.diplom.model.enums.FriendshipCode;
import ru.skillbox.diplom.model.enums.Type;
import ru.skillbox.diplom.repository.FriendShipRepository;
import ru.skillbox.diplom.repository.PersonRepository;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;
import ru.skillbox.diplom.util.UserUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendShipService implements SocialNetworkService {

    private final FriendShipRepository friendShipRepository;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final IsFriendsMapper isFriendsMapper = Mappers.getMapper(IsFriendsMapper.class);
    private final NotificationsService notificationsService;
    private final Class<FriendShipService> loggerClass = FriendShipService.class;

    @Autowired
    public FriendShipService(FriendShipRepository friendShipRepository, PersonRepository personRepository, NotificationsService notificationsService) {
        this.friendShipRepository = friendShipRepository;
        this.personRepository = personRepository;
        this.notificationsService = notificationsService;
    }

    public FriendShipResponse getListFriends(Integer offset, Integer itemPerPage, String name) {
        List<Person> personList = friendShipRepository.findByCurrentId(UserUtility.getIdCurrentUser());
        List<FriendShipDTO> friendList = new ArrayList();

        personList.forEach(person -> friendList.add(personMapper.personToFriendShipDTO(person)));
        if (friendList.size() > 0) {
            log(loggerClass, LoggerLevel.INFO, "getListFriends",
                    LoggerValue.GET_LIST_FRIENDS, "get list friend for current user ");
        }
        return new FriendShipResponse().responseSuccess(friendList, offset, itemPerPage,friendList.size());
    }

    @Transactional
    public OkResponse removeUserFromFriends(Integer id) {
        Integer idCurrentUser = UserUtility.getIdCurrentUser();
        deleteFriendship(idCurrentUser,id);
        deleteFriendship(id,idCurrentUser);//удаляем обратную дружбу
        log(loggerClass, LoggerLevel.INFO, "removeUserFromFriends",
                LoggerValue.DEL_FRIEND, "deleting a friend from the current user "+idCurrentUser);

        return new OkResponse();
    }
    //Добавление или принятие пользователя в друзья
    @Transactional
    public OkResponse acceptOrAddFriendShip(Integer id) {

        Integer idCurrentUser = UserUtility.getIdCurrentUser();

        Optional<Person> candidateForFriends = personRepository.findById(id);

        if (candidateForFriends.isEmpty()) {
            log(loggerClass, LoggerLevel.ERROR, "acceptOrAddFriendShip",
                    LoggerValue.ADD_FRIEND, "The user with this id is not in the database."+id);
            throw new InvalidRequest("The user with this id is not in the database.");

        }
        Person candidate = candidateForFriends.get();
        if(candidate.getIsDeleted()){
            log(loggerClass, LoggerLevel.ERROR, "acceptOrAddFriendShip",
                    LoggerValue.ADD_FRIEND, "The user is marked for deletion."+id);
            throw new InvalidRequest("The user is marked for deletion.");
        }
        if (id.equals(idCurrentUser)){
            log(loggerClass, LoggerLevel.ERROR, "acceptOrAddFriendShip",
                    LoggerValue.ADD_FRIEND, "We tried to add ourselves as friends.Id current user: " +idCurrentUser+
                            " and id candidateForFriends "+id);
            throw new InvalidRequest("We tried to add ourselves as friends.");
        }

        Optional<FriendShip> suchRequestFriendShip = friendShipRepository.findBySrcPersonId(idCurrentUser)
                .stream()
                .filter(friendShip ->friendShip.getDstPersonId().equals(id)).findFirst();

        if (suchRequestFriendShip.isPresent()){
            log(loggerClass, LoggerLevel.ERROR, "acceptOrAddFriendShip",
                    LoggerValue.ADD_FRIEND, "There is already such a request. friendship.id = "+suchRequestFriendShip.get().getId());
            throw new InvalidRequest("There is already such a request.");
        }

        Optional<FriendShip> friendShipOptional = friendShipRepository.findByDstPersonId(idCurrentUser)
                .stream()
                .filter(friendShip -> friendShip.getSrcPersonId().equals(candidate.getId()))
                .findFirst();

        FriendShip friendShip;

        if (friendShipOptional.isPresent() && friendShipOptional.get().getStatus().getCode().equals(FriendshipCode.REQUEST)) {

            friendShip = friendShipOptional.get();
            FriendShipStatus status = friendShip.getStatus();
            status.setCode(FriendshipCode.FRIEND);
            status.setName(FriendshipCode.FRIEND.name());
            friendShip.setStatus(status);
            friendShipRepository.save(friendShip);

            friendShip = createFriendShip(idCurrentUser,id,FriendshipCode.FRIEND);
            log(loggerClass, LoggerLevel.INFO, "acceptOrAddFriendShip", LoggerValue.ADD_FRIEND, "accepted as a friend "+id);
            //создаем нотификации
            notificationsService.createNotification(Type.FRIEND_REQUEST,id,friendShip,"email: "+candidate.getEmail()+" phone: "+candidate.getPhone());

        } else if (friendShipOptional.isPresent() && friendShipOptional.get().getStatus().getCode().equals(FriendshipCode.FRIEND)) {
            log(loggerClass, LoggerLevel.ERROR, "acceptOrAddFriendShip",
                    LoggerValue.ADD_FRIEND, "The user is already a friend."+id);
            throw new InvalidRequest("The user is already a friend.");
        } else {
            //создаем дружбу
            friendShip = createFriendShip(idCurrentUser,id,FriendshipCode.REQUEST);
            log(loggerClass, LoggerLevel.INFO, "acceptOrAddFriendShip",
                    LoggerValue.ADD_FRIEND, "add friend"+id);
            //создаем нотификации
            notificationsService.createNotification(Type.FRIEND_REQUEST,id,friendShip,"email: "+candidate.getEmail()+" phone: "+candidate.getPhone());

        }
        return new OkResponse();
    }
//  Список заявок в друзья
    public FriendShipResponse getListIncomingRequests(Integer offset, Integer itemPerPage, String name) {

        List<Integer> idFriendShip = friendShipRepository.findByDstPersonId(UserUtility.getIdCurrentUser())
                .stream()
                .filter(friendShip -> friendShip.getStatus().getCode().equals(FriendshipCode.REQUEST))
                .map(FriendShip::getSrcPersonId)
                .collect(Collectors.toList());

        List<Person> personList = personRepository.findAllById(idFriendShip);

        List<FriendShipDTO> friendShipDTOs = new ArrayList<>();

        personList.forEach(person -> friendShipDTOs.add(personMapper.personToFriendShipDTO(person)));
        if (personList.size()>0) {
            log(loggerClass, LoggerLevel.INFO, "getListIncomingRequests", LoggerValue.GET_LIST_REQUEST_FRIEND, "");
        }
        return new FriendShipResponse().responseSuccess(friendShipDTOs, offset, itemPerPage,friendShipDTOs.size());

    }
    //выводиться список возможных знакомых
    public FriendShipResponse getListRecommendation(Integer offset, Integer itemPerPage) {
        Integer currentId = UserUtility.getIdCurrentUser();
        List<Person> friendsList = friendShipRepository.getListPossibleFriends(currentId);
        List<FriendShipDTO> friendShipDTOs = new ArrayList<>();
        friendsList.forEach(person ->
                friendShipDTOs.add(personMapper.personToFriendShipDTO(person)));

        if (friendsList.size()>0) {
            log(loggerClass, LoggerLevel.INFO, "getListRecommendation",
                    LoggerValue.GET_LIST_RECOMENDATIONS_FRIEND, "for current user "+currentId);
        }
        return new FriendShipResponse().responseSuccess(friendShipDTOs, offset, itemPerPage,friendShipDTOs.size());
    }

    //Получить информацию является ли пользователь другом указанных пользователей
    public IsFriendsResponse isFriends(IsFriendsRequest isFriendsRequest) {

        Integer currentId = UserUtility.getIdCurrentUser();
        List<Integer> isFriends = isFriendsRequest.getUserIds();

        if (isFriends.isEmpty()) {
            log(loggerClass, LoggerLevel.ERROR, "isFriends",
                    LoggerValue.IS_FRIENDS, "The users_id list is empty.");
            throw new InvalidRequest("The users_id list is empty.");

        }
        List<FriendShip> listFriendsCurrentUser = friendShipRepository.findAllByDstPersonIdIn(isFriends)
                .stream()
                .filter(friendShip -> friendShip.getSrcPersonId().equals(currentId))
                .collect(Collectors.toList());

        List<IsFriendsDTO> isFriendsDTOList = new ArrayList<>();

        listFriendsCurrentUser.forEach(friendShip -> isFriendsDTOList.add(isFriendsMapper.friendShitToIsFriendsDTO(friendShip)));
        log(loggerClass, LoggerLevel.INFO, "isFriends",
                LoggerValue.IS_FRIENDS, "for current user "+currentId);
        return new IsFriendsResponse(isFriendsDTOList);
    }

    private FriendShip createFriendShip(Integer srcPersonId,Integer dstPersonId,FriendshipCode friendshipCode ){

        FriendShip friendShip = new FriendShip();
        friendShip.setSrcPersonId(srcPersonId);
        friendShip.setDstPersonId(dstPersonId);

        //создаем статус дружбы
        FriendShipStatus status = new FriendShipStatus();
        status.setCode(friendshipCode);
        status.setTime(LocalDateTime.now());
        status.setName(friendshipCode.name());

        friendShip.setStatus(status);
        friendShipRepository.save(friendShip);

        return friendShip;
    }

    public List<Integer> getListFriendsId(Integer currentUser){
        List<FriendShip> friendShip = friendShipRepository.findBySrcPersonId(currentUser);
        List<Integer> idFriend = friendShip.stream()
                .filter(friend -> friend.getStatus().getCode().equals(FriendshipCode.FRIEND))
                .map(FriendShip::getDstPersonId)
                .collect(Collectors.toList());
        return idFriend;
    }
    public List<Person> getListFriends(Integer currentUser){
        List<Integer>idFriends = getListFriendsId(currentUser);
        return personRepository.findAllById(idFriends);
    }

    private void deleteFriendship(Integer srcPersonId, Integer dstPersonId){
        Optional<FriendShip> friendShipOptional = friendShipRepository.findBySrcPersonIdAndDstPersonId(srcPersonId,dstPersonId);
        if (friendShipOptional.isEmpty()) {
            log(loggerClass, LoggerLevel.ERROR, "removeUserFromFriends",
                    LoggerValue.DEL_FRIEND, "The object to delete was not found."+dstPersonId);
            throw new InvalidRequest("The object to delete was not found."+dstPersonId);
        }
        FriendShip friendShip = friendShipOptional.get();
        friendShipRepository.delete(friendShip);
    }
}
