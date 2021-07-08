package com.okay.service;

import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import com.okay.domain.repository.SurveyCommentRepository;
import com.okay.domain.repository.SurveyRepository;
import com.okay.dto.SurveyCommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyCommentService {
    @Autowired
    SurveyCommentRepository surveyCommentRepository;
    @Autowired
    SurveyRepository surveyRepository;

    public void create(SurveyCommentDto surveyCommentDto){ // CREATE
        SurveyComment surveyComment = surveyCommentDto.changeSurveyComment(surveyCommentDto);
        surveyCommentRepository.save(surveyComment);
    }

    public List<SurveyComment> selectAll(Survey surveyNo){
        List<SurveyComment> list = surveyCommentRepository.findAllBySurveyNoOrderByIdDesc(surveyNo);
        return list;
    }

 // 관리자의 회원관리에 사용
    public List<SurveyComment> selectSize(){
        List<SurveyComment> list = surveyCommentRepository.findAll();
        return list;
    }
    //회원 + 관리자의 회원활동내역에 사용
    public List<SurveyComment> selectCommentSize(User userNo){
        List<SurveyComment> list = surveyCommentRepository.findAllByUserNo(userNo);
        return list;
    }
    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<SurveyComment> listComment(User userNo){
        List<SurveyComment> list = surveyCommentRepository.findFirst5ByUserNoOrderByIdDesc(userNo);
        return list;
    }
// 0705
    public SurveyComment selectOne(Long id) { // DB SELECT ONE <- ID
        Optional<SurveyComment> result = surveyCommentRepository.findById(id);
        return result.get();
    }
// 0705
    public void update(SurveyCommentDto surveyCommentDto) { // DB UPDATE
        SurveyComment surveyComment = surveyCommentDto.changeSurveyComment(surveyCommentDto);
        surveyCommentRepository.save(surveyComment);
    }

    public Long max(){

        BigDecimal max = surveyCommentRepository.max();
        Long no = Long.valueOf(String.valueOf(max));
        return no;
    }

    //희진

    public void deleteAll(Long Id) {
        Optional<Survey> survey = surveyRepository.findById(Id);
        surveyCommentRepository.deleteAllBySurveyNo(survey.get());
    }



}
