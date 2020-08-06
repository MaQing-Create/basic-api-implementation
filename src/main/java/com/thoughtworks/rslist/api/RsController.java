package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
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

import static com.thoughtworks.rslist.tools.CommonMethods.checkIsInputIndexOutOfRange;
import static com.thoughtworks.rslist.tools.CommonMethods.getList;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
public class RsController {
    private List<RsEvent> rsList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private User admin = new User("admin", 18, "male", "admin@email.com", "10123456789");

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRspository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

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
    ResponseEntity<RsEvent> getOneRs(@PathVariable Integer index) throws Exception, InvalidIndexException {
//        checkIsInputIndexOutOfRange(index, rsList, "invalid index");
        RsEvent rsEvent = new RsEvent(rsEventRepository.getRsEventsByEventId(index));
        VoteEntity voteEntity = voteRepository.getVoteByEventId(index);
        if (voteEntity != null)
            rsEvent.setVoteNum(voteEntity.getVoteNum());
        return ResponseEntity.ok().body(rsEvent);
    }

    @GetMapping("/rs/list")
    ResponseEntity<List> getRsBetween(@RequestParam(required = false) Integer start,
                                      @RequestParam(required = false) Integer end) throws InvalidIndexException, Exception {
        return getList(start, end, rsList);
    }

    @PostMapping("/rs")
    ResponseEntity addRs(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        if (!userRepository.existsByUserId(rsEvent.getUserId())) {
            return badRequest().build();
        }
        rsEventRepository.save(new RsEventEntity(rsEvent));
        rsList.add(rsEvent);
        return ResponseEntity.created(null).body(rsList.size() - 1);
    }

    @PostMapping("/rs/list")
    ResponseEntity editRs(@RequestParam(required = true) Integer index, @RequestBody String eventJson) throws Exception,
            InvalidIndexException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        checkIsInputIndexOutOfRange(index, rsList, "invalid request param");
        if (newRsEvent.getEventName() != null)
            rsList.get(index - 1).setEventName(newRsEvent.getEventName());
        if (newRsEvent.getKeyWord() != null)
            rsList.get(index - 1).setKeyWord((newRsEvent.getKeyWord()));
        RsEventEntity rsEventEntity = rsEventRepository.getRsEventsByEventId(index);
        if (newRsEvent.getEventName() != null)
            rsEventEntity.setEventName(newRsEvent.getEventName());
        if (newRsEvent.getKeyWord() != null)
            rsEventEntity.setKeyWord((newRsEvent.getKeyWord()));
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null).body(index);
    }

    @DeleteMapping("/rs/{index}")
    ResponseEntity deleteRs(@PathVariable Integer index) throws Exception, InvalidIndexException {
        checkIsInputIndexOutOfRange(index, rsList, "invalid index");
        rsList.remove(index - 1);
        rsEventRepository.deleteByEventId(index);
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

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity vote(@PathVariable(required = true) Integer rsEventId, @RequestBody Vote vote) throws JsonProcessingException {
        UserEntity userEntity = userRepository.getUsersByUserId(vote.getUserId());
        User user = new User(userEntity);
        if (user.getVoteNum() < vote.getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }
        vote.setEventId(rsEventId);
        VoteEntity voteEntity = new VoteEntity(vote);
        voteRepository.save(voteEntity);
        return ResponseEntity.ok().build();
    }

//    @ExceptionHandler(InvalidIndexException.class)
//    ResponseEntity exceptionHandler(InvalidIndexException ex){
//        CommonException commonException = new CommonException();
//        commonException.setError(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException);
//    }
}
