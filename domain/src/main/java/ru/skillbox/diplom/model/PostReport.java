package ru.skillbox.diplom.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Data
@IdClass(PostReportKey.class)
@Table(name = "report")
public class PostReport {

    @Column(name = "time")
    private LocalDateTime time;

    @Id
    @Column(name = "post_id")
    private Integer postId;

    @Id
    @Column(name = "person_id")//author_id
    private Integer personId;

}

@Getter
@Setter
class PostReportKey implements Serializable {
    private Integer postId;
    private Integer personId;
}


