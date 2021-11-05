package ru.skillbox.diplom.model.api.response.friendship;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.model.IsFriendsDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class IsFriendsResponse {
    List<IsFriendsDTO> data;
}
