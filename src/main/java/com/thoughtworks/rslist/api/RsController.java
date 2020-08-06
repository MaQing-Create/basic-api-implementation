package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.CommonException;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRspository;
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

import static com.thoughtworks.rslist.tools.CommonMethods.checkIsInputIndexOutOfRange;
import static com.thoughtworks.rslist.tools.CommonMethods.getList;

@RestController
public class RsController {
    private List<RsEvent> rsList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRspository rsEventRepository;

    public RsController() {
        rsList.add(new RsEvent("第一条事件", "政治", 1));
        rsList.add(new RsEvent("第二条事件", "科技", 1));
        rsList.add(new RsEvent("第三条事件", "经济", 1));
        userList.add(admin);
    }

//    private void checkIsInputIndexOutOfRange(int index) throws Exception, InvalidIndexException {
//        if (index < 1 || index > rsList.size()) {
//            throw new InvalidIndexException("Error Request Param!");
//        }
//    }

    @GetMapping("/rs/{index}")
    ResponseEntity<RsEvent> getOneRs(@PathVariable int index) throws Exception, InvalidIndexException {
        checkIsInputIndexOutOfRange(index, rsList, "invalid index");
        return ResponseEntity.ok().body(rsList.get(index - 1));
    }

    @GetMapping("/rs/list")
    ResponseEntity<List> getRsBetween(@RequestParam(required = false) Integer start,
                                      @RequestParam(required = false) Integer end) throws InvalidIndexException, Exception {
        return getList(start, end, rsList);
    }

    @PostMapping("/rs")
    ResponseEntity addRs(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        if (!userRepository.existsByUserId(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        rsEventRepository.save(new RsEventEntity(rsEvent));
        rsList.add(rsEvent);
        return ResponseEntity.created(null).body(rsList.size() - 1);
    }

    @PostMapping("/rs/list")
    ResponseEntity editRs(@RequestParam(required = true) int index, @RequestBody String eventJson) throws Exception, InvalidIndexException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        checkIsInputIndexOutOfRange(index, rsList,"invalid request param");
        if (newRsEvent.getEventName() != null)
            rsList.get(index - 1).setEventName(newRsEvent.getEventName());
        if (newRsEvent.getKeyWord() != null)
            rsList.get(index - 1).setKeyWord((newRsEvent.getKeyWord()));
        return ResponseEntity.created(null).body(index);
    }

    @DeleteMapping("/rs/{index}")
    ResponseEntity deleteRs(@PathVariable int index) throws Exception, InvalidIndexException {
        checkIsInputIndexOutOfRange(index, rsList,"invalid index");
        rsList.remove(index - 1);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity userExceptionHandler(MethodArgumentNotValidException ex) {
        Logger logger = LoggerFactory.getLogger(LoggingController.class);
        logger.error("这是由于RsEvent成员没有通过检验造成的错误");
        CommonException commonException = new CommonException();
        commonException.setError("invalid param");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException);
    }

//    @ExceptionHandler(InvalidIndexException.class)
//    ResponseEntity exceptionHandler(InvalidIndexException ex){
//        CommonException commonException = new CommonException();
//        commonException.setError(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException);
//    }
}
