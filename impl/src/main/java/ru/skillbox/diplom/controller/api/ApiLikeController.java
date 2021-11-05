package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.api.request.LikeRequest;
import ru.skillbox.diplom.model.api.response.LikeResponse;
import ru.skillbox.diplom.resources.LikeController;
import ru.skillbox.diplom.service.LikeService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class ApiLikeController<T> implements LikeController {

    private final LikeService likeService;

    @Autowired
    public ApiLikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Override
    @GetMapping("/liked")
    public ResponseEntity<LikeResponse> getLiked(
            @RequestParam(value = "user_id", required = false) int userId,
            @RequestParam(value = "item_id") int itemId,
            @RequestParam(value = "type") String type) {
        LikeResponse response = likeService.liked(userId, itemId, type);
        return getLikeResponseEntity(response);
    }

    @Override
    @GetMapping("/likes")
    public ResponseEntity<LikeResponse> getLikes(
            @RequestParam(value = "item_id") int itemId,
            @RequestParam(value = "type") String type) {
        LikeResponse response = likeService.getLikes(itemId, type);
        return getLikeResponseEntity(response);
    }

    @Override
    @PutMapping("/likes")
    public ResponseEntity<LikeResponse> putLike(
            @RequestBody LikeRequest request) {
        LikeResponse response = likeService.putLike(request);
        return getLikeResponseEntity(response);
    }

    @Override
    @DeleteMapping("/likes")
    public ResponseEntity<LikeResponse> deleteLike(
            @RequestParam(value = "item_id") int itemId,
            @RequestParam(value = "type") String type) {
        LikeResponse response = likeService.deleteLike(itemId, type);
        return getLikeResponseEntity(response);
    }

    private ResponseEntity<LikeResponse> getLikeResponseEntity(LikeResponse response) {
        String e = response.getErrorDescription();
        if (e != null && e.equals("Bad Request")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (e != null && e.equals("unauthorized")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
