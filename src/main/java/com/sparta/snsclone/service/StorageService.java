package com.sparta.snsclone.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.model.Image;
import com.sparta.snsclone.model.Post;
import com.sparta.snsclone.model.User;
import com.sparta.snsclone.repository.ImageRepository;
import com.sparta.snsclone.repository.PostRepository;
import com.sparta.snsclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;


    @Autowired
    private AmazonS3 s3Client;

    // 게시글 등록(이미지 첨부)
    @Transactional
    public ResponseDto uploadFile(ArrayList<MultipartFile> files, String content, String userEmail) {

        // 유효성 검사
        if(content.length() > 100){
            return new ResponseDto(400,"내용은 100자 이하");
        }

        // 회원 예외처리
        User user = userRepository.findByUserEmail(userEmail).orElse(null);
        if(user == null){
            return new ResponseDto(400,"존재하지 않는 회원입니다.");
        }

        // 이미지 업로드
        List<String> urls = new ArrayList<>();
        try{
            for (MultipartFile file : files){
                File fileObj = convertMultiPartFileToFile(file);
                String forExtensionName = file.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + "." +forExtensionName.substring(forExtensionName.lastIndexOf(".") + 1);
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                String url = s3Client.getUrl(bucketName,fileName).toString();
                System.out.println(url);
                urls.add(url);
                fileObj.delete();
            }
        } catch (Exception e){
            return new ResponseDto(400, "올바르지 않은 이미지 파일입니다");
        }

        // 게시글, 이미지 db 등록
        Post post = new Post(user,content);
        postRepository.save(post);
        for(String url : urls){
            Image image = new Image(post,url);
            imageRepository.save(image);
        }


        return new ResponseDto(200, null);
    }

    // 이미지 변환(업로드)
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }


}
