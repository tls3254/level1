package com.sparta.level1.dto;

import com.sparta.level1.entity.Board;
import lombok.Data;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private long id;
    private String title;
    private String username;
    private String contents;
    private String date;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
    }

    public BoardResponseDto(Long id,String title, String username,String contents,String date) {
        this.id = id;
        this.title =title;
        this.username = username;
        this.contents=contents;
        this.date = date;
    }
}
