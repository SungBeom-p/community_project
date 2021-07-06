package com.okay.domain.repository;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByCategory(String category, Pageable pageable); //전체 페이지
    Page<Post> findAllByTitleContainingAndCategory(String title, String category, Pageable pageable); //제목 검색
    Page<Post> findAllByContentContainingAndCategory(String content, String category, Pageable pageable); //내용검색
    Page<Post> findAllByNameContainingAndCategory(String name, String category, Pageable pageable); //작성자 검색
    Page<Post> findAllByTitleContainingOrContentContainingOrNameContaining(String title, String content, String name, Pageable pageable); //메인에서 검색


    @Query(value = "select max(postNo) from Post")
    BigDecimal max();

    List<Post> findFirst5ByUserNoOrderByPostNoDesc(User userNo); //최근 기준으로 내림차순 5개 추출
    List<Post> findFirst5ByCategoryOrderByViewsDesc(String category); //조회수를 기준으로 내림차순 5개 추출
    List<Post> findFirst10ByCategoryOrderByViewsDesc(String category); //조회수를 기준으로 내림차순 10개 추출
    List<Post> findAllByUserNo(User userNo);
    List<Post> findAllByCategory(String category); //카테고리로 리스트
    //210702 update view
    //Post findAllByPostNo(Long postNo);





}
