package ru.skillbox.diplom.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LikeRequest {

    @JsonProperty("item_id")
    private int itemId;
    private String type;
}
