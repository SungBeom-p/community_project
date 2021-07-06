package com.okay.domain.repository;

import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Long> {
    Page<Survey> findAll(Pageable pageable);
    Page<Survey> findAllByTitleContaining(String title, Pageable pageable);
    Survey findBySurveyNo (Long surveyNo);
    List<Survey> findFirst5ByOrderByViewsDesc();
    List<Survey> findAllByUserNo(User userNo); //회원,관리자가 회원 활동내역에 사용
    List<Survey> findAllBy();  //관리자 회원관리에사용
    List<Survey> findFirst5ByUserNoOrderBySurveyNoDesc(User userNo); //최근 기준으로 내림차순 5개 추출


//dto 넘버 증감
    @Query(value = "select max(surveyNo) from Survey ")
    BigDecimal max();

}
