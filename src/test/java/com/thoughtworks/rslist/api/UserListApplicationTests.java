package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserListApplicationTests {
    //@Autowired
    MockMvc mockMvc;

    private User admin = new User("admin1", 18, "male", "admin@email.com", "10123456789");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
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
}
