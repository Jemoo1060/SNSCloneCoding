package com.sparta.snsclone.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class User {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long userId;


    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String profileImg;

    @Column(nullable = false)
    private String nickName;


    public User(String userEmail, String password, String userName, String profileImg, String nickName) {
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.profileImg = profileImg;
        this.nickName = nickName;
    }
}
