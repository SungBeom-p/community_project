package com.okay.service;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import com.okay.domain.repository.SurveyCommentRepository;
import com.okay.domain.repository.SurveyRepository;
import com.okay.dto.SurveyCommentDto;
import com.okay.dto.SurveyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public List<SurveyComment> selecttopAll(Survey surveyNo){
        List<SurveyComment> list = surveyCommentRepository.findFirst15BySurveyNoOrderByIdDesc(surveyNo);
        return list;
    }



    public Page<SurveyComment> pagesurveycomment(Survey surveyNo,Pageable pageable){
        Page<SurveyComment> surveyCommentPage = surveyCommentRepository.findAllBySurveyNo(surveyNo,pageable);
        return surveyCommentPage;
    }

 // 관리자의 회원관리에 사용
    public List<SurveyComment> selectsize(){
        List<SurveyComment> list = surveyCommentRepository.findAllBy();
        return list;
    }
    //회원 + 관리자의 회원활동내역에 사용
    public List<SurveyComment> selectcommentsize(User userNo){
        List<SurveyComment> list = surveyCommentRepository.findAllByUserNo(userNo);

        return list;
    }
    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<SurveyComment> listcomment(User userNo){
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


}
