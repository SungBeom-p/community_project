package com.okay.Adapt;

import com.okay.domain.entity.Survey;
import com.okay.dto.SurveyDto;

public interface SurveyAdapt {

    Survey changeSurvey(SurveyDto surveyDto);

    SurveyDto changeSurveyDto(Survey survey);
}
