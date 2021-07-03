package com.okay.service;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Survey;
import com.okay.domain.entity.User;
import com.okay.domain.repository.SurveyRepository;
import com.okay.dto.SearchDto;
import com.okay.dto.SurveyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class SurveyService extends Service {

    @Autowired
    SurveyRepository surveyRepository;

    public void create(SurveyDto surveyDto) { // DB CREATE
        Survey survey = surveyDto.changeSurvey(surveyDto);
        surveyRepository.save(survey);
    }


    public void remove(SurveyDto surveyDto) { // DB DELETE
        surveyRepository.deleteById(surveyDto.getSurveyNo());
    }

    public void update(SurveyDto surveyDto) { // DB UPDATE
        Survey survey = surveyDto.changeSurvey(surveyDto);
        surveyRepository.save(survey);
    }

    public Survey selectOne(Long id) { // DB SELECT ONE <- ID
        Optional<Survey> result = surveyRepository.findById(id);
        return result.get();
    }



    public Page<Survey> getSurveyList(Pageable pageable, SearchDto searchDto) { // 글 목록 조회
        Page<Survey> surveyList = null;
        Page<Survey> nullStoreList = null;
        switch (searchDto.getSearchFilter()) {
//            case "title_content":
//                surveyList = surveyRepository.findAllByTitleContainingOrContentContaining(searchDto.getSearchValue(), searchDto.getSearchValue(), pageable); //전체 검색
//                break;
            case "title":
                surveyList = surveyRepository.findAllByTitleContaining(searchDto.getSearchValue(), pageable); //제목으로 검색하는 경우
                break;
//            case "content":
//                surveyList = surveyRepository.findAllByContentContaining(searchDto.getSearchValue(), pageable); //내용으로 검색하는 경우
//                break;
            default:
                surveyList = surveyRepository.findAll(pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if (surveyList.isEmpty()) return nullStoreList; //검색 결과가 없을 경우 빈 객체 반환

        return surveyList;
    }

    public SurveyDto getSurvey(Long surveyNo) {
        Survey surveyEntity = surveyRepository.findBySurveyNo(surveyNo);
        SurveyDto surveyDto = new SurveyDto();
        surveyDto = surveyDto.changeSurveyDto(surveyEntity);
        return surveyDto;
    }
    public List<Survey> surveyListfive(){
        List<Survey> surveyList = surveyRepository.findFirst5ByOrderByViewsDesc();
        return surveyList;
    }
    public List<Survey> selectadmin(){
        List<Survey> list = surveyRepository.findAllBy();
        return list;
    }

    //회원 관리자 활동내역에 사용 0703
    public List<Survey> selectActive(User userNo){
        List<Survey> list = surveyRepository.findAllByUserNo(userNo);
        return list;
    }
    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<Survey> listsurvey(User userNo){
        List<Survey> list = surveyRepository.findFirst5ByUserNoOrderBySurveyNoDesc(userNo);
        return list;
    }


    public Long max(){

        BigDecimal max = surveyRepository.max();
        Long no = Long.valueOf(String.valueOf(max));
        return no;
    }



}
