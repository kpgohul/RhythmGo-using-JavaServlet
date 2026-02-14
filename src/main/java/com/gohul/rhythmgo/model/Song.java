package com.gohul.rhythmgo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Song {

    private int id;
    private String title;
    private String album;
    private int artistId;

}
