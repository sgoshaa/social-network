package ru.skillbox.diplom.model.api.response.dialogs;

import lombok.Data;
import ru.skillbox.diplom.model.api.response.dialogs.UnreadData;

@Data
public class UnreadedResponse {

    private String error;
    private Long timestamp;
    private UnreadData data;
}
