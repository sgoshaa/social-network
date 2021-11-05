package ru.skillbox.diplom.model;
//{
//        "data": [
//        {
//        "user_id": 3,
//        "status": "FRIEND"
//        }
//        ]
//}

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IsFriendsDTO {

    @JsonProperty("user_id")
    Integer userId;

    String status;

}
