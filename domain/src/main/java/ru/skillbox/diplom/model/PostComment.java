package ru.skillbox.diplom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

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

@Entity
@Getter
@Setter
@ToString
@Table(name = "post_comment")
@FilterDef(name = "isDeleted", parameters = @ParamDef(name = "is_deleted", type = "boolean"),
        defaultCondition = "NOT is_deleted")
public class PostComment extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "time")
    private LocalDateTime time;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "comment_report",
            joinColumns = {@JoinColumn(name = "post_comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private List<Person> personsWithReport;

    @ToString.Exclude
    @JoinColumn(name = "parent_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PostComment parentId;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    @Filter(name = "isDeleted")
    private List<PostComment> subComments;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_sub")
    private boolean isSub;

    @Column(name = "comment_text")
    private String commentText;

    @ToString.Exclude
    @OneToMany(mappedBy = "postCommentId", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private List<CommentLike> likes;

    public int getLikesAmount() {
        return likes == null ? 0 : likes.size();
    }


}




