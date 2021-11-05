package ru.skillbox.diplom.model.api.response.friendship;
//{
//        "error": "string",
//        "timestamp": 1559751301818,
//        "data": {
//        "message": "ok"
//        }
//}

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OkResponse {

    String error;

    @JsonProperty("timestamp")
    Long timeStamp;

    DataOkFriendResponse data;

    public OkResponse() {
        error = "string";
        timeStamp = new Date().getTime();
        data = new DataOkFriendResponse();
    }
}
