package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.enums.Errors;
import ru.skillbox.diplom.model.api.request.PostRequest;
import ru.skillbox.diplom.model.api.request.ProfileRequest;
import ru.skillbox.diplom.model.api.request.SearchRequest;
import ru.skillbox.diplom.model.api.response.LoginResponse;
import ru.skillbox.diplom.model.api.response.PostResponse;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.api.response.WallResponse;
import ru.skillbox.diplom.model.enums.Role;
import ru.skillbox.diplom.resources.UserController;
import ru.skillbox.diplom.service.AccountService;
import ru.skillbox.diplom.service.PersonBlockService;
import ru.skillbox.diplom.service.PostService;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerImpl implements UserController {

    private final AccountService accountService;
    private final PostService postService;
    private final PersonBlockService personBlockService;

    @Autowired
    public UserControllerImpl(AccountService accountService,
                              PostService postService,
                              PersonBlockService personBlockService
                              ) {
        this.accountService = accountService;
        this.postService = postService;
        this.personBlockService = personBlockService;
    }

    @Override
    @GetMapping("/me")
    public LoginResponse getUrself() {
        return accountService.getMe();
    }

    @Override
    @PutMapping("/me")
    @ResponseBody
    public ResponseEntity<LoginResponse> editProfile(@RequestBody ProfileRequest request) {
        if (!accountService.editProfile(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse().denied());
        }
        return ResponseEntity.ok(getUrself());
    }

    @Override
    @DeleteMapping("/me")
    @ResponseBody
    public ResponseEntity<LoginResponse> deleteProfile() {
        accountService.deleteProfile();
        return null;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<LoginResponse> getUser(@PathVariable("id") int id) {
        LoginResponse response = accountService.getPersonById(id);
        if (response.getError() == Errors.INVALID_REQUEST) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/{id}/wall")
    public ResponseEntity<WallResponse> getWall(
            @PathVariable("id") int id,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage", required = false, defaultValue = "20") int itemPerPage) {
        Person person = accountService.getPerson(id);
        return postService.getWallWithPagination(person, offset, itemPerPage);
    }

    @Override
    @PostMapping("/{id}/wall{publish_date}")
    @ResponseBody
    public PostResponse post(@PathVariable("id") int id,
                             @RequestParam(value = "publish_date", required = false) Long date,
                             @RequestBody PostRequest request) {
        User user = accountService.getUser(id);
        if (user.getRole() == Role.ROLE_USER) {
            Person person = accountService.getPerson(user.getId());
            return postService.post(request, person, date);
        }
        return new PostResponse().unauthorised(); //moder logic
    }

    @Override
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Response<PersonDTO>> search(SearchRequest searchRequest) {
        return accountService.searchPerson(searchRequest);
    }

    @Override
    @PutMapping("/block/{id}")
    public void blockPerson(@PathVariable(value = "id") int blockingId){
        personBlockService.personBlocking(accountService.findByCurrentEmail(), accountService.getPerson(blockingId));
    }
}
