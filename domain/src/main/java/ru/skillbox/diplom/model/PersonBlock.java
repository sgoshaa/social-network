package ru.skillbox.diplom.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.enums.ActionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "person_blocked")
@NoArgsConstructor
public class PersonBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person personId;

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    private Person blocked;

    @Column(name = "action")
    @Enumerated(value = EnumType.STRING)
    private ActionType action;

    public PersonBlock(Person personId, Person blocked, ActionType action) {
        this.personId = personId;
        this.blocked = blocked;
        this.action = action;
        time = LocalDateTime.now();
    }
}
