package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {

    private List<RsEvent> rsList;
    private List<User> userList;
    private List<Vote> voteList;

    private List<RsEventEntity> rsEventEntityList;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRspository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/votes")
    ResponseEntity<List> getVotesBewteenTime(@RequestParam String start, @RequestParam String end) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime timeStart = LocalDateTime.parse(start, df);
        LocalDateTime timeEnd = LocalDateTime.parse(end, df);
        List<VoteEntity> lits1 = voteRepository.getVotesBewteenTime(timeStart, timeEnd);
        return ResponseEntity.ok(voteRepository.getVotesBewteenTime(timeStart, timeEnd).stream().map(voteEntity -> new Vote(voteEntity)).collect(Collectors.toList()));
    }

}
