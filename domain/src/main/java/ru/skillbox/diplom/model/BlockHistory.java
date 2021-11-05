package ru.skillbox.diplom.model;

import lombok.Data;
import ru.skillbox.diplom.model.enums.ActionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "block_histories")
public class BlockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "action")
    @Enumerated(value = EnumType.STRING)
    private ActionType action;

}
