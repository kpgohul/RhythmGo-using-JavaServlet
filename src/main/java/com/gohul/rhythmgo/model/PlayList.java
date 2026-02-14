package com.gohul.rhythmgo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlayList {

    private int id;
    //private int userId;
    private String name;
    private String description;

}
