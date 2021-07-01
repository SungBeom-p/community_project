package com.okay.Adapt;

import com.okay.domain.entity.SurveyComment;
import com.okay.dto.SurveyCommentDto;

public interface SurveyCommentAdapt {

    SurveyComment changeSurveyComment(SurveyCommentDto surveyCommentDto);

    SurveyCommentDto changeSurveyCommentDto(SurveyComment surveyComment);
}
