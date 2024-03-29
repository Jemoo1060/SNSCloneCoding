package com.sparta.snsclone.repository;


import com.sparta.snsclone.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPost_PostId(Long postId);
    void deleteAllByPost_PostId(Long postId);
}