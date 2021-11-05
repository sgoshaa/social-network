package ru.skillbox.diplom.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("reg_date")
    private LocalDateTime regDate;

    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    @JsonProperty("e_mail")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("about")
    private String about;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("messages_permission")
    private String messagesPermission;

    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

    @JsonProperty("token")
    private String token;
}
