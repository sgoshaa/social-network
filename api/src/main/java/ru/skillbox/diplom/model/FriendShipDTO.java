package ru.skillbox.diplom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class FriendShipDTO {

    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("reg_date")
    private long regDate;

    @JsonProperty("birth_date")
    private long birthDate;

    @JsonProperty("e_mail")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("about")
    private String about;

    @JsonProperty("city")
    private City city;

    @JsonProperty("country")
    private Country country;

    @JsonProperty("messages_permission")
    private String messagesPermission;

    @JsonProperty("last_online_time")
    private long lastOnlineTime;

    @JsonProperty("is_blocked")
    private boolean isBlocked;
}
