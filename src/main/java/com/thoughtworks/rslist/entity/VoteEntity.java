package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "vote")
public class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;
    private Long voteTime;
    private Integer userId;
    private Integer eventId;
    private Integer voteNum;

    public VoteEntity(Vote vote){
        this.voteNum = vote.getVoteNum();
        this.voteTime = vote.getVoteTime();
        this.userId = vote.getUserId();
        this.eventId = vote.getEventId();
    }
}
