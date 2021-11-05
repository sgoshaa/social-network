package ru.skillbox.diplom.model.api.response.dialogs;

import lombok.Data;

@Data
public class PostMessageResponse {

    private String error;
    private Long timestamp;
    private MessageResponse data;
}
