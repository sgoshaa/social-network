package ru.skillbox.diplom.model.enums;

import lombok.Getter;

@Getter
public enum Type {
    POST_COMMENT("Комментарий к посту"),
    COMMENT_COMMENT("Ответ на комментарий"),
    FRIEND_REQUEST("Запрос дружбы"),
    MESSAGE("Личное сообщение"),
    FRIEND_BIRTHDAY("День рождения друга"),
    POST("Новый пост");

    private final String description;

    Type(String description) {
        this.description = description;
    }
}
