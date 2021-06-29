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
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByTitleContaining(String title, Pageable pageable);
    Page<Post> findAllByContentContaining(String content, Pageable pageable);
    Page<Post> findAllByNameContaining(String name, Pageable pageable);
    Page<Post> findAllByTitleContainingOrContentContainingOrNameContaining(String title, String contetnt, String name, Pageable pageable);
    List<Post> findFirst5ByOrderByViewsDesc(); //조회수를 기준으로 내림차순 5개 추출
    Post findByPostNo (Long postNo);

    List<Post> findFirst10ByOrderByViewsDesc(); //조회수를 기준으로 내림차순 10개 추출
    List<Post> findAllByUserNo(User userNo);
}
