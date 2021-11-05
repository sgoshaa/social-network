package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.NotificationsDataDTO;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.resources.NotificationController;
import ru.skillbox.diplom.service.NotificationsService;

@RestController
@RequestMapping("/api/v1/")
public class ApiNotificationsControllerImpl implements NotificationController {

    private final NotificationsService notificationsService;

    @Autowired
    public ApiNotificationsControllerImpl(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @Override
    @GetMapping("notifications")
    public Response<NotificationsDataDTO> getListNotifications(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                               @RequestParam(value = "itemPerPage", defaultValue = "20") Integer itemPerPage) {
        return notificationsService.getListNotifications(offset,itemPerPage);
    }

    @Override
    @PutMapping("notifications")
    public Response<NotificationsDataDTO> updateNotification(@RequestParam(value = "offset",required = false, defaultValue = "0") Integer offset,
                                                             @RequestParam(value = "itemPerPage",required = false, defaultValue = "20") Integer itemPerPage,
                                                             @RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) Boolean all) {
        return notificationsService.updateNotification(id,offset,itemPerPage,all);
    }
}
