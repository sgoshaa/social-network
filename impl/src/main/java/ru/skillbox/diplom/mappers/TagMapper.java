package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.model.Tag;

@Mapper
public abstract class TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tag", source = "tagString")
    public abstract Tag convert (String tagString);

    public String tagToString(Tag tag) {
        return tag.getTag();
    }

}
