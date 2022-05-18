package com.sparta.snsclone.dto;


import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {
    private Long postId;
    private String nickName;
    private String profileImg;
    private List<String> contentImg;
    private String content;
    private String createAt;
    private Long likeCnt;
    private Long commnetCnt;
    private boolean clicked;


    public PostResponseDto() {
    }

    public PostResponseDto(Long postId, String nickName, String profileImg, List<String> contentImg, String content, String createAt, Long likeCnt, Long commnetCnt, boolean clicked) {
        this.postId = postId;
        this.nickName = nickName;
        this.profileImg = profileImg;
        this.contentImg = contentImg;
        this.content = content;
        this.createAt = createAt;
        this.likeCnt = likeCnt;
        this.commnetCnt = commnetCnt;
        this.clicked = clicked;
    }
}
