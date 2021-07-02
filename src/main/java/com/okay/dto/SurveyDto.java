package com.okay.dto;

import com.okay.Adapt.SurveyAdapt;
import com.okay.domain.entity.Survey;
import com.okay.domain.entity.User;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SurveyDto implements SurveyAdapt {
    private Long surveyNo;
    private Long views;
    private User userNo;
    private String fileName1;   //이미지이름
    private String fileName2;   //이미지이름
    private Long size1; // 이미지크기
    private Long size2; // 이미지크기
    private Long result1;
    private Long result2;
    private String name;
    private String option1; // 의견 1
    private String option2; // 의견 2
    private String pw;
    private String path;
    private String title;
    private Long hit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Override
    public Survey changeSurvey(SurveyDto surveyDto) {
        Survey survey = Survey.builder()
                .surveyNo(surveyDto.getSurveyNo())
                .views(surveyDto.getViews())
                .userNo(surveyDto.getUserNo())
                .fileName1(surveyDto.getFileName1())
                .fileName2(surveyDto.getFileName2())
                .size1(surveyDto.getSize2())
                .size2(surveyDto.getSize2())
                .path(surveyDto.getPath())
                .option1(surveyDto.getOption1())
                .option2(surveyDto.getOption2())
                .name(surveyDto.getName())
                .result1(surveyDto.getResult1())
                .result2(surveyDto.getResult2())
                .pw(surveyDto.getPw())
                .hit(surveyDto.getHit())
                .title(surveyDto.getTitle())
                .startTime(surveyDto.getStartTime())
                .endTime(surveyDto.getEndTime())
                .build();
        return survey;
    }
    @Override
    public SurveyDto changeSurveyDto(Survey survey) {
        SurveyDto surveyDto = SurveyDto.builder()
                .surveyNo(survey.getSurveyNo())
                .views(survey.getViews())
                .userNo(survey.getUserNo())
                .fileName1(survey.getFileName1())
                .fileName2(survey.getFileName2())
                .size1(survey.getSize1())
                .size2(survey.getSize2())
                .name(survey.getName())
                .path(survey.getPath())
                .hit(survey.getHit())
                .pw(survey.getPw())
                .option1(survey.getOption1())
                .option2(survey.getOption2())
                .title(survey.getTitle())
                .result1(survey.getResult1())
                .result2(survey.getResult2())
                .startTime(survey.getStartTime())
                .endTime(survey.getEndTime())
                .build();
        return surveyDto;

    }
}



