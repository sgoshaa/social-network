package ru.skillbox.diplom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.enums.Type;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSettingDTO {

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Boolean enable;

    public static NotificationSettingDTO fromSetting(UserSetting setting){
        return new NotificationSettingDTO(setting.getType().getName(), setting.getType().getCode(), setting.isValue());
    }
}
