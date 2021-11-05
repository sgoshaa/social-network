package ru.skillbox.diplom.util;

import java.util.Objects;

public class UtilSplit {

    public static String[] split(String string) {
        return Objects.isNull(string) ? new String[0] : string.trim().split("[\\s,.;:!?()]+");
    }
}
