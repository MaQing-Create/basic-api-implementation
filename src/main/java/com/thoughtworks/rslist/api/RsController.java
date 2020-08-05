package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    public RsController() {
        rsList.add(new RsEvent("第一条事件", "政治", admin));
        rsList.add(new RsEvent("第二条事件", "科技", admin));
        rsList.add(new RsEvent("第三条事件", "经济", admin));
    }

    private void checkIsInputIndexOutOfRange(int index) throws Exception {
        if (index < 1 || index > rsList.size()) {
            throw new Exception("Error Request Param!");
        }
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
    void addRs(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        rsList.add(rsEvent);
        addUser(rsEvent.getUser());
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
    void deleteRs(@RequestParam(required = true) int index) throws Exception {
        checkIsInputIndexOutOfRange(index);
        rsList.remove(index - 1);
    }

    @PostMapping("/user")
    void addUser(@RequestBody(required = true) @Valid User user){
        for (User userExist : userList){
            if (userExist.getUserName().equals(user.getUserName())) return;
        }
        userList.add(user);
    }
}
