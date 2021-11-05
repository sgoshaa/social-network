package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.RegisterRequest;

@Mapper
public interface UserMapper {

    @Mapping(target = "firstName", source = "registerRequest.firstName")
    @Mapping(target = "password", source = "encryptedPassword")
    @Mapping(target = "email", source = "registerRequest.email")
    @Mapping(target = "type", expression = "java(ru.skillbox.diplom.model.enums.ModerationType.USER)")
    User toUserEntity(RegisterRequest registerRequest, String encryptedPassword);

}
