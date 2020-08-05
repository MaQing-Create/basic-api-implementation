package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.rslist.tools.CommonMethods.getList;

@RestController
public class UserController {

    private List<RsEvent> rsList = new ArrayList<>();
    private static List<User> userList = new ArrayList<>();
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    public UserController() {
        rsList.add(new RsEvent("第一条事件", "政治", admin));
        rsList.add(new RsEvent("第二条事件", "科技", admin));
        rsList.add(new RsEvent("第三条事件", "经济", admin));
        userList = new ArrayList<>();
        userList.add(admin);
    }

    @PostMapping("/user")
    static ResponseEntity addUser(@RequestBody(required = true) @Valid User user) {
        for (User userExist : userList) {
            if (userExist.getUserName().equals(user.getUserName()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userList.add(user);
        return ResponseEntity.created(null).body(userList.size() - 1);
    }

    @GetMapping("/users")
    ResponseEntity<List> getUsers(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {
        return getList(start, end, userList);
    }
}
