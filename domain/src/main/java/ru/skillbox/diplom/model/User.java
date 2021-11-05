package ru.skillbox.diplom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.model.enums.ModerationType;
import ru.skillbox.diplom.model.enums.Role;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity{

    @Column(name = "e_mail", nullable = false)
    private String email;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String firstName;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Enumerated(EnumType.STRING)
    private ModerationType type;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ToString.Exclude
    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable(name = "user_settings",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "notification_type_id")}
    )
    private List<UserSetting> settings;

    public User() {
        type = ModerationType.USER;
    }

    public Role getRole() {
        return type.equals(ModerationType.USER) ? Role.ROLE_USER :
                type.equals(ModerationType.ADMIN) ? Role.ROLE_ADMIN : Role.ROLE_MODERATOR;
    }
}
