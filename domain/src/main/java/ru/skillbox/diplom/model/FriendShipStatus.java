package ru.skillbox.diplom.model;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.model.enums.FriendshipCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "friendship_statuses")
public class FriendShipStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime time;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipCode code;

}