package com.okay.service;

import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import com.okay.domain.repository.SurveyCommentRepository;
import com.okay.dto.SurveyCommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyCommentService {
    @Autowired
    SurveyCommentRepository surveyCommentRepository;

    public void create(SurveyCommentDto surveyCommentDto){ // CREATE
        SurveyComment surveyComment = surveyCommentDto.changeSurveyComment(surveyCommentDto);
        surveyCommentRepository.save(surveyComment);
    }

    public List<SurveyComment> selectAll(Survey surveyNo){
        List<SurveyComment> list = surveyCommentRepository.findAllBySurveyNoOrderByIdDesc(surveyNo);
        return list;
    }

    public Page<SurveyComment> pagesurveycomment(Survey surveyNo,Pageable pageable){
        Page<SurveyComment> surveyCommentPage = surveyCommentRepository.findAllBySurveyNo(surveyNo,pageable);
        return surveyCommentPage;
    }


    public List<SurveyComment> selectsize(){
        List<SurveyComment> list = surveyCommentRepository.findAllBy();
        return list;
    }

    public Long max(){

        BigDecimal max = surveyCommentRepository.max();
        Long no = Long.valueOf(String.valueOf(max));
        return no;
    }


}
