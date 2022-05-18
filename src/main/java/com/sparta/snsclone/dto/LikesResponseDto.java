package com.sparta.snsclone.dto;

import lombok.Getter;

@Getter
public class LikesResponseDto {

    private int status;
    private String msg;
    private boolean clicked;

    public LikesResponseDto() {}

    public LikesResponseDto(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public LikesResponseDto(int status, String msg, boolean clicked) {
        this.status = status;
        this.msg = msg;
        this.clicked = clicked;
    }
}
