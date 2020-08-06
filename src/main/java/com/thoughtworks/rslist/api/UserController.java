package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommonException;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.tools.LoggingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.rslist.tools.CommonMethods.getList;

@RestController
public class UserController {

    private List<RsEvent> rsList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    @Autowired
    UserRepository userRepository;

    public UserController() {
        rsList.add(new RsEvent("第一条事件", "政治", admin));
        rsList.add(new RsEvent("第二条事件", "科技", admin));
        rsList.add(new RsEvent("第三条事件", "经济", admin));
        userList = new ArrayList<>();
        userList.add(admin);
    }

    @PostMapping("/user")
     ResponseEntity addUser(@RequestBody(required = true) @Valid User user) {
        UserEntity userEntity = new UserEntity(user);
        userRepository.save(userEntity);
        for (User userExist : userList) {
            if (userExist.getUserName().equals(user.getUserName()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userList.add(user);
        return ResponseEntity.created(null).body(userList.size() - 1);
    }

    @GetMapping("/users")
    ResponseEntity<List> getUsers(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) throws InvalidIndexException, Exception {
        return getList(start, end, userList);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity userExceptionHandler(MethodArgumentNotValidException ex) {
        Logger logger = LoggerFactory.getLogger(LoggingController.class);
        logger.error("这是由于User成员没有通过检验造成的错误");
        CommonException commonException = new CommonException();
        commonException.setError("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException);
    }
}
