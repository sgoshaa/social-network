package ru.skillbox.diplom.model;

//friendship - дружба
//
//        id
//        status_id - статус связи (см. ниже)
//        src_person_id - пользователь, запросивший дружбу
//        dst_person_id - пользователь, получивший запрос

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "friendship")
@EqualsAndHashCode
public class FriendShip extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private FriendShipStatus status;

    @Column(name = "src_person_id")
    private Integer srcPersonId;

    @Column(name = "dst_person_id")
    private Integer dstPersonId;

}
