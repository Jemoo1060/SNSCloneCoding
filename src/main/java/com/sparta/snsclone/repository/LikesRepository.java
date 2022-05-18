package com.sparta.snsclone.repository;


import com.sparta.snsclone.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT COUNT(likesId) FROM Likes WHERE post.postId = :postId")
    Long findByCountByPost(@Param("postId") Long postId);

    Optional<Likes> findByPost_PostIdAndUser_NickName(Long postId, String nickname);
    void deleteAllByPost_PostId(Long postId);

    Optional<Likes> findByUser_UserEmailAndPost_PostId(String userEmail, Long postId);
}
