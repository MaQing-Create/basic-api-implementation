package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String userName;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNum;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy ="userId")
    private List<RsEventEntity> events;

    public UserEntity(User user){
        this.userName = user.getUserName();
        this.gender = user.getGender();
        this.age = user.getAge();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.voteNum = user.getVoteNum();
    }
}