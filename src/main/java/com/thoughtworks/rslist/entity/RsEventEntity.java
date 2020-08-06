package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.RsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RsEventEntity {

    @Id
    @GeneratedValue
    private Integer eventId;
    private String eventName;
    private String keyWord;
    private Integer userId;

    public RsEventEntity(RsEvent rsEvent){
        this.eventName = rsEvent.getEventName();
        this.keyWord = rsEvent.getKeyWord();
        this.userId = rsEvent.getUserId();
    }
}
