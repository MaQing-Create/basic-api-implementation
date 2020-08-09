package com.thoughtworks.rslist.domain;

import com.thoughtworks.rslist.entity.VoteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    private Long voteTime;
    private Integer userId;
    private Integer eventId;
    private Integer voteNum;

    public Vote(VoteEntity voteEntity){
        this.userId = voteEntity.getUserId();
        this.eventId = voteEntity.getEventId();
        this.voteNum = voteEntity.getVoteNum();
        this.voteTime = voteEntity.getVoteTime();
    }
}
