package ru.skillbox.diplom.mappers.converter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Component
public class DateConverter {

    @Named("convertRegDate")
    public long convertRegDate(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return 0;
        }

        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public LocalDateTime longToDate(Long dateLong) {
        return null == dateLong ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.systemDefault());
    }

    public LocalDateTime ageToBirthDate(Integer age) {
        return null == age ? null : LocalDateTime.now().minusYears(age);
    }
}
