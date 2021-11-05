package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.PostRequest;

import java.time.LocalDateTime;

@Mapper(uses = {DateConverter.class, CommentMapper.class, PersonMapper.class,TagMapper.class})

public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "time", source = "post.time", qualifiedByName = "convertRegDate")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "postText", source = "post.postText")
    @Mapping(target = "blocked", expression = "java(post.isBlocked())")
    @Mapping(target = "likes", expression = "java(post.getLikesAmount())")
    @Mapping(target = "author", source = "personDTO")
    PostDTO convert(Post post, PersonDTO personDTO);

    @Mapping(target = "likes", expression = "java(post.getLikesAmount())")
    @Mapping(target = "time", source = "post.time", qualifiedByName = "convertRegDate")
    PostDTO convert(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", source = "localDateTime")
    @Mapping(target = "author", source = "person")
    @Mapping(target = "title", source = "request.title")
    @Mapping(target = "postText", source = "request.postText")
    @Mapping(target = "tags", ignore = true)
    Post convert(PostRequest request, User person, LocalDateTime localDateTime);
}

