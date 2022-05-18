package com.sparta.snsclone.controller;

import com.sparta.snsclone.dto.CommentRequsetDto;
import com.sparta.snsclone.dto.CommentResponseDto;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.security.UserDetailsImpl;
import com.sparta.snsclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;


    // 댓글 조회
    @GetMapping("/api/comment/{postId}")
    public List<CommentResponseDto> getComments(@PathVariable Long postId) {

        return commentService.getcomments(postId);
    }

    // 댓글 등록
    @PostMapping("/api/comment/{postId}")
    public ResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequsetDto commentDto, @AuthenticationPrincipal UserDetailsImpl userDetails){


        return commentService.createCommnet(postId,commentDto,userDetails.getUsername());
    }

    // 댓글 삭제
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseDto deletePost(@PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails ){


        return commentService.deleteComment(commentId,userDetails.getUsername());
    }

}
