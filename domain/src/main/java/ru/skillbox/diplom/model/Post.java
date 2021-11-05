package ru.skillbox.diplom.model;
//post - посты
//
//        id
//        time - дата и время публикации
//        author_id
//        title - заголовок
//        post_text - HTML-текст поста
//        is_blocked - отметка о том, что пост заблокирован

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@ToString
@Table(name = "posts")
public class Post extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime time;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Person author;

    @Column(nullable = false)
    private String title;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Filter(name = "isDeleted")
    private List<PostComment> postComments;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "report",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private List<Person> personsWithReport;

    @ToString.Exclude
    @OneToMany(mappedBy = "postId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostLike> likes;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(name = "post2tag",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    @ToString.Exclude
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostFile> postFiles;

    public int getLikesAmount() {
        return likes == null ? 0 : likes.size();
    }

    public List<PostComment> getNotSubComments() {
        return getPostComments().stream().filter(c -> !c.isSub()).collect(Collectors.toList());
    }
}
