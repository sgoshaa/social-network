package ru.skillbox.diplom.model;
//"data": [
//        {
//        "id": 1,
//        "type_id": 1,
//        "sent_time": 1559751301818,
//        "entity_id": 1,
//        "info": "string"
//        }
//        ]

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.enums.Type;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class NotificationsDataDTO {

    private Integer id;

    @JsonProperty("event_type")
    @Enumerated(EnumType.STRING)
    private String typeId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("sent_time")
    private Long sentTime;

    @JsonProperty("entity_author")
    private FriendShipDTO entityId;

    private String info;
}
