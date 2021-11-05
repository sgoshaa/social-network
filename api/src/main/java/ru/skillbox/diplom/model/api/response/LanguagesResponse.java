package ru.skillbox.diplom.model.api.response;

import lombok.Data;
import ru.skillbox.diplom.model.LanguageDataDTO;

import java.util.List;

@Data
public class LanguagesResponse {

    private String error;
    private Long timestamp;
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private List<LanguageDataDTO> data;
}