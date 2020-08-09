package com.thoughtworks.rslist;

import com.thoughtworks.rslist.api.VoteController;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public VoteController votecontroller(){
        return new VoteController();
    }

//    @Bean
//    public RsEventRspository ssEventRspository(){
//        return new RsEventRspository();
//    }
}
