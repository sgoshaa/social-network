package ru.skillbox.diplom.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.skillbox.diplom.model.enums.ReadStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message extends BaseEntity {


    @Column(nullable = false)
    private Date time;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Person authorId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Person recipientId;

    @Column(name = "message_text", nullable = false)
    @Type(type = "text")
    private String messageText;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", nullable = false)
    private ReadStatus readStatus;

    @Column(name = "dialog_id")
    private Integer dialogId;
}
