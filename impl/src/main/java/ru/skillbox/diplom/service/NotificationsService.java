package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.exception.InvalidRequest;
import ru.skillbox.diplom.mappers.NotificationsMapper;
import ru.skillbox.diplom.mappers.PersonMapper;
import ru.skillbox.diplom.model.*;
import ru.skillbox.diplom.model.api.enums.Errors;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.enums.Type;
import ru.skillbox.diplom.repository.NotificationsRepository;
import ru.skillbox.diplom.repository.PersonRepository;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;
import ru.skillbox.diplom.util.UserUtility;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationsService implements SocialNetworkService {

    private final NotificationsRepository notificationsRepository;
    private final NotificationsMapper notificationsMapper = Mappers.getMapper(NotificationsMapper.class);
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final PersonRepository personRepository;
    private final _UserSettingsService userSettingsService;
    private final Class<NotificationsService> loggerClass = NotificationsService.class;

    @Autowired
    public NotificationsService(NotificationsRepository notificationsRepository, PersonRepository personRepository,_UserSettingsService userSettingsService) {
        this.notificationsRepository = notificationsRepository;
        this.personRepository = personRepository;
        this.userSettingsService = userSettingsService;
    }

   public Response<NotificationsDataDTO> getListNotifications(Integer offset, Integer itemPerPage){
        Integer idCurrentUser = UserUtility.getIdCurrentUser();
        int page = offset / itemPerPage;
        Pageable pageable = PageRequest.of(page,itemPerPage);
        Page<Notification> notifications = notificationsRepository
               .findByPersonIdAndDelivered(idCurrentUser,pageable,false);

        List<Type> enabledSettings = getListEnabledUserSettings(idCurrentUser);
        enabledSettings.add(Type.POST);//todo убрать если на фронте будет реализована настройка оповещений для постов

        Set<NotificationsDataDTO> notificationsDataDTOS = new HashSet<>();
        notifications.forEach(notification ->{
                   switch (notification.getType()) {
                       case POST:
                           if (enabledSettings.contains(Type.POST)) {
                               Post post = (Post) notification.getEntity();
                               notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification,personMapper.personToFriendShipDTO(post.getAuthor()), post.getTitle()));
                           }
                           break;
                       case FRIEND_REQUEST:
                           if (enabledSettings.contains(Type.FRIEND_REQUEST)) {
                               FriendShip friendShip = (FriendShip) notification.getEntity();
                               Person person = personRepository.findById(friendShip.getSrcPersonId()).get();
                               Person currentUser = personRepository.findById(idCurrentUser).get();
                               notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification,personMapper.personToFriendShipDTO(person)
                                       ,currentUser.getFirstName()+" "+currentUser.getLastName()));
                           }
                           break;
                       case POST_COMMENT:
                           if (enabledSettings.contains(Type.POST_COMMENT)) {
                               PostComment postComment = (PostComment) notification.getEntity();
                               notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification,personMapper.personToFriendShipDTO(postComment.getPerson())
                                       ,postComment.getCommentText().substring(0, Math.min(postComment.getCommentText().length(), 50))));
                           }
                           break;
                       case COMMENT_COMMENT:
                           if (enabledSettings.contains(Type.COMMENT_COMMENT)) {
                               PostComment commentComment = (PostComment) notification.getEntity();
                               notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification,personMapper.personToFriendShipDTO(commentComment.getPerson())
                                       ,commentComment.getCommentText().substring(0, Math.min(commentComment.getCommentText().length(), 50))));
                           }
                           break;
                       case FRIEND_BIRTHDAY:
                           if (enabledSettings.contains(Type.FRIEND_BIRTHDAY)) {
                               Person person = (Person) notification.getEntity();
                               notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification,personMapper.personToFriendShipDTO(person),"Сегодня день рождение у вашего друга "));
                           }
                           break;
                       case MESSAGE:
                           if (enabledSettings.contains(Type.MESSAGE)) {
                               Message message = (Message) notification.getEntity();
                               notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification,personMapper.personToFriendShipDTO(message.getAuthorId())
                                       ,message.getMessageText().substring(0, Math.min(message.getMessageText().length(), 50))));
                           }
                           break;
                   }
        });
       //log(loggerClass,LoggerLevel.INFO,"getListNotifications",LoggerValue.GETLIST_NOTIFICATION,"getting notifications");
       return createResponse(notificationsDataDTOS,offset,itemPerPage);
    }
    @Transactional
    public Response<NotificationsDataDTO> updateNotification(Integer id,Integer offset, Integer itemPerPage,Boolean all) {

        if (all!=null && all){
            return deleteAllNotifications(all,offset, itemPerPage);
        }
        Optional<Notification> optionalNotification = notificationsRepository.findById(id);
        if (optionalNotification.isEmpty()){
            log(loggerClass,LoggerLevel.ERROR,"updateNotification",
                    LoggerValue.UPDATE_NOTIFICATION,"не существует оповещения c таким id! "+id);
            throw new InvalidRequest("не существует такого оповещения!");
        }
        Notification notification = optionalNotification.get();
        notification.setDelivered(true);
        notificationsRepository.save(notification);
        log(loggerClass,LoggerLevel.INFO,"updateNotification",
                LoggerValue.UPDATE_NOTIFICATION,"updateNotification "+id);

        Set<NotificationsDataDTO> notificationsDataDTOS = new HashSet<>();
        notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification));

        return createResponse(notificationsDataDTOS,offset,itemPerPage);
    }
    private Response<NotificationsDataDTO> createResponse(Set<NotificationsDataDTO> data, Integer offset, Integer itemPerPage){

        Response<NotificationsDataDTO> notificationsResponse = new Response<>();
        notificationsResponse.setData(data);
        notificationsResponse.setError(Errors.NO_ERRORS);
        notificationsResponse.setTimestamp(new Date().getTime());
        notificationsResponse.setOffset(offset);
        notificationsResponse.setPerPage(itemPerPage);
        notificationsResponse.setTotal((long) data.size());

        return notificationsResponse;
    }

    @Transactional
    public void createNotification(Type type, Integer personId, BaseEntity entity, String contact){
        notificationsRepository.save(createNotificationObject(type,personId,entity,contact));
    }
    private Notification createNotificationObject(Type type, Integer personId, BaseEntity entity, String contact){
        Notification notification = new Notification();

        notification.setType(type);
        notification.setSentTime(LocalDateTime.now());
        notification.setPersonId(personId);
        notification.setContact(contact);
        notification.setEntity(entity);
        notification.setDelivered(false);

        return notification;
    }
    @Transactional
    public void createNotificationsToFriends(Type type,List<Person> friends, BaseEntity entity){
        if (friends.isEmpty()){
            return;
        }
        List<Notification>notifications = new ArrayList<>();
        friends.forEach(person ->{
            notifications.add(createNotificationObject(type,person.getId(),entity,"email: "+person.getEmail()+" phone: "+person.getPhone()));
        });
        notificationsRepository.saveAll(notifications);
    }

    @Transactional
    public Response<NotificationsDataDTO> deleteAllNotifications(Boolean all,Integer offset, Integer itemPerPage) {

        List<Notification> notifications = notificationsRepository
                .findByPersonIdAndDelivered(UserUtility.getIdCurrentUser(), false);

        Set<NotificationsDataDTO> notificationsDataDTOS = new HashSet<>();

        notifications.forEach(notification -> {
                notification.setDelivered(true);
                notificationsDataDTOS.add(notificationsMapper.notificationsToDTO(notification));
        });
        log(loggerClass,LoggerLevel.INFO,"deleteAllNotifications",
                LoggerValue.UPDATE_NOTIFICATION,"updateAllNotification");

        notificationsRepository.saveAll(notifications);
        return createResponse(notificationsDataDTOS,offset,itemPerPage);
    }
    private List<Type> getListEnabledUserSettings(Integer idCurrentUser){
        List<UserSetting> settings = userSettingsService.getAllByUserId(idCurrentUser);
        return settings.stream()
                .filter(UserSetting::isValue)
                .map(UserSetting::getType)
                .map(NotificationType::getCode)
                .collect(Collectors.toList());
    }
    @Transactional
    public void createListFriendsBirthdayToday(Integer idCurrentUser){
        List<Person> personList = notificationsRepository.getListFriendsBirthdayToday(idCurrentUser);
        if (personList.size() > 0) {
            List<Notification> notifications = new ArrayList<>();
            Person currentUser = personRepository.getOne(idCurrentUser);
            personList.forEach(person -> {
                notifications.add(createNotificationObject(Type.FRIEND_BIRTHDAY,idCurrentUser,
                        person,"email: "+currentUser.getEmail()+" phone: "+currentUser.getPhone()));
                log(loggerClass,LoggerLevel.INFO,"createListFriendsBirthdayToday",
                        LoggerValue.CREATE_BIRTHDAY_NOTIFICATION,"createListFriendsBirthdayToday "+person.getEmail());
            });
            notificationsRepository.saveAll(notifications);
        }
    }
}
