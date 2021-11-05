package ru.skillbox.diplom.model.api.response.friendship;

import lombok.Data;

@Data
public class DataOkFriendResponse {

    String message;

    public DataOkFriendResponse() {
        message = "ОК";
    }
}
