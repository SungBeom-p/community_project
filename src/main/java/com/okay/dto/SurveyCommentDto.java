package com.okay.dto;

import com.okay.Adapt.SurveyCommentAdapt;
import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SurveyCommentDto implements SurveyCommentAdapt {
    private Long id;
    private Survey surveyNo;
    private String name;
    private String content;
    private String regDate;
    private User userNo;

    @Override
    public SurveyComment changeSurveyComment(SurveyCommentDto surveyCommentDto) {
        SurveyComment surveyComment = SurveyComment.builder()
                .id(surveyCommentDto.getId())
                .surveyNo(surveyCommentDto.getSurveyNo())
                .name(surveyCommentDto.getName())
                .content(surveyCommentDto.getContent())
                .regDate(surveyCommentDto.getRegDate())
                .userNo(surveyCommentDto.getUserNo())
                .build();
        return  surveyComment;
    }

    @Override
    public SurveyCommentDto changeSurveyCommentDto(SurveyComment surveyComment) {
        SurveyCommentDto surveyCommentDto = SurveyCommentDto.builder()
                .id(surveyComment.getId())
                .surveyNo(surveyComment.getSurveyNo())
                .name(surveyComment.getName())
                .content(surveyComment.getContent())
                .regDate(surveyComment.getRegDate())
                .userNo(surveyComment.getUserNo())
                .build();
        return surveyCommentDto;
    }
}
