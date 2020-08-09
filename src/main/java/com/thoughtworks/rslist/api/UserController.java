package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommonException;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
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
import java.util.stream.Collectors;

import static com.thoughtworks.rslist.tools.CommonMethods.getList;

@RestController
public class UserController {

    private List<RsEvent> rsList;
    private List<User> userList;
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    final UserRepository userRepository;
    final RsEventRspository rsEventRepository;
    final VoteRepository voteRepository;

    public UserController(UserRepository userRepository, RsEventRspository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    @PostMapping("/user")
    ResponseEntity addUser(@RequestBody(required = true) @Valid User user) {
        UserEntity userEntity = new UserEntity(user);
        if (userRepository.existsByUserName(user.getUserName()))
            return ResponseEntity.badRequest().build();
        userRepository.save(userEntity);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{index}")
    ResponseEntity<UserEntity> getUser(@PathVariable Integer index) {
        UserEntity userEntity = userRepository.getUsersByUserId(index);
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/users")
    ResponseEntity<List> getUsers(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) throws InvalidIndexException, Exception {
        userList =
                userRepository.findAll().stream().map(userEntity -> new User(userEntity)).collect(Collectors.toList());
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

    @DeleteMapping("/user/{index}")
    ResponseEntity<UserEntity> deleteUser(@PathVariable Integer index) {
        userRepository.deleteByUserId(index);
        return ResponseEntity.ok().build();
    }
}
