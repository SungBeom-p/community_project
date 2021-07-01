package com.okay.domain.repository;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByCategory(String category, Pageable pageable);
    Page<Post> findAllByTitleContainingAndCategory(String title, String category, Pageable pageable);
    Page<Post> findAllByContentContainingAndCategory(String content, String category, Pageable pageable);
    Page<Post> findAllByNameContainingAndCategory(String name, String category, Pageable pageable);
    Page<Post> findAllByTitleContainingOrContentContainingOrNameContaining(String title, String content, String name, Pageable pageable);

    Post findByPostNo (Long postNo);


    List<Post> findFirst5ByOrderByViewsDesc(); //조회수를 기준으로 내림차순 5개 추출
    List<Post> findFirst10ByOrderByViewsDesc(); //조회수를 기준으로 내림차순 10개 추출
    List<Post> findAllByUserNo(User userNo);

    List<Post> findAllByCategory(String category); //카테고리로 리스트





}
