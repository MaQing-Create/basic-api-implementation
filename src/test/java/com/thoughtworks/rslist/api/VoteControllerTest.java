package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRspository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");
    private RsEvent event1 = new RsEvent("第一条事件", "政治", 1);
    private RsEvent event2 = new RsEvent("第二条事件", "科技", 1);
    private RsEvent event3 = new RsEvent("第三条事件", "经济", 1);
    private int userId;


    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        UserEntity thisUserEntity = userRepository.save(new UserEntity(admin));
        userId = thisUserEntity.getUserId();
        event1.setUserId(userId);
        event2.setUserId(userId);
        event3.setUserId(userId);
        rsEventRepository.save(new RsEventEntity(event1));
        rsEventRepository.save(new RsEventEntity(event2));
        rsEventRepository.save(new RsEventEntity(event3));
    }

    @Test
    void shouldReturnAllVotesBewteen() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        Long voteTime1 = Long.parseLong(df.format(LocalDateTime.now()));
        String eventJson = "{\"voteNum\":1, \"userId\":1, \"voteTime\":" + voteTime1 + "}";
        mockMvc.perform(post("/rs/vote/1").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        Long voteTime2 = Long.parseLong(df.format(LocalDateTime.now()));
        eventJson = "{\"voteNum\":2, \"userId\":1, \"voteTime\":" + voteTime2 + "}";
        mockMvc.perform(post("/rs/vote/1").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        Long voteTime3 = Long.parseLong(df.format(LocalDateTime.now()));
        eventJson = "{\"voteNum\":3, \"userId\":1, \"voteTime\":" + voteTime3 + "}";
        mockMvc.perform(post("/rs/vote/1").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/votes?timeStart=" + voteTime1 + "&timeEnd=" + voteTime2)).andExpect(jsonPath("$",
                hasSize(2))).andExpect(status().isOk());
    }
}

