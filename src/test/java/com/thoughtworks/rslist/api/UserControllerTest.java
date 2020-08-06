package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
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

    private User admin = new User("admin1", 18, "male", "admin@email.com", "10123456789");

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
//        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
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
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("1")).andExpect(status().isCreated());
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


    @Test
    void shouldGetAllUser() throws Exception {
        List<User> userList = new ArrayList<>();
        User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");
        userList.add(admin);
        ObjectMapper objectMapper = new ObjectMapper();
        String userListString = objectMapper.writeValueAsString(userList);
        mockMvc.perform(get("/users")).andExpect(content().string(userListString)).andExpect(status().isOk());
    }

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
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("1")).andExpect(status().isCreated());
        List<UserEntity> userListFromDatabase = userRepository.findAll();
        assertEquals(userListFromDatabase.get(0).getUserName(), "userName");
        assertEquals(userListFromDatabase.get(0).getAge(), 18);
        assertEquals(userListFromDatabase.get(0).getGender(), "male");
        assertEquals(userListFromDatabase.get(0).getEmail(), "user@email.com");
        assertEquals(userListFromDatabase.get(0).getPhone(), "10123456789");
    }

    @Test
    void shouldGetUser() throws Exception {
        User user = new User("userName", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("1")).andExpect(status().isCreated());
        mockMvc.perform(get("/user/1")).andExpect(jsonPath("$.userName",is("userName"))).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {

        User user = new User("userName", 18, "male", "user@email.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("1")).andExpect(status().isCreated());
        assertEquals(userRepository.findAll().size(), 1);
        mockMvc.perform(delete("/user/1")).andExpect(status().isOk());
        assertEquals(userRepository.findAll().size(), 0);
    }
}
