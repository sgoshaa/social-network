package ru.skillbox.diplom.resources;

import ru.skillbox.diplom.model.api.request.DialogsRequest;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.api.response.dialogs.*;

public interface DialogsController {

    Response<DialogResponse> getDialogs(String query, int offset, int itemPerPage);

    Response<LastMessage> getMessages(Integer id, String query, int offset, int itemPerPage);

    UnreadedResponse getUnread();

    PostMessageResponse postMessage(Integer id, DialogsRequest dialogsRequest);

    PostDialogResponse postDialog(PostDialogRequest postDialogRequest);

}
