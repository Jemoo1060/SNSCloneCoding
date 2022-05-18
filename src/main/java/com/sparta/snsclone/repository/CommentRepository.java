package com.sparta.snsclone.repository;


import com.sparta.snsclone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT COUNT(commentId) FROM Comment WHERE post.postId = :postId")
    Long findByCountByPost(@Param("postId") Long postId);

    void deleteAllByPost_PostId(Long postId);

    List<Comment> findAllByPost_PostId(Long postId);

}
