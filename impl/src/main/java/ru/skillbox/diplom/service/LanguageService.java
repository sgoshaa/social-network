package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.mappers.LanguageMapper;
import ru.skillbox.diplom.model.LanguageDataDTO;
import ru.skillbox.diplom.model.api.response.LanguagesResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService {

    private final LanguageMapper languageMapper = Mappers.getMapper(LanguageMapper.class);

    @Cacheable("language")
    public LanguagesResponse language(String language, int offset, int itemPerPage) {

        LanguageDataDTO languageDataDTO = new LanguageDataDTO();
        List<LanguageDataDTO> dataDTOList = new ArrayList<>();

        languageDataDTO.setId(1);
        languageDataDTO.setTitle(language);

        dataDTOList.add(languageDataDTO);

        LanguagesResponse languagesResponse = languageMapper.toLanguageResponse(offset, itemPerPage, dataDTOList);

        return languagesResponse;
    }
}