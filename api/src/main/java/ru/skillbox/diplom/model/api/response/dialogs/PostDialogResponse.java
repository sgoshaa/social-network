package ru.skillbox.diplom.model.api.response.dialogs;

import lombok.Data;

@Data
public class PostDialogResponse {

    private String error;
    private Long timestamp;
    private PostDialog data;
}
