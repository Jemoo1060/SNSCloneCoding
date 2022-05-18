package com.sparta.snsclone.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.dto.SignUpRequestDto;
import com.sparta.snsclone.model.User;
import com.sparta.snsclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    private AmazonS3 s3Client;

    // 회원 가입
    public ResponseDto signup(MultipartFile file,SignUpRequestDto requestDto) {
        // 유효성검사
        ResponseDto responseDto = validTest(requestDto);
        if(responseDto.getStatus() != 200){
            return responseDto;
        }

        // 아이디 중복 체크
        String userEmail = requestDto.getUserEmail();
        String nickName = requestDto.getNickName();
        User userEmailCheck  = userRepository.findByUserEmail(userEmail).orElse(null);
        if(userEmailCheck != null){
            return new ResponseDto(400, "중복된 ID값");
        }

        // 닉네임 중복 체크
        User nickNameCheck  = userRepository.findByNickName(nickName).orElse(null);
        if(nickNameCheck != null){
            return new ResponseDto(400, "중복된 닉네임값");
        }
        
        // 프로필 파일 업로드
        String profileImg = "";
        try{
            File fileObj = convertMultiPartFileToFile(file);
            String forExtensionName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + "." +forExtensionName.substring(forExtensionName.lastIndexOf(".") + 1);
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            profileImg = s3Client.getUrl(bucketName,fileName).toString();
        } catch (Exception e){
            profileImg = "https://spring-fileupload.s3.ap-northeast-2.amazonaws.com/default.png";
        }

        // 회원정보 db 등록
        String password = passwordEncoder.encode(requestDto.getPassword());
        String userName = requestDto.getUserName();
        
        User signUpInfo = new User(userEmail,password,userName,profileImg,nickName);

        userRepository.save(signUpInfo);

        return new ResponseDto(200,null);
    }

    // 유효성 검사
    private ResponseDto validTest(SignUpRequestDto requestDto) {

        String userEmail = requestDto.getUserEmail();
        String nickName = requestDto.getNickName();
        String password = requestDto.getPassword();

        if(!Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", userEmail)){
            return new ResponseDto(400,"이메일형식이 아닙니다");
        }

        if(nickName.length()< 4 || nickName.length() > 10){
            return new ResponseDto(400,"닉네임 4~10자");
        }

        if(password.length() < 4 || password.length()> 16){
            return new ResponseDto(400,"비밀번호 4~16자");
        }

        return new ResponseDto(200,null);
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("찾는 유저가 없습니다")
        );
        return user;
    }
}
