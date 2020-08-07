package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.RsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rsEvent")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RsEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    private String eventName;
    private String keyWord;
    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    public RsEventEntity(RsEvent rsEvent){
        this.eventName = rsEvent.getEventName();
        this.keyWord = rsEvent.getKeyWord();
        this.userId = rsEvent.getUserId();
    }
}
