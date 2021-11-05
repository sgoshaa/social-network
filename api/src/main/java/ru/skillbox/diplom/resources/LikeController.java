package ru.skillbox.diplom.resources;

import org.springframework.http.ResponseEntity;
import ru.skillbox.diplom.model.api.request.LikeRequest;

public interface LikeController<T> {

    ResponseEntity<T> getLiked(int userId, int itemId, String type);

    ResponseEntity<T> getLikes(int itemId, String type);

    ResponseEntity<T> putLike(LikeRequest request);

    ResponseEntity<T> deleteLike(int itemId, String type);
}
