package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.BitSet;
import java.util.List;


@Repository
public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();

    List<VoteEntity> findByEventId(Integer eventId);

    VoteEntity getVoteByEventId(Integer eventId);

//    @Query("select v from VoteEntity v where v.voteTime between :timeStart and :timeEnd")
//    List<VoteEntity> getVotesBewteenTime(LocalDateTime timeStart, LocalDateTime timeEnd);
    @Query(value = "select * from vote where vote_time between :timeStart and :timeEnd", nativeQuery = true)
    List<VoteEntity> getVotesBewteenTime(@Param("timeStart")LocalDateTime timeStart, @Param("timeEnd")LocalDateTime timeEnd);
}