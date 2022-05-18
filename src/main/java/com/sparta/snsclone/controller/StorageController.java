package com.sparta.snsclone.controller;


import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.security.UserDetailsImpl;
import com.sparta.snsclone.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    // 게시글 작성(이미지 업로드)
    @PostMapping(value = "/api/post")
    public ResponseDto uploadFileCreatePost(String content, @RequestParam(value = "contentImg") ArrayList<MultipartFile> files, @AuthenticationPrincipal UserDetailsImpl userDetails) {


        return storageService.uploadFile(files, content, userDetails.getUsername());
    }

}
