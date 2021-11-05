package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.CommentDTO;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.PostComment;


@Mapper(uses = {PostMapper.class, DateConverter.class, PersonDTO.class})
public interface CommentMapper {

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "time", source = "comment.time", qualifiedByName = "convertRegDate")
    @Mapping(target = "author", source = "person")
    @Mapping(target = "parentId", source = "parentId.id")
    @Mapping(target = "likes", expression = "java(comment.getLikesAmount())")
    CommentDTO convert(PostComment comment);
}