package ru.skillbox.diplom.repository.specification.enums;

public enum FieldName {
    ID("id"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    BIRTHDATE("birthDate"),
    TOWN("town"),
    AUTHOR("author"),
    TIME("time"),
    TITLE("title"),
    POST_TEXT("postText"),
    IS_DELETED("isDeleted"),
    DST_PERSON("dstPersonId"),
    SRC_PERSON("srcPersonId");

    private final String value;

    FieldName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
