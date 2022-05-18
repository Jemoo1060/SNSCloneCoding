package com.sparta.snsclone.service;

import com.sparta.snsclone.dto.LikesResponseDto;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.model.Likes;
import com.sparta.snsclone.model.Post;
import com.sparta.snsclone.model.User;
import com.sparta.snsclone.repository.LikesRepository;
import com.sparta.snsclone.repository.PostRepository;
import com.sparta.snsclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private  final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 좋아요 기능
    @Transactional
    public LikesResponseDto likeCheck(Long postId, String userEmail) {

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            return  new LikesResponseDto(400,"존재하지 않는 게시글 입니다");
        }

        User user = userRepository.findByUserEmail(userEmail).orElse(null);
        if(user == null){
            return  new LikesResponseDto(400,"존재하지 않는 회원입니다.");
        }

        Likes likes = likesRepository.findByUser_UserEmailAndPost_PostId(userEmail,postId).orElse(null);

        if(likes == null){
            Likes saveLikes = new Likes(post,user);
            likesRepository.save(saveLikes);
            return new LikesResponseDto(200,null, true );
        } else {
            likesRepository.deleteById(likes.getLikesId());
            return new LikesResponseDto(200,null, false);
        }

    }
}
