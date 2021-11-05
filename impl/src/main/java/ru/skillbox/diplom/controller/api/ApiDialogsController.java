package ru.skillbox.diplom.controller.api;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.model.api.request.DialogsRequest;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.api.response.dialogs.*;
import ru.skillbox.diplom.resources.DialogsController;
import ru.skillbox.diplom.service.DialogService;

@RestController
@RequestMapping("/api/v1/dialogs")
public class ApiDialogsController implements DialogsController {

    private final DialogService dialogService;

    public ApiDialogsController(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    @GetMapping
    public Response<DialogResponse> getDialogs(@RequestParam(value = "query", defaultValue = "") String query,
                                               @RequestParam(value = "offset", defaultValue = "0") int offset,
                                               @RequestParam(value = "itemPerPage", defaultValue = "20") int itemPerPage) {
        return dialogService.getDialogs(query, offset, itemPerPage);
    }

    @GetMapping("/{id}/messages")
    public Response<LastMessage> getMessages(@PathVariable Integer id,
                                             @RequestParam(value = "query", defaultValue = "") String query,
                                             @RequestParam(value = "offset", defaultValue = "0") int offset,
                                             @RequestParam(value = "itemPerPage", defaultValue = "20") int itemPerPage) {
        return dialogService.getMessages(id, query, offset, itemPerPage);
    }

    @GetMapping("/unreaded")
    public UnreadedResponse getUnread() {
        return dialogService.getUnread();
    }

    @PostMapping("/{id}/messages")
    public PostMessageResponse postMessage(@PathVariable Integer id,
                                           @RequestBody DialogsRequest dialogsRequest) {
        return dialogService.postMessage(id, dialogsRequest);
    }

    @PostMapping
    public PostDialogResponse postDialog(@RequestBody PostDialogRequest postDialogRequest) {
        return dialogService.postDialog(postDialogRequest);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteDialog(@PathVariable Integer id){
        return dialogService.deleteDialog(id);
    }
}
