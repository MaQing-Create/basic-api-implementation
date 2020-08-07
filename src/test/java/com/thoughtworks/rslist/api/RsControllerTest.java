package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {

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

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.save(new UserEntity(admin));
        rsEventRepository.save(new RsEventEntity(event1));
        rsEventRepository.save(new RsEventEntity(event2));
        rsEventRepository.save(new RsEventEntity(event3));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldGetOneRs() throws Exception {
        mockMvc.perform(get("/rs/1")).andExpect(jsonPath("$.eventName", is("第一条事件"))).andExpect(jsonPath("$.keyWord"
                , is("政治"))).andExpect(jsonPath("$", not(hasKey("user")))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/2")).andExpect(jsonPath("$.eventName", is("第二条事件"))).andExpect(jsonPath("$.keyWord"
                , is("科技"))).andExpect(jsonPath("$", not(hasKey("user")))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/3")).andExpect(jsonPath("$.eventName", is("第三条事件"))).andExpect(jsonPath("$.keyWord"
                , is("经济"))).andExpect(jsonPath("$", not(hasKey("user")))).andExpect(status().isOk());


    }

    @Test
    void shouldGetRsBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[0]", not(hasKey("user")))).andExpect(jsonPath("$[1].eventName", is(
                "第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[1]", not(hasKey("user")))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3")).andExpect(jsonPath("$[0].eventName", is("第二条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("科技"))).andExpect(jsonPath("$[0]", not(hasKey("user")))).andExpect(jsonPath("$[1].eventName",
                is("第三条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("经济"))).andExpect(jsonPath("$[1]", not(hasKey("user")))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?end=2")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[0]", not(hasKey("user")))).andExpect(jsonPath("$[1].eventName",
                is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[1]", not(hasKey("user")))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2")).andExpect(jsonPath("$[0].eventName", is("第二条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("科技"))).andExpect(jsonPath("$[0]", not(hasKey("user")))).andExpect(jsonPath("$[1].eventName",
                is("第三条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("经济"))).andExpect(jsonPath("$[1]", not(hasKey("user")))).andExpect(status().isOk());
    }

    @Test
    void shouldAddOneRs() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
        ObjectMapper objectMapper = new ObjectMapper();

        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("4")).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[2].eventName", is("第三条事件"))).andExpect(jsonPath("$[2].keyWord"
                , is("经济"))).andExpect(jsonPath("$[3].eventName", is("第四条事件"))).andExpect(jsonPath("$[3].keyWord"
                , is("军事"))).andExpect(status().isOk());
    }

    @Test
    void shouldEditRs() throws Exception {
        String eventJson1 = "{\"keyWord\":\"Economy\"}";
        String eventJson2 = "{\"eventName\":\"第3条事件\"}";
        String eventJson3 = "{\"eventName\":\"第三条事件\", \"keyWord\":\"经济\"}";
        mockMvc.perform(post("/rs/list?index=3").content(eventJson1).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("3")).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[2].eventName", is("第三条事件"))).andExpect(jsonPath("$[2].keyWord"
                , is("Economy"))).andExpect(status().isOk());
        mockMvc.perform(post("/rs/list?index=3").content(eventJson2).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("3")).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[2].eventName", is("第3条事件"))).andExpect(jsonPath("$[2].keyWord"
                , is("Economy"))).andExpect(status().isOk());
        mockMvc.perform(post("/rs/list?index=3").content(eventJson3).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("3")).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[2].eventName", is("第三条事件"))).andExpect(jsonPath("$[2].keyWord"
                , is("经济"))).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRs() throws Exception {
        mockMvc.perform(delete("/rs/2")).andExpect(status().isOk());
        assertEquals(rsEventRepository.findAll().size(), 2);

    }

    @Test
    void shouldAddRsFailWhenEventNameIsNull() throws Exception {
        RsEvent newRsEvent = new RsEvent(null, "军事", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenKeywordIsNull() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", null, 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }


//    @Test
//    void shouldAddRsFailWhenUserIsNull() throws Exception {
//        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", null);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(newRsEvent);
//
//        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldAddRsFailWhenUserNameLengthLargerThan8() throws Exception {
//        User user = new User("userName0", 18, "male", "user@email.com", "10123456789");
//        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(newRsEvent);
//
//        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldAddRsFailWhenAgeLargerThan100() throws Exception {
//        User user = new User("userName", 101, "male", "user@email.com", "10123456789");
//        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(newRsEvent);
//
//        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldAddRsFailWhenAgeLessThan18() throws Exception {
//        User user = new User("userName", 17, "male", "user@email.com", "10123456789");
//        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(newRsEvent);
//
//        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldAddRsFailWhenEmailIsIllegal() throws Exception {
//        User user = new User("userName", 18, "male", "useremail.com", "10123456789");
//        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(newRsEvent);
//
//        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldAddRsFailWhenPhoneIsIllegal() throws Exception {
//        User user = new User("userName", 18, "male", "user@email.com", "00123456789");
//        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(newRsEvent);
//
//        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }

//    @Test
//    void shouldThrowIndexExceptionWhenInputInvalidIndex() throws Exception {
//        mockMvc.perform(get("/rs/0"))
//                .andExpect(jsonPath("$.error", is("invalid index"))).andExpect(status().isBadRequest());
//        mockMvc.perform(get("/rs/list?start=0&end=2"))
//                .andExpect(jsonPath("$.error", is("invalid request param"))).andExpect(status().isBadRequest());
//        mockMvc.perform(get("/rs/list?start=1&end=5"))
//                .andExpect(jsonPath("$.error", is("invalid request param"))).andExpect(status().isBadRequest());
//    }

    @Test
    void shouldThrowMethodArgumentNotValidExceptionWhenNotPassValidation() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", null);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error", is("invalid param"))).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddRsWhenUserNotExists() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 0);
        ObjectMapper objectMapper = new ObjectMapper();

        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        userRepository.save(new UserEntity(admin));

        mockMvc.perform(post("/rs").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        assertEquals(rsEventRepository.findAll().size(), 3);
    }

    @Test
    void canVoteRs() throws Exception {
        userRepository.save(new UserEntity(admin));
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
        rsEventRepository.save(new RsEventEntity(newRsEvent));
        String eventJson = "{\"voteNum\":5, \"userId\":1, \"voteTime\":\"" + LocalDateTime.now() + "\"}";
        mockMvc.perform(post("/rs/vote/1").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        List<VoteEntity> votes = voteRepository.findAll();
        assertEquals(1, votes.size());
        assertEquals(1, votes.get(0).getUserId());
        assertEquals(1, votes.get(0).getEventId());
    }

    @Test
    void cannotVoteRsWhenVoteIsNotEnough() throws Exception {
        userRepository.save(new UserEntity(admin));
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
        rsEventRepository.save(new RsEventEntity(newRsEvent));
        String eventJson = "{\"voteNum\":15, \"userId\":1, \"voteTime\":\"" + LocalDateTime.now() + "\"}";
        mockMvc.perform(post("/rs/vote/1").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

}