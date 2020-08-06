package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.entity.RsEventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEvent {

    @NotNull
    private String eventName;

    @NotNull
    private String keyWord;

//    @NotNull
//    @Valid
//    private User user;

    @NotNull
    private Integer userId;

    public RsEvent(RsEventEntity rsEventEntity){
        this.eventName = rsEventEntity.getEventName();
        this.keyWord = rsEventEntity.getKeyWord();



    }
}
