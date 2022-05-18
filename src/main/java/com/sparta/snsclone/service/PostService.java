package com.sparta.snsclone.service;

import com.amazonaws.services.s3.AmazonS3;
import com.sparta.snsclone.dto.PostResponseDto;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.model.Image;
import com.sparta.snsclone.model.Likes;
import com.sparta.snsclone.model.Post;
import com.sparta.snsclone.model.User;
import com.sparta.snsclone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class PostService {


    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;


    @Value("${application.bucket.name}")
    private String bucketName;
    
    @Autowired
    private AmazonS3 s3Client;
    
    // 전체 게시글 페이징
    public  Page<PostResponseDto> getPosts(int page, int size, String sortBy, String nickname) {

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> entities = postRepository.findAll(pageable);
        Page<PostResponseDto> postResponseDtos = entities.map(new Function<Post, PostResponseDto>() {
            @Override
            public PostResponseDto apply(Post post) {
                Long postId = post.getPostId();
                String nickName = post.getUser().getNickName();
                String profileImg = post.getUser().getProfileImg();
                String content = post.getContent();
                String createdAt = post.getCreateAt().toString();
                Long likeCnt = likesRepository.findByCountByPost(postId);
                Long commnetCnt = commentRepository.findByCountByPost(postId);
                Likes likes = likesRepository.findByPost_PostIdAndUser_NickName(postId,nickname).orElse(null);
                boolean clicked = likes != null;
                List<Image> imgs = imageRepository.findAllByPost_PostId(postId);
                List<String> contentImg = new ArrayList<>();
                for(Image image : imgs){
                    contentImg.add(image.getContentImg());
                }

                PostResponseDto dto = new PostResponseDto(postId,nickName,profileImg,contentImg,content,createdAt,likeCnt,commnetCnt,clicked);

                return dto;
            }
        });
        return postResponseDtos;
    }

    // 상세 게시글 정보
    public PostResponseDto getPost(Long postid, String userNick) {
        Post post = postRepository.findById(postid).orElse(null);

        if(post == null){
            throw new IllegalArgumentException("이미 삭제된 게시글 입니다");
        }
        
        Long postId = post.getPostId();
        String nickName = post.getUser().getNickName();
        String profileImg = post.getUser().getProfileImg();
        String content = post.getContent();
        String createdAt = post.getCreateAt().toString();
        Long likeCnt = likesRepository.findByCountByPost(postId);
        Long commnetCnt = commentRepository.findByCountByPost(postId);
        Likes likes = likesRepository.findByPost_PostIdAndUser_NickName(postId,userNick).orElse(null);
        boolean clicked = likes != null;
        List<Image> imgs = imageRepository.findAllByPost_PostId(postId);
        List<String> contentImg = new ArrayList<>();
        for(Image image : imgs){
            contentImg.add(image.getContentImg());
        }
        
        return new PostResponseDto(postId,nickName,profileImg,contentImg,content,createdAt,likeCnt,commnetCnt,clicked);
    }

    // 게시글 수정
    @Transactional
    public ResponseDto updatePost(Long postId, String content, String userEmail) {

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            return  new ResponseDto(400,"존재하지 않는 게시글입니다");
        }

        User user = userRepository.findByUserEmail(userEmail).orElse(null);
        if(user == null){
            return  new ResponseDto(400,"존재하지 않는 회원입니다");
        }

        if(!userEmail.equals(post.getUser().getUserEmail())){
            return  new ResponseDto(400,"작성자만 가능한 기능입니다.");
        }

        post.update(user,content);

        return  new ResponseDto(200,null);
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto deletepost(Long postId, String userEmail) {

        Post post = postRepository.findById(postId).orElse(null);

        if(post == null){
            return  new ResponseDto(400,"존재하지 않는 게시글입니다");
        }

        if(!userEmail.equals(post.getUser().getUserEmail())){
            return  new ResponseDto(400,"작성자만 가능한 기능입니다.");
        }

        List<Image> images = imageRepository.findAllByPost_PostId(post.getPostId());
        List<String> fileNames = new ArrayList<>();
        for(Image image : images){
            String fileName = image.getContentImg().split(".com/")[1];
            fileNames.add(fileName);
        }

        commentRepository.deleteAllByPost_PostId(postId);
        imageRepository.deleteAllByPost_PostId(postId);
        likesRepository.deleteAllByPost_PostId(postId);
        postRepository.deleteById(postId);

        for(String fileName : fileNames){
            System.out.println(deleteFile(fileName));
        }
        
        return new ResponseDto(200, null);
    }

    // S3 이미지파일 삭제
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


}
