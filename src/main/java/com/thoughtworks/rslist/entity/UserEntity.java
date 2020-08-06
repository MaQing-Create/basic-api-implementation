package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer userId;
    private String userName;
    private String gender;
    private int age;
    private String email;
    private String phone;

    public UserEntity(User user){
        this.userName = user.getUserName();
        this.gender = user.getGender();
        this.age = user.getAge();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }
}