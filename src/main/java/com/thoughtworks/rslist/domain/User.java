package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.constraints.*;

public class User {

    //    "user": {
//        "userName": "xiaowang",
//                "age": 19,
//                "gender": "female",
//                "email": "a@thoughtworks.com",
//                "phone": 18888888888
//    }
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

    public User() {
    }

    public User(String userName, int age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
