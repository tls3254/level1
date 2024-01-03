package com.sparta.level1.entity;

import com.sparta.level1.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class Board {
    private Long id;
    private String title;
    private String contents;
    private String username;
    private String password;
    private String date;


    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }

    public void update(BoardRequestDto requestDto) {
        this.title= requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = requestDto.getContents();
        this.password = requestDto.getContents();
    }
}
