package ru.skillbox.diplom.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(CommentReportKey.class)
@Table(name = "comment_report")
public class CommentReport {

    @Column(name = "time")
    private LocalDateTime time;

    @Id
    @Column(name = "comment_id")
    private Integer commentId;

    @Id
    @Column(name = "person_id")//author_id
    private Integer personId;

}

@Getter
@Setter
class CommentReportKey implements Serializable {
    private Integer commentId;
    private Integer personId;
}

