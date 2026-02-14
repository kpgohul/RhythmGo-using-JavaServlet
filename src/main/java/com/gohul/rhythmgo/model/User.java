package com.gohul.rhythmgo.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String salt;

    private List<String> roles;


    public User(String name, String email, String password, String salt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

}
