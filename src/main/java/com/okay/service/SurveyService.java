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

// survey게시물 삭제시 사용
    public void remove(Long surveyNo) { // DB DELETE
        surveyRepository.deleteById(surveyNo);
    }

    public void update(SurveyDto surveyDto) { // DB UPDATE
        Survey survey = surveyDto.changeSurvey(surveyDto);
        surveyRepository.save(survey);
    }

    public Survey selectOne(Long id) { // DB SELECT ONE <- ID
        Optional<Survey> result = surveyRepository.findById(id);
        return result.get();
    }

//0706 진성
    public SurveyDto selectOneDto(Long id){
        Optional<Survey> byId = surveyRepository.findById(id);
        SurveyDto surveyDto = new SurveyDto();
        SurveyDto result = surveyDto.changeSurveyDto(byId.get());
        return result;
    }
//0706 진성
    public void addResult(Long id, String result){
        Optional<Survey> temp = surveyRepository.findById(id);
        SurveyDto surveyDto = new SurveyDto();
        surveyDto = surveyDto.changeSurveyDto(temp.get());
        if(result.equals("true")){
            surveyDto.setResult1(temp.get().getResult1()+1L);
        } else{
            surveyDto.setResult2(temp.get().getResult2()+1L);
        }
        Survey survey = surveyDto.changeSurvey(surveyDto);
        surveyRepository.save(survey);
    }

    public Page<Survey> getSurveyList(Pageable pageable, SearchDto searchDto) { // 글 목록 조회
        Page<Survey> surveyList = null;
        Page<Survey> nullSurveyList = null;
        switch (searchDto.getSearchFilter()) {
            //전체글 인것 마냥 바꿔주시면되요 히진씨
            //아 그리고 히진씨~ 이거 case가 굳이 필요없을수 있으니 없이 표현 가능하면
            // 그렇게 변환해주실수있다면~ 해주세요 ^^
            case "title":
                surveyList = surveyRepository.findAllByTitleContaining(searchDto.getSearchValue(), pageable); //제목으로 검색하는 경우
                break;
            default:
                surveyList = surveyRepository.findAll(pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if (surveyList.isEmpty()) return nullSurveyList; //검색 결과가 없을 경우 빈 객체 반환
        return surveyList;
    }


    public List<Survey> surveyListfive(){
        List<Survey> surveyList = surveyRepository.findFirst5ByOrderByViewsDesc();
        return surveyList;
    }

    public List<Survey> selectAdmin(){
        List<Survey> list = surveyRepository.findAll();
        return list;
    }

    //회원 관리자 활동내역에 사용 0703
    public List<Survey> selectActive(User userNo){
        List<Survey> list = surveyRepository.findAllByUserNo(userNo);
        return list;
    }
    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<Survey> listSurvey(User userNo){
        List<Survey> list = surveyRepository.findFirst5ByUserNoOrderBySurveyNoDesc(userNo);
        return list;
    }


    public Long max(){
        BigDecimal max = surveyRepository.max();
        Long no = Long.valueOf(String.valueOf(max));
        return no;
    }

    //조아요버튼
    public Survey likeButton(Long id){
        Survey survey = surveyRepository.findById(id).get();
        return survey;
    }


    public void delete(Long Id) {
        surveyRepository.deleteById(Id);
    }




}
