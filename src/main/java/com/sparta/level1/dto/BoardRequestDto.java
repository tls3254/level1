package com.sparta.level1.dto;

import lombok.Getter;

import java.sql.Date;


@Getter
public class BoardRequestDto {
    private String title;
    private String contents;
    private String username ;
    private String password;
    private String date;
}
