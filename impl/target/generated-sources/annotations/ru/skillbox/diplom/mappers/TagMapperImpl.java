package ru.skillbox.diplom.mappers;

import javax.annotation.processing.Generated;
import ru.skillbox.diplom.model.Tag;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class TagMapperImpl extends TagMapper {

    @Override
    public Tag convert(String tagString) {
        if ( tagString == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setTag( tagString );

        return tag;
    }
}
