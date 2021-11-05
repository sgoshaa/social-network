package ru.skillbox.diplom.util;

import ru.skillbox.diplom.model.Person;

public class ToEmpty {

    public static void toPerson(Person person) {
        person.setBirthDate(null);
        person.setLastName(person.getLastName().concat(" (УДАЛЕН)"));
        person.setEmail("socialNetwork");
        person.setIsApproved(false);
        person.setIsDeleted(true);
        person.setPhone(null);
        person.setPhoto("https://res.cloudinary.com/dpwfgek2k/image/upload/v1631382668/depositphotos_26979665-stock-illustration-pirate-vector-flag-jolly-roger_isccp9.jpg");
        person.setAbout(null);
    }
}
