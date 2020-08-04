package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = new ArrayList<>();

    RsController() {
        rsList.add(new RsEvent("第一条事件", "政治"));
        rsList.add(new RsEvent("第二条事件", "科技"));
        rsList.add(new RsEvent("第三条事件", "经济"));
    }

    @GetMapping("/rs/{index}")
    RsEvent getOneRs(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("/rs/list")
    List<RsEvent> getRsBetween(@RequestParam(required = false) Integer start,
                               @RequestParam(required = false) Integer end) {
        if (start == null && end == null) return rsList;
        if (start == null) return rsList.subList(0, end);
        if (end == null) return rsList.subList(start - 1, rsList.size());
        return rsList.subList(start - 1, end);
    }

    @PostMapping("/rs/add")
    void addRs(@RequestBody String eventJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        rsList.add(newRsEvent);
    }
}
