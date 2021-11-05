package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.api.response.FeedsResponse;
import ru.skillbox.diplom.resources.FeedsController;
import ru.skillbox.diplom.service.FeedsService;

@RestController
@RequestMapping("api/v1/feeds")
public class FeedsControllerIml implements FeedsController {
    @Autowired
    private FeedsService feedsService;

    public FeedsControllerIml(FeedsService feedsService) {
        this.feedsService = feedsService;
    }

    @Override
    @GetMapping("")
    public FeedsResponse getFeeds(@RequestParam(value = "name", defaultValue = "") String name,
                                  @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                  @RequestParam(value = "itemPerPage", defaultValue = "10") Integer itemPerPage) {
        return feedsService.feeds(name, offset, itemPerPage);

    }
}
