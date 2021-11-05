package ru.skillbox.diplom.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "message_id")
    private Integer messageId;
}
