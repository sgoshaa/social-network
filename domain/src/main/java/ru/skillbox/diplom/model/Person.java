package ru.skillbox.diplom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.model.enums.MessagesPermission;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "persons")
public class Person extends User {

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "photo")
    private String photo;

    @Column(name = "about")
    private String about;

    private String town;

    @Column(name = "confirmation_code", nullable = false)
    private String confirmationCode;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @ToString.Exclude
    @Column(name = "message_permission", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MessagesPermission messagesPermission;

    @Column(name = "last_online_time", nullable = false)
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @ToString.Exclude
    @OneToMany(mappedBy = "authorId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messagesByAuthor;

    @ToString.Exclude
    @OneToMany(mappedBy = "recipientId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;

    @ToString.Exclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "report",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private List<Post> postsWithReport;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "comment_report",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_comment_id")})
    private List<PostComment> commentsWithReport;

    @ToString.Exclude
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostComment> postsComment;

    @ToString.Exclude
    @OneToMany(mappedBy = "personId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostLike> likes;

    @ToString.Exclude
    @OneToMany(mappedBy = "personId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes;

    public Person approve() {
        this.setIsApproved(true);
        this.setConfirmationCode("");
        return this;
    }

    public Person changePass(Person person, String pass) {
        person.setConfirmationCode("");
        person.setPassword(pass);
        return this;
    }

    public Person takeConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
        return this;
    }
}
