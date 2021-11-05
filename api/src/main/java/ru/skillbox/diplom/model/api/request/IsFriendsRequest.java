package ru.skillbox.diplom.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class IsFriendsRequest {

    @JsonProperty("user_ids")
    List<Integer> userIds;

}
