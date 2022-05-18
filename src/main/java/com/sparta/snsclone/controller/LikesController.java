package com.sparta.snsclone.controller;

import com.sparta.snsclone.dto.LikesResponseDto;
import com.sparta.snsclone.security.UserDetailsImpl;
import com.sparta.snsclone.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LikesController {

    private final LikesService likesService;

    // 좋아요 기능
    @PostMapping("/api/likes/{postId}")
    public LikesResponseDto likeCheck(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return likesService.likeCheck(postId,userDetails.getUsername());
    }

}
