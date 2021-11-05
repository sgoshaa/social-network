package ru.skillbox.diplom.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.enums.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification_types")
@Getter
@NoArgsConstructor
public class NotificationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Type code;

    private String name;
}
