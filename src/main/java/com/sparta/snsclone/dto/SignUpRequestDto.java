package com.sparta.snsclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {

    private String userEmail;
    private String userName;
    private String nickName;
    private String password;

}
