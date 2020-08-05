package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {
    //@Autowired
    MockMvc mockMvc;

    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldGetOneRs() throws Exception {
        mockMvc.perform(get("/rs/1")).andExpect(jsonPath("$.eventName", is("第一条事件"))).andExpect(jsonPath("$.keyWord"
                , is("政治"))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/2")).andExpect(jsonPath("$.eventName", is("第二条事件"))).andExpect(jsonPath("$.keyWord"
                , is("科技"))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/3")).andExpect(jsonPath("$.eventName", is("第三条事件"))).andExpect(jsonPath("$.keyWord"
                , is("经济"))).andExpect(status().isOk());
    }

    @Test
    void shouldGetRsBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3")).andExpect(jsonPath("$[0].eventName", is("第二条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("科技"))).andExpect(jsonPath("$[1].eventName", is("第三条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("经济"))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?end=2")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2")).andExpect(jsonPath("$[0].eventName", is("第二条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("科技"))).andExpect(jsonPath("$[1].eventName", is("第三条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("经济"))).andExpect(status().isOk());
    }

    @Test
    void shouldAddOneRs() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", admin);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("3")).andExpect(status().isCreated());
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
        mockMvc.perform(post("/rs/delete?index=1")).andExpect(content().string("1")).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$[0].eventName", is("第二条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("科技"))).andExpect(jsonPath("$[1].eventName", is("第三条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("经济"))).andExpect(status().isOk());

    }

    @Test
    void shouldAddUser() throws Exception {
        User user = new User("userName", 18, "male","user@email.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("1")).andExpect(status().isCreated());
    }

    @Test
    void shouldAddUserFailWhenUserNameLengthLargerThan8() throws Exception {
        User user = new User("userName0", 18, "male","user@email.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenAgeLargerThan100() throws Exception {
        User user = new User("userName", 101, "male","user@email.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenAgeLessThan18() throws Exception {
        User user = new User("userName", 17, "male","user@email.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenEmailIsIllegal() throws Exception {
        User user = new User("userName", 18, "male","useremail.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserFailWhenPhoneIsIllegal() throws Exception {
        User user = new User("userName", 18, "male","useremail.com","00123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenEventNameIsNull() throws Exception {
        RsEvent newRsEvent = new RsEvent(null, "军事", admin);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenKeywordIsNull() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", null, admin);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }


    @Test
    void shouldAddRsFailWhenUserIsNull() throws Exception {
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", null);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenUserNameLengthLargerThan8() throws Exception {
        User user = new User("userName0", 18, "male","user@email.com","10123456789");
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", user);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenAgeLargerThan100() throws Exception {
        User user = new User("userName", 101, "male","user@email.com","10123456789");
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", user);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenAgeLessThan18() throws Exception {
        User user = new User("userName", 17, "male","user@email.com","10123456789");
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", user);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenEmailIsIllegal() throws Exception {
        User user = new User("userName", 18, "male","useremail.com","10123456789");
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", user);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddRsFailWhenPhoneIsIllegal() throws Exception {
        User user = new User("userName", 18, "male","user@email.com","00123456789");
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事", user);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
