package com.okay.domain.repository;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import com.okay.dto.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostNo (Post post);

    List<Comment> findAllByUserNo(User UserNo);
    List<Comment> findFirst5ByUserNoOrderByCommentNoDesc(User userNo); //최근 기준으로 내림차순 5개 추출

    @Transactional
    void deleteAllByPostNo(Post post);

    @Query(value = "select max(commentNo) from Comment ")
    BigDecimal max();

    //0706

}
