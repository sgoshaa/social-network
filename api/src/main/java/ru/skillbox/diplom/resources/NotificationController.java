package ru.skillbox.diplom.resources;

import ru.skillbox.diplom.model.api.response.Response;


public interface NotificationController {
    Response getListNotifications(Integer offset, Integer itemPerPage);

    Response updateNotification(Integer id,Integer offset, Integer itemPerPage,Boolean all);

}
