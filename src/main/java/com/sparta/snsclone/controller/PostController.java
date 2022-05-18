package com.sparta.snsclone.controller;

import com.sparta.snsclone.dto.*;
import com.sparta.snsclone.security.UserDetailsImpl;
import com.sparta.snsclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 전체 게시글 페이징
    @PostMapping("/api/posts")
    public Page<PostResponseDto> getPosts(@RequestBody PageRequestDto pageRequestDto) {

        int page = pageRequestDto.getPage() - 1;
        int size = 3;
        String sortBy = "createAt";
        return postService.getPosts(page, size, sortBy,pageRequestDto.getNickName());
    }

    // 상세 게시글
    @PostMapping("/api/post/detail")
    public PostResponseDto getPost(@RequestBody DetailPostRequestDto postRequestDto) {
        return postService.getPost(postRequestDto.getPostId(),postRequestDto.getNickName());
    }

    // 게시글 수정
    @PutMapping("/api/post/{postId}")
    public ResponseDto updatePost(@PathVariable("postId") Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails ){


        return postService.updatePost(postId,postRequestDto.getContent(),userDetails.getUsername());
    }


    // 게시글 삭제
    @DeleteMapping("/api/post/{postId}")
    public ResponseDto deletePost(@PathVariable("postId") Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails ){


        return postService.deletepost(postId,userDetails.getUsername());
    }


}
