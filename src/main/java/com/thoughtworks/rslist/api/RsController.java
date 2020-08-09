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
import java.util.List;
import java.util.stream.Collectors;

import static com.thoughtworks.rslist.tools.CommonMethods.checkIsInputIndexOutOfRange;
import static com.thoughtworks.rslist.tools.CommonMethods.getList;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
public class RsController {
    private List<RsEvent> rsList;
    private List<User> userList;
    private List<Vote> voteList;

    private List<RsEventEntity> rsEventEntityList;

    final UserRepository userRepository;
    final RsEventRspository rsEventRepository;
    final VoteRepository voteRepository;


    public RsController(UserRepository userRepository, RsEventRspository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/rs/{index}")
    ResponseEntity<RsEvent> getOneRs(@PathVariable Integer index) throws Exception, InvalidIndexException {
        rsList = rsEventRepository.findAll().stream().map(rsEventEntity -> new RsEvent(rsEventEntity)).collect(Collectors.toList());
        checkIsInputIndexOutOfRange(index, rsList, "invalid index");
        RsEvent rsEvent = rsList.get(index - 1);
        VoteEntity voteEntity = voteRepository.getVoteByEventId(index);
        if (voteEntity != null)
            rsEvent.setVoteNum(voteEntity.getVoteNum());
        return ResponseEntity.ok().body(rsEvent);
    }

    @GetMapping("/rs/list")
    ResponseEntity<List> getRsBetween(@RequestParam(required = false) Integer start,
                                      @RequestParam(required = false) Integer end) throws InvalidIndexException, Exception {
        rsList = rsEventRepository.findAll().stream().map(rsEventEntity -> new RsEvent(rsEventEntity)).collect(Collectors.toList());

        return getList(start, end, rsList);
    }

    @PostMapping("/rs")
    ResponseEntity addRs(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        if (!userRepository.existsByUserId(rsEvent.getUserId())) {
            return badRequest().build();
        }
        rsEventRepository.save(new RsEventEntity(rsEvent));
        return ResponseEntity.created(null).body(rsEventRepository.count());
    }

    @PostMapping("/rs/list")
    ResponseEntity editRs(@RequestParam(required = true) Integer index, @RequestBody String eventJson) throws Exception,
            InvalidIndexException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent newRsEvent = objectMapper.readValue(eventJson, RsEvent.class);
        rsEventEntityList = rsEventRepository.findAll();
        checkIsInputIndexOutOfRange(index, rsEventEntityList, "invalid request param");
        if (newRsEvent.getEventName() != null)
            rsEventEntityList.get(index - 1).setEventName(newRsEvent.getEventName());
        if (newRsEvent.getKeyWord() != null)
            rsEventEntityList.get(index - 1).setKeyWord((newRsEvent.getKeyWord()));
        rsEventRepository.save(rsEventEntityList.get(index - 1));
        return ResponseEntity.created(null).body(index);
    }

    @DeleteMapping("/rs/{index}")
    ResponseEntity deleteRs(@PathVariable Integer index) throws Exception, InvalidIndexException {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        checkIsInputIndexOutOfRange(index, rsEventEntityList, "invalid index");
        rsEventRepository.deleteByEventId(rsEventEntityList.get(index).getEventId());
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

    @PostMapping("/rs/{rsEventId}/vote")
    ResponseEntity vote(@PathVariable(required = true) Integer rsEventId, @RequestBody Vote vote) throws JsonProcessingException {
        UserEntity userEntity = userRepository.getUsersByUserId(vote.getUserId());
        if (userEntity.getVoteNum() < vote.getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }
        vote.setEventId(rsEventId);
        VoteEntity voteEntity = new VoteEntity(vote);
        voteRepository.save(voteEntity);
        userEntity.setVoteNum(userEntity.getVoteNum() - vote.getVoteNum());
        userRepository.save(userEntity);
        return ResponseEntity.ok().build();
    }
}
