package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.model.api.response.StorageResponse;
import ru.skillbox.diplom.resources.StorageController;
import ru.skillbox.diplom.service.StorageService;

@RestController
@RequestMapping("/api/v1/storage")
public class ApiStorageController implements StorageController {

    private final StorageService storageService;

    @Autowired
    public ApiStorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    @PostMapping(value = "", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<StorageResponse> upload(@RequestParam("type") String type, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(storageService.upload(type, file));
    }
}
