package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.RsEvent;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = new ArrayList<>();

    public RsController() {
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
        if (start == null) return rsList.subList(0, end > rsList.size() ? rsList.size() : end);
        if (end == null) return rsList.subList(start < 1 ? 0 : start - 1, rsList.size());
        return rsList.subList(start < 1 ? 0 : start - 1, end > rsList.size() ? rsList.size() : end);
    }

    @PostMapping("/rs/add")
    void addRs(@RequestBody String eventJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        rsList.add(newRsEvent);
    }

    @PostMapping("/rs/list")
    void editRs(@RequestParam(required = true) int index, @RequestBody String eventJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        checkIsInputIndexOutOfRange(index);
        if (newRsEvent.getEventName() != null)
            rsList.get(index - 1).setEventName(newRsEvent.getEventName());
        if (newRsEvent.getKeyWord() != null)
            rsList.get(index - 1).setKeyWord((newRsEvent.getKeyWord()));
    }

    @PostMapping("/rs/delete")
    void deleteRs(@RequestParam(required = true)int index) throws Exception {
        checkIsInputIndexOutOfRange(index);
        rsList.remove(index-1);
    }

    void checkIsInputIndexOutOfRange(int index) throws Exception {
        if (index < 1 || index > rsList.size()) {
            throw new Exception("Error Request Param!");
        }
    }
}
