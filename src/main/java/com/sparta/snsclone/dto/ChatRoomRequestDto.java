package com.sparta.snsclone.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomRequestDto {

    private String chatRoomName;
    private Long userId;
    private String nickName;

}
