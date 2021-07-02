package com.okay.domain.repository;

import com.okay.domain.entity.Comment;
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
public interface SurveyCommentRepository extends JpaRepository<SurveyComment,Long> {
    List<SurveyComment> findAllBySurveyNoOrderByIdDesc(Survey surveyNo);
    List<SurveyComment> findAllBy();
    Page<SurveyComment> findAllBySurveyNo(Survey surveyNo, Pageable pageable);

    //dto 넘버 증감
    @Query(value = "select max(surveyNo) from Survey ")
    BigDecimal max();

}
