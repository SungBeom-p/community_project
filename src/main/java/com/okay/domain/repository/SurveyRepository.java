package com.okay.domain.repository;

import com.okay.domain.entity.Survey;
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

//dto 넘버 증감
    @Query(value = "select max(surveyNo) from Survey ")
    BigDecimal max();

}
