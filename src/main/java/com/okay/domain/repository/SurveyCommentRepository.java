package com.okay.domain.repository;

import com.okay.domain.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SurveyCommentRepository extends JpaRepository<SurveyComment,Long> {
    List<SurveyComment> findFirst15BySurveyNoOrderByIdDesc(Survey surveyNo); //최근 기준으로 내림차순 5개 추출
    List<SurveyComment> findAllBySurveyNoOrderByIdDesc(Survey surveyNo);
    List<SurveyComment> findAllBy(); //관리자 회원관리용
    List<SurveyComment> findAllByUserNo(User userNo); //회원,관리자 회원활동내역용
    Page<SurveyComment> findAllBySurveyNo(Survey surveyNo, Pageable pageable);

    List<SurveyComment> findFirst5ByUserNoOrderByIdDesc(User userNo); //최근 기준으로 내림차순 5개 추출
    //dto 넘버 증감
    @Query(value = "select max(surveyNo) from Survey ")
    BigDecimal max();

}
