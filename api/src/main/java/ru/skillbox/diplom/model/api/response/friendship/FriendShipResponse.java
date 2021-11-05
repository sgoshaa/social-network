package ru.skillbox.diplom.model.api.response.friendship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.FriendShipDTO;

import java.util.Date;
import java.util.List;

@Data
public class FriendShipResponse {

    private String error;

    @JsonProperty("timestamp")
    private long timeStamp;

    private Integer total;

    private Integer offset;

    private Integer perPage;

    private List<FriendShipDTO> data;

    public FriendShipResponse responseSuccess(List<FriendShipDTO> friends, Integer offset, Integer perPage,Integer total) {

        error = "string";
        timeStamp = new Date().getTime();
        this.total = total;
        this.offset = offset;
        this.perPage = perPage;
        data = friends;

        return this;
    }

}
