package ru.skillbox.diplom.resources;

import ru.skillbox.diplom.model.api.response.LanguagesResponse;

public interface PlatformController {

    LanguagesResponse language(int offset, int itemPerPage, String language);
}
