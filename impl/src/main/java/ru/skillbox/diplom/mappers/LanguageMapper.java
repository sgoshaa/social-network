package ru.skillbox.diplom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.model.LanguageDataDTO;
import ru.skillbox.diplom.model.api.response.LanguagesResponse;

import java.util.List;

@Mapper
public interface LanguageMapper {

    @Mapping(target = "error", ignore = true)
    @Mapping(target = "timestamp", expression = "java(new java.util.Date().getTime())")
    @Mapping(target = "total", ignore = true) // что за поле тотал?
    @Mapping(target = "offset", source = "offset")
    @Mapping(target = "perPage", source = "itemPerPage")
    @Mapping(target = "data", source = "dataDTOList")
    LanguagesResponse toLanguageResponse(Integer offset, Integer itemPerPage, List<LanguageDataDTO> dataDTOList);

}
