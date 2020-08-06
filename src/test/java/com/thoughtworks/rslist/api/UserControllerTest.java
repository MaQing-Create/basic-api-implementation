package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
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
    void shouldAddUserFailWhenAgeLargerThan100() throws Exception {
        User user = new User("userName", 101, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenAgeLessThan18() throws Exception {
        User user = new User("userName", 17, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenUserNameLengthLargerThan8() throws Exception {
        User user = new User("userName0", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUser() throws Exception {
        User user = new User("userName", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        assertEquals("userName", userRepository.getUsersByUserId(5).getUserName());
    }

    @Test
    void shouldNotAddUserWhenUserExists() throws Exception {
        User user = new User("admin", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenPhoneIsIllegal() throws Exception {
        User user = new User("userName", 18, "male", "useremail.com", "00123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenEmailIsIllegal() throws Exception {
        User user = new User("userName", 18, "male", "useremail.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }


//    @Test
//    void shouldGetAllUser() throws Exception {
//        List<User> userList = new ArrayList<>();
//        User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");
//        userList.add(admin);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userListString = objectMapper.writeValueAsString(userList);
//        mockMvc.perform(get("/users")).andExpect(content().string(userListString)).andExpect(status().isOk());
//
//    }

    @Test
    void shouldThrowMethodArgumentNotValidExceptionWhenNotPassValidation() throws Exception {
        User user = new User("userName", 18, "male", "useremail.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error",is("invalid user"))).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserIntoDatabase() throws Exception {
        User user = new User("userName", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        List<UserEntity> userListFromDatabase = userRepository.findAll();
        assertEquals(userListFromDatabase.get(1).getUserName(), "userName");
        assertEquals(userListFromDatabase.get(1).getAge(), 18);
        assertEquals(userListFromDatabase.get(1).getGender(), "male");
        assertEquals(userListFromDatabase.get(1).getEmail(), "user@email.com");
        assertEquals(userListFromDatabase.get(1).getPhone(), "10123456789");
    }

    @Test
    void shouldGetUser() throws Exception {
        User user = new User("userName", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        mockMvc.perform(get("/user/5")).andExpect(jsonPath("$.userName",is("userName"))).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        User user = new User("userName", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        assertEquals(userRepository.findAll().size(), 2);
        mockMvc.perform(delete("/user/5")).andExpect(status().isOk());
        assertEquals(userRepository.findAll().size(), 1);
    }

    @Test
    void shouldDeleteRsWhenDeleteUser(){
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", 1);
        rsEventRepository.save(new RsEventEntity(newRsEvent));
        assertEquals(1,userRepository.findAll().size());
        assertEquals(4,rsEventRepository.findAll().size());
        userRepository.deleteByUserId(1);
        assertEquals(0,userRepository.findAll().size());
        assertEquals(0,rsEventRepository.findAll().size());
    }

}
