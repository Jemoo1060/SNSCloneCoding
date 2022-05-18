package com.sparta.snsclone.controller;

import com.sparta.snsclone.dto.LoginCheckDto;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.dto.SignUpRequestDto;
import com.sparta.snsclone.security.UserDetailsImpl;
import com.sparta.snsclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/api/user/signup")
    public ResponseDto signup(@RequestParam(value = "profileImg") MultipartFile file, SignUpRequestDto requestDto) {
        return userService.signup(file,requestDto);
    }



    // 로그인 체크
    @GetMapping("/api/user/islogin")
    public LoginCheckDto isLogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        String nickname = userDetails.getUser().getNickName();
        String profileImg = userDetails.getUser().getProfileImg();
        String userName = userDetails.getUser().getUserName();
        String userEmail = userDetails.getUsername();

        return new LoginCheckDto(nickname,profileImg,userName, userEmail);
    }



}
