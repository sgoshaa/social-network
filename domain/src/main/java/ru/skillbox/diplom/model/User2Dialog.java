package ru.skillbox.diplom.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user2dialog")
public class User2Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dialog_id")
    private Integer dialogId;

    @Column(name = "user_id")
    private Integer userId;
}