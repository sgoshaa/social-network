package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.api.response.LoginResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = DateConverter.class)
public interface LoginResponseMapper {

    @Mapping(target = "data", source = "data")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    LoginResponse toLoginResponse(PersonDTO data);
}
