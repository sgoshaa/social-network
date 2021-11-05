package ru.skillbox.diplom.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.model.api.response.StorageResponse;

public interface StorageController {

    ResponseEntity<StorageResponse> upload(String type, MultipartFile file);
}
