package ru.skillbox.diplom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.enums.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;


//notification - оповещения
//
//        id
//        type_id - тип оповещения
//        sent_time - время отправки
//        person_id - кому отправлено
//        entity_id - идентификатор сущности, относительно которой отправлено оповещение (комментарий, друг, пост или сообщение)
//        contact - куда отправлено оповещение (конкретный e-mail или телефон)


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_id")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @Column(name = "person_id")
    private Integer personId;

    @ManyToOne
    @JoinColumn(name = "entity_id")
    private BaseEntity entity;

    private String contact;

    private Boolean delivered;
}
