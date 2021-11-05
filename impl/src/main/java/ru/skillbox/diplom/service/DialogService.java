package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.exception.InvalidRequest;
import ru.skillbox.diplom.mappers.MessageMapper;
import ru.skillbox.diplom.model.Dialog;
import ru.skillbox.diplom.model.Message;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.User2Dialog;
import ru.skillbox.diplom.model.api.request.DialogsRequest;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.api.response.dialogs.*;
import ru.skillbox.diplom.model.enums.ReadStatus;
import ru.skillbox.diplom.model.enums.Type;
import ru.skillbox.diplom.repository.*;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import java.util.*;

@Service
public class DialogService implements SocialNetworkService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;
    private final User2DialogRepository user2DialogRepository;
    private final DialogRepository dialogRepository;
    private final MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);
    private final Class<EmailService> loggerClass = EmailService.class;
    private final NotificationsService notificationsService;

    public DialogService(UserRepository userRepository, MessageRepository messageRepository, PersonRepository personRepository, User2DialogRepository user2DialogRepository, DialogRepository dialogRepository, NotificationsService notificationsService) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
        this.user2DialogRepository = user2DialogRepository;
        this.dialogRepository = dialogRepository;
        this.notificationsService = notificationsService;
    }

    public Response<DialogResponse> getDialogs(String query, int offset, int itemPerPage) {

        Response response = new Response();
        List<DialogResponse> dialogDataList = new ArrayList<>();

        response.setTimestamp(new Date().getTime());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);

        List<User2Dialog> u2dListForId = user2DialogRepository.findByUserId(getCurrentUser().getId());
        List<Integer> dialogIdsList = new ArrayList<>();
        for (User2Dialog u2d : u2dListForId) {
            int dialogId = u2d.getDialogId();
            dialogIdsList.add(dialogId);
        }

        if (!dialogIdsList.isEmpty()) {
            response.setTotal((long) dialogIdsList.size());


            for (Integer id : dialogIdsList) {

                DialogResponse dialogResponse = new DialogResponse();

                int unreadCount = messageRepository.findAllUnreadMessages(getCurrentUser().getId(), id).size();

                dialogResponse.setUnreadCount(unreadCount);

                Page<Message> messages = messageRepository.findAllMessages(id, getPaging(offset, itemPerPage));
                Message message = messages.getContent().get(0);

                List<User2Dialog> u2dList = user2DialogRepository.findByDialogId(id);
                for (User2Dialog u2d : u2dList) {
                    if (!Objects.equals(u2d.getUserId(), getCurrentUser().getId())) {
                        LastMessage lastMessage = messageMapper.toLastMessage(message, convertPerson(personRepository.findById(u2d.getUserId()).get()), convertPerson(personRepository.findById(getCurrentUser().getId()).get()));
                        lastMessage.setSentByMe(getCurrentUser().getId() == message.getAuthorId().getId());
                        dialogResponse.setLastMessage(lastMessage);
                    }
                }

                dialogResponse.setId(id);

                dialogDataList.add(dialogResponse);
            }
            Page<Message> messagePage = messageRepository.findMessages(getCurrentUser().getId(), getPaging(offset, itemPerPage));

            for (Message m : messagePage) {
                if (m.getReadStatus().equals(ReadStatus.SENT)) {
                    m.setReadStatus(ReadStatus.READ);
                    messageRepository.save(m);
                }
            }

        } else {
            response.setTotal(0L);
        }
        response.setData(dialogDataList);

        return response;
    }

    public UnreadedResponse getUnread() {
        UnreadedResponse unreadedResponse = new UnreadedResponse();
        UnreadData unreadData = new UnreadData();

        int count = messageRepository.findUnreadedMessages(getCurrentUser().getId()).size();

        unreadData.setCount(count);
        unreadedResponse.setData(unreadData);

        return unreadedResponse;
    }

    public Response<LastMessage> getMessages(int id, String query, int offset, int itemPerPage) {

        Response messageResponse = new Response();

        messageResponse.setTimestamp(new Date().getTime());
        messageResponse.setTotal(messageRepository.findMessages(getCurrentUser().getId(), getPaging(offset, itemPerPage)).getTotalElements());
        messageResponse.setOffset(offset);
        messageResponse.setPerPage(itemPerPage);

        Page<Message> messageList = messageRepository.findAllMessages(id, getPaging(offset, itemPerPage));
        List<LastMessage> lastMessagesList = new ArrayList<>();

        for (Message message : messageList) {

            LastMessage lastMessage = messageMapper.toLastMessage(message, convertPerson(message.getRecipientId()), convertPerson(message.getAuthorId()));
            lastMessage.setSentByMe(getCurrentUser().getId() == message.getAuthorId().getId());
            lastMessagesList.add(lastMessage);
        }

        messageResponse.setData(lastMessagesList);

        return messageResponse;
    }

    public PostMessageResponse postMessage(Integer id, DialogsRequest dialogsRequest) {
        PostMessageResponse postMessageResponse = new PostMessageResponse();
        MessageResponse messageResponse = new MessageResponse();
        Message message = new Message();

        postMessageResponse.setTimestamp(new Date().getTime());

        int currentUserId = getCurrentUser().getId();
        Person author = personRepository.findById(currentUserId).get();

        List<User2Dialog> u2dList = user2DialogRepository.findByDialogId(id);
        for (User2Dialog u2d : u2dList) {
            if (!Objects.equals(u2d.getUserId(), author.getId())) {
                message.setRecipientId(personRepository.findById(u2d.getUserId()).get());
                messageResponse.setRecipient(u2d.getUserId());
            }
        }

        message.setTime(new Date());
        message.setAuthorId(author);
        message.setMessageText(dialogsRequest.getMessageText());
        message.setReadStatus(ReadStatus.SENT);
        message.setDialogId(id);

        messageRepository.save(message);
        log(loggerClass, LoggerLevel.INFO, "post", LoggerValue.POST_REQUEST, dialogsRequest.getMessageText());
        //добавляем оповещение
        notificationsService.createNotification(Type.MESSAGE, message.getRecipientId().getId(), message,
                "email: " + message.getRecipientId().getEmail() + " phone: " + message.getRecipientId().getPhone());
        //
        messageResponse.setId(message.getId());
        messageResponse.setTime(new Date());
        messageResponse.setAuthor(author.getId());
        messageResponse.setMessageText(dialogsRequest.getMessageText());
        messageResponse.setReadStatus(ReadStatus.SENT);

        postMessageResponse.setData(messageResponse);

        return postMessageResponse;
    }

    public PostDialogResponse postDialog(PostDialogRequest postDialogRequest) {
        PostDialogResponse postDialogResponse = new PostDialogResponse();
        Message message = new Message();
        PostDialog postDialog = new PostDialog();
        User2Dialog u2dAuthor = new User2Dialog();
        User2Dialog u2dRecipient = new User2Dialog();

        int recipientId = postDialogRequest.getUserIds().get(0);
        Person recipient = personRepository.findById(recipientId).get();

        int currentUserId = getCurrentUser().getId();
        Person author = personRepository.findById(currentUserId).get();

        List<User2Dialog> recipientDialogs = user2DialogRepository.findByUserId(recipientId);
        List<User2Dialog> currentUserDialogs = user2DialogRepository.findByUserId(currentUserId);

        Boolean dialogExist = false;
        Boolean userDeleted = false;

        for (User2Dialog u2dRecip : recipientDialogs) {
            for (User2Dialog u2dCurrent : currentUserDialogs) {
                int recipientDialogId = u2dRecip.getDialogId();
                int currentDialogId = u2dCurrent.getDialogId();

                if (currentDialogId == recipientDialogId) {
                    dialogExist = true;
                    break;
                }
                if (userRepository.findById(recipientId).get().getIsDeleted()){
                    userDeleted = true;
                    break;
                }
            }
        }

        if ((!recipient.getIsBlocked()) && (!dialogExist) && (!userDeleted)) {

            Dialog dialog = new Dialog();
            dialogRepository.save(dialog);
            log(loggerClass, LoggerLevel.INFO, "post", LoggerValue.POST_REQUEST, dialog.toString());

            message.setTime(new Date());
            message.setAuthorId(author);
            message.setRecipientId(recipient);
            message.setMessageText("New dialog with " + recipient.getFirstName() + " was created");
            message.setReadStatus(ReadStatus.SENT);
            message.setDialogId(dialog.getId());

            messageRepository.save(message);
            log(loggerClass, LoggerLevel.INFO, "post", LoggerValue.POST_REQUEST, "New dialog with " + author.getFirstName() + " was created");

            u2dAuthor.setDialogId(dialog.getId());
            u2dAuthor.setUserId(currentUserId);
            user2DialogRepository.save(u2dAuthor);
            log(loggerClass, LoggerLevel.INFO, "post", LoggerValue.POST_REQUEST, u2dAuthor.toString());


            u2dRecipient.setDialogId(dialog.getId());
            u2dRecipient.setUserId(recipientId);
            user2DialogRepository.save(u2dRecipient);
            log(loggerClass, LoggerLevel.INFO, "post", LoggerValue.POST_REQUEST, u2dRecipient.toString());

            postDialog.setId(dialog.getId());

            postDialogResponse.setTimestamp(new Date().getTime());
            postDialogResponse.setData(postDialog);
        } else throw new InvalidRequest("User is blocked or dialog already exists");

        return postDialogResponse;
    }

    public DeleteResponse deleteDialog(Integer id) {
        DeleteResponse deleteResponse = new DeleteResponse();
        PostDialog data = new PostDialog();

        dialogRepository.deleteById(id);
        user2DialogRepository.deleteByDialogId(id);
        messageRepository.deleteByDialogId(id);

        deleteResponse.setTimestamp(new Date().getTime());
        data.setId(id);
        deleteResponse.setData(data);

        return deleteResponse;
    }

    public ru.skillbox.diplom.model.User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Optional<ru.skillbox.diplom.model.User> optionalUser = userRepository.findByEmail(user.getUsername());
        return optionalUser.orElse(null);
    }

    public Pageable getPaging(int offset, int itemPerPage) {
        Pageable paging;
        int pageNumber = offset / itemPerPage;
        paging = PageRequest.of(pageNumber, itemPerPage);

        return paging;
    }

    public DialogPerson convertPerson(Person person) {

        return messageMapper.toRecipient(person);
    }
}
