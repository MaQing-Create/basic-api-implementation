package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

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
        RsEvent newRsEvent = new RsEvent("第四条事件", "军事");

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/add").content(eventJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).andExpect(jsonPath("$[0].eventName", is("第一条事件"))).andExpect(jsonPath("$[0].keyWord"
                , is("政治"))).andExpect(jsonPath("$[1].eventName", is("第二条事件"))).andExpect(jsonPath("$[1].keyWord"
                , is("科技"))).andExpect(jsonPath("$[2].eventName", is("第三条事件"))).andExpect(jsonPath("$[2].keyWord"
                , is("经济"))).andExpect(jsonPath("$[3].eventName", is("第四条事件"))).andExpect(jsonPath("$[3].keyWord"
                , is("军事"))).andExpect(status().isOk());

    }

}
