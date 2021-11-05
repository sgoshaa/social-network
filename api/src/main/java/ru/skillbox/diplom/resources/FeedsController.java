package ru.skillbox.diplom.resources;

import ru.skillbox.diplom.model.api.response.FeedsResponse;

public interface FeedsController {

    FeedsResponse getFeeds(String name, Integer offset, Integer itemPerPage);

}