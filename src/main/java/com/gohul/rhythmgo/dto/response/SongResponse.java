package com.gohul.rhythmgo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {

    private int id;
    private String title;
    private String album;
    private int artistId;

}
