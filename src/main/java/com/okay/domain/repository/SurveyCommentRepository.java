package com.okay.domain.repository;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyCommentRepository extends JpaRepository<SurveyComment,Long> {
    List<SurveyComment> findAllBySurveyNoOrderByIdDesc(Survey surveyNo,Pageable pageable);
    List<SurveyComment> findAllBy();
}
