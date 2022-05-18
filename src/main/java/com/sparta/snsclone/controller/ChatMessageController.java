package com.sparta.snsclone.controller;

import com.sparta.snsclone.dto.ChatMessageRequestDto;
import com.sparta.snsclone.model.ChatMessage;
import com.sparta.snsclone.model.User;
import com.sparta.snsclone.repository.UserRepository;
import com.sparta.snsclone.security.jwt.JwtDecoder;
import com.sparta.snsclone.service.ChatMessageService;
import com.sparta.snsclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;


    // 채팅 메시지를 @MessageMapping 형태로 받는다
    // 웹소켓으로 publish 된 메시지를 받는 곳이다
    @MessageMapping("/api/chat/message")
    public void message(@RequestBody ChatMessageRequestDto messageRequestDto, @Header("token") String token) {

        System.out.println(token);
        System.out.println();
        // 로그인 회원 정보를 들어온 메시지에 값 세팅
        String username = jwtDecoder.decodeUsername(token);
        Optional<User> user1 = userRepository.findByUserEmail(username);
        User user = user1.get();
        messageRequestDto.setUserId(user.getUserId());
        messageRequestDto.setSender(user.getUserEmail());

        // 메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        messageRequestDto.setCreatedAt(dateResult);

        // DTO 로 채팅 메시지 객체 생성
        ChatMessage chatMessage = new ChatMessage(messageRequestDto, userService);

        // 웹소켓 통신으로 채팅방 토픽 구독자들에게 메시지 보내기
        chatMessageService.sendChatMessage(chatMessage);

        // MySql DB에 채팅 메시지 저장
        chatMessageService.save(chatMessage);
    }
}
