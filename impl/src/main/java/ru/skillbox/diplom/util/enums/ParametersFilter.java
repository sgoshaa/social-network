package ru.skillbox.diplom.util.enums;

public enum ParametersFilter {
    IS_DELETED ("isDeleted", "is_deleted");

    private final String name;
    private final String nameParameter;

    ParametersFilter(String name, String nameParameter) {
        this.name = name;
        this.nameParameter = nameParameter;
    }

    public String getName() {
        return name;
    }

    public String getNameParameter() {
        return nameParameter;
    }
}
