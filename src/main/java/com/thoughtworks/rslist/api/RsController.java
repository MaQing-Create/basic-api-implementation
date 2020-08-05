package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.rslist.api.UserController.addUser;
import static com.thoughtworks.rslist.tools.CommonMethods.getList;

@RestController
public class RsController {
    private List<RsEvent> rsList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    public RsController() {
        rsList.add(new RsEvent("第一条事件", "政治", admin));
        rsList.add(new RsEvent("第二条事件", "科技", admin));
        rsList.add(new RsEvent("第三条事件", "经济", admin));
        userList.add(admin);
    }

    private void checkIsInputIndexOutOfRange(int index) throws Exception {
        if (index < 1 || index > rsList.size()) {
            throw new Exception("Error Request Param!");
        }
    }

    @GetMapping("/rs/{index}")
    ResponseEntity<RsEvent> getOneRs(@PathVariable int index) throws Exception {
        checkIsInputIndexOutOfRange(index);
        return ResponseEntity.ok().body(rsList.get(index - 1));
    }

    @GetMapping("/rs/list")
    ResponseEntity<List> getRsBetween(@RequestParam(required = false) Integer start,
                                      @RequestParam(required = false) Integer end) {
        return getList(start, end, rsList);
    }

    @PostMapping("/rs/add")
    ResponseEntity addRs(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        rsList.add(rsEvent);
        addUser(rsEvent.getUser());
        return ResponseEntity.created(null).body(rsList.size() - 1);
    }

    @PostMapping("/rs/list")
    ResponseEntity editRs(@RequestParam(required = true) int index, @RequestBody String eventJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        checkIsInputIndexOutOfRange(index);
        if (newRsEvent.getEventName() != null)
            rsList.get(index - 1).setEventName(newRsEvent.getEventName());
        if (newRsEvent.getKeyWord() != null)
            rsList.get(index - 1).setKeyWord((newRsEvent.getKeyWord()));
        return ResponseEntity.created(null).body(index);
    }

    @PostMapping("/rs/delete")
    ResponseEntity deleteRs(@RequestParam(required = true) int index) throws Exception {
        checkIsInputIndexOutOfRange(index);
        rsList.remove(index - 1);
        return ResponseEntity.created(null).body(index);
    }

}
