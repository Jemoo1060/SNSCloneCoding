package com.sparta.snsclone.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String profileImg;
    private String nickName;
    private String comment;

    public CommentResponseDto(Long commentId, String profileImg, String nickName, String comment) {
        this.commentId = commentId;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.comment = comment;
    }
}
