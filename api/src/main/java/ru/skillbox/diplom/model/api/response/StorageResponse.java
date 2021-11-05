package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageResponse<K, V> {

    @JsonProperty("error")
    private Errors error;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("data")
    private Map<K, V> data;

}
