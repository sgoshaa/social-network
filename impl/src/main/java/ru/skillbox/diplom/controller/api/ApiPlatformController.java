package ru.skillbox.diplom.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.api.response.LanguagesResponse;
import ru.skillbox.diplom.resources.PlatformController;
import ru.skillbox.diplom.service.LanguageService;

@RestController
@RequestMapping("/api/v1/platform")
public class ApiPlatformController implements PlatformController {

    private final LanguageService languageService;

    public ApiPlatformController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Override
    @GetMapping("/languages")
    public LanguagesResponse language(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") int itemPerPage,
            @RequestParam(value = "language", defaultValue = "Русский") String language) {
        return languageService.language(language, offset, itemPerPage);
    }
}
