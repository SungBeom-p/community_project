package com.okay.domain.repository;

import com.okay.domain.entity.Survey;
import com.okay.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Long> {
    Page<Survey> findAll(Pageable pageable);

    Page<Survey> findAllByTitleContaining(String title, Pageable pageable);
    Survey findBySurveyNo (Long surveyNo);

}
