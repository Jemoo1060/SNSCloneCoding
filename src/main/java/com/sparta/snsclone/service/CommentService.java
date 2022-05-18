package com.sparta.snsclone.service;

import com.sparta.snsclone.dto.CommentRequsetDto;
import com.sparta.snsclone.dto.CommentResponseDto;
import com.sparta.snsclone.dto.PostResponseDto;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.model.Comment;
import com.sparta.snsclone.model.Post;
import com.sparta.snsclone.model.User;
import com.sparta.snsclone.repository.CommentRepository;
import com.sparta.snsclone.repository.PostRepository;
import com.sparta.snsclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 등록
    public ResponseDto createCommnet(Long postId, CommentRequsetDto commentDto, String userEmail) {

        if(commentDto.getComment().length() > 20){
            return new ResponseDto(400,"댓글 20자 이내");
        }

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            return new ResponseDto(400,"존재하지 않는 게시글 입니다.");
        }

        User user = userRepository.findByUserEmail(userEmail).orElse(null);

        if(user == null){
            return  new ResponseDto(400,"존재하지 않는 회원입니다.");
        }


        Comment comment = new Comment(post,user,commentDto.getComment());

        commentRepository.save(comment);

        return new ResponseDto(200,null);
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto deleteComment(Long commentId, String userEmail) {

        Comment comment = commentRepository.findById(commentId).orElse(null);

        if(comment == null){
            return  new ResponseDto(400,"존재하지 않는 댓글입니다");
        }
        if(!userEmail.equals(comment.getUser().getUserEmail())){
            return  new ResponseDto(400,"작성자만 가능한 기능입니다");
        }

        commentRepository.deleteById(commentId);

        return new ResponseDto(200,null);
    }

    // 댓글 조회
    public List<CommentResponseDto> getcomments(Long postId) {

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            throw new IllegalArgumentException("존재하지 않는 게시글 입니다");
        }

        List<Comment> comments = commentRepository.findAllByPost_PostId(postId);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for(Comment cmt : comments){
            Long commentId = cmt.getCommentId();
            String profileImg = cmt.getUser().getProfileImg();
            String nickName = cmt.getUser().getNickName();
            String comment = cmt.getComment();

            CommentResponseDto commentResponseDto = new CommentResponseDto(commentId,profileImg,nickName,comment);
            commentResponseDtos.add(commentResponseDto);
        }

        return  commentResponseDtos;
    }
}
