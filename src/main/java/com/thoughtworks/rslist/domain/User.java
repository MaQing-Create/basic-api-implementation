package com.thoughtworks.rslist.domain;

import com.thoughtworks.rslist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
public class User {
    @Size(max = 8)
    @NotNull
    private String userName;

    @Max(100)
    @Min(18)
    @NotNull
    private int age;

    @NotNull
    private String gender;

    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "1\\d{10}")
    private String phone;

    private int voteNum;

    public User() {
        this.voteNum = 10;
    }

    public User(@Size(max = 8) @NotNull String userName, @Max(100) @Min(18) @NotNull int age, @NotNull String gender, @Email String email, @NotNull @Pattern(regexp = "1\\d{10}") String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.voteNum = 10;
    }

    public User(UserEntity userEntity) {
        this.userName = userEntity.getUserName();
        this.age = userEntity.getAge();
        this.gender = userEntity.getGender();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
        this.voteNum = userEntity.getVoteNum();
    }
}
