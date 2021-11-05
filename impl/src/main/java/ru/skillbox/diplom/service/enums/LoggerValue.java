package ru.skillbox.diplom.service.enums;

public enum LoggerValue {
    PERSON_SAVED(" :: person has been saved :: "),
    PERSON_DELETED(" :: person has been deleted :: "),
    PERSON_RECOVERY(" :: person asked for recovery :: "),
    PERSON_RECOVERY_DENIED(" :: recovery denied for person :: "),
    PERSON_CONFIRMATION(" :: person confirmation code :: "),
    PERSON_CHANGE_REQUEST(" :: person changing profile settings :: "),
    PERSON_CHANGE_RESPONSE(" :: changed :: "),
    PERSON_CHANGE_DENIED(" :: denied to change profile settings :: "),
    POST_REQUEST(" :: post request :: "),
    POST_CREATED(" :: post has been created :: "),
    POST_EDIT(" :: post was changed :: "),
    POST_DELETED(" :: post has been deleted :: "),
    POST_ERROR(" :: post error :: "),
    POSTS_FOUND(" :: posts amount found :: "),
    POSTS_NOT_FOUND(" :: posts not found :: "),
    COMMENT_DELETED(" :: comment has been deleted :: "),
    LOGIN_REQUESTED(" :: login request :: "),
    LOGIN_FAILED(" :: failed to log in :: "),
    LOGIN_ACCEPTED(" :: successfully logged in :: "),
    ACCOUNT_REQUEST(" :: account request :: "),
    ACCOUNT_404(" :: account not found :: "),
    EMAIL_EXISTS(" :: email already exists :: "),
    EMAIL_ERROR(" :: sending email failed :: "),
    EMAIL_SENT(" :: email sent to :: "),
    EMAIL_401(" :: email sent to wrong person :: "),
    TOKEN_CREATED(" :: token created in :: "),
    TOKEN_VALIDATED(" :: token validated :: "),
    TOKEN_EXPIRED(" :: token expired :: "),
    AUTHENTICATION_FAILED(" :: authentication failed :: "),
    SEARCHING_REQUEST(" :: searching request:: "),
    SEARCHING_RESPONSE(" :: searching response :: "),
    SETTING_CHANGED(" :: setting changed :: "),
    SETTINGS_GIVEN(" :: settings given to :: "),
    SETTINGS_NEW(" :: settings fulfilling for :: "),
    TAG_SAVE(" :: save tags"),
    TAG_EDIT(" :: edit tags"),
    TAG_DELETE(" :: delete tags"),
    FEED_REQUEST(" :: gettings feedsRequest :: "),
    GETLIST_NOTIFICATION(":: gettings list notifications:: "),
    UPDATE_NOTIFICATION("::update notifications:: "),
    CREATE_BIRTHDAY_NOTIFICATION("::create notifications for birthday:: "),
    GET_LIST_FRIENDS(":: get friends ::"),
    DEL_FRIEND("::delete friend :: "),
    ADD_FRIEND("::add friend ::"),
    GET_LIST_REQUEST_FRIEND(":: get list incoming requests ::"),
    GET_LIST_RECOMENDATIONS_FRIEND(":: get list recommendation ::"),
    IS_FRIENDS(":: check is friend ::"),
    BLOCKING(":: person blocking ::")
    ;
    public String description;

    LoggerValue(String description) {
        this.description = description;
    }
}
