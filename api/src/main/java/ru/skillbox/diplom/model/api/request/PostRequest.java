package ru.skillbox.diplom.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostRequest {

    private String title;

    @JsonProperty("post_text")
    private String postText;

    private List<String> tags;
}
