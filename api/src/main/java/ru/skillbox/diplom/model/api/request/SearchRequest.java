package ru.skillbox.diplom.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.Post;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @JsonProperty("text")
    private String text;

    @JsonProperty("date_from")
    private Long date_from;

    @JsonProperty("date_to")
    private Long date_to;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("author")
    private String author;

    @JsonProperty("age_from")
    private Integer age_from;

    @JsonProperty("age_to")
    private Integer age_to;

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("itemPerPage")
    private int itemPerPage = 20;

    public boolean requestIsNull(Class<?> tClass) {
        if (tClass.equals(Post.class)) {
            return isNullOrBlank(text) && isNullOrBlank(author) &&
                    Objects.isNull(date_from) && Objects.isNull(date_to);
        }
        return isNullOrBlank(first_name) && isNullOrBlank(last_name) &&
                Objects.isNull(age_from) && Objects.isNull(age_to) && Objects.isNull(city) && Objects.isNull(country);
    }

    private boolean isNullOrBlank(String value) {
        return Objects.isNull(value) || value.isBlank();
    }
}
