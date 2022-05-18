package com.sparta.snsclone.model;


import com.sparta.snsclone.dto.ChatRoomRequestDto;
import com.sparta.snsclone.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends Timestamped {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column
    private String chatRoomName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<User> userList = new ArrayList<>();


    public ChatRoom(ChatRoomRequestDto requestDto, UserService userService) {
        this.chatRoomName = requestDto.getChatRoomName();
        this.userList.add(userService.findById(requestDto.getUserId()));
    }

    public ChatRoom(String name){
        this.chatRoomName = name;
    }
}
