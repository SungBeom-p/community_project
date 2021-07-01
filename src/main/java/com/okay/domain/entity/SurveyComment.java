package com.okay.domain.entity;

import com.okay.Adapt.SurveyCommentAdapt;
import com.okay.dto.SurveyCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "survey_comment")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SurveyComment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    @ManyToOne(cascade = CascadeType.DETACH)
    Survey surveyNo;

    String content;
    @Column(name = "reg_date")
    String regDate;

}
