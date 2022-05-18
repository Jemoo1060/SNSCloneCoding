package com.sparta.snsclone.dto;

import lombok.Getter;

@Getter
public class LoginCheckDto {
    private String nickName;
    private String profileImg;
    private String userName;
    private String userEmail;


    public LoginCheckDto() {
    }

    public LoginCheckDto(String nickName, String profileImg, String userName, String userEmail) {
        this.nickName = nickName;
        this.profileImg = profileImg;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
