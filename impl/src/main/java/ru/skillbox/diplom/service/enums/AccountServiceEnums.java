package ru.skillbox.diplom.service.enums;

public enum AccountServiceEnums {
    LINK_REGISTRATION(":8086/api/v1/account/confirm/", "Ссылка для подтверждения почты."),
    TOKEN_RECOVERY(":8080/change-password?token=", "Ссылка для восстановления пароля."),
    EMAIL_WRONG_USER("not_this_email", "wrong user asked for email recovery");

    public String link;
    public String description;

    AccountServiceEnums(String link, String description) {
        this.link = link;
        this.description = description;
    }

}
