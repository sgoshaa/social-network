package ru.skillbox.diplom.resources;

import org.springframework.http.ResponseEntity;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.api.request.PostRequest;
import ru.skillbox.diplom.model.api.request.ProfileRequest;
import ru.skillbox.diplom.model.api.request.SearchRequest;
import ru.skillbox.diplom.model.api.response.LoginResponse;
import ru.skillbox.diplom.model.api.response.PostResponse;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.api.response.WallResponse;

public interface UserController {

    LoginResponse getUrself();

    ResponseEntity<LoginResponse> editProfile(ProfileRequest request);

    ResponseEntity<LoginResponse> deleteProfile();

    ResponseEntity<LoginResponse> getUser(int id);

    ResponseEntity<WallResponse> getWall(int id, int offset, int itemPerPage);

    PostResponse post(int id, Long publishDate, PostRequest request);

    ResponseEntity<Response<PersonDTO>> search(SearchRequest searchRequest);

    void blockPerson(int blockingId);
}
