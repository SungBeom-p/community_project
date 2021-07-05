package com.okay.controller;

import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import com.okay.dto.SearchDto;
import com.okay.dto.SurveyCommentDto;
import com.okay.dto.SurveyDto;
import com.okay.service.SurveyCommentService;
import com.okay.service.SurveyService;
import com.okay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@ComponentScan(basePackages = {"com.okay.service"})
@Controller
public class SurveyContoller {
    @Autowired
    SurveyService surveyService;
    @Autowired
    UserService userService;
    @Autowired
    SurveyCommentService surveyCommentService;


    @GetMapping("surveywrite")
    public String surveyGet(HttpServletRequest request) {
        return "surveywrite";
    }

    @PostMapping("surveywrite")
    public String surveyPost(String name, String title, String opinion1, String opinion2, LocalDateTime endTime, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        SurveyDto dto = new SurveyDto();
        dto = SurveyDto.builder()
                .surveyNo(surveyService.max()+1L)
                .userNo(user)
                .name(name)
                .fileName1(opinion1)
                .fileName2(opinion2)
                .pw(user.getUserPw())
                .title(title)
                .hit(0L)
                .size1(0L)
                .size2(0L)
                .result1(0L)
                .result2(0L)
                .views(0L)
                .endTime(endTime)
                .startTime(LocalDateTime.now())
                .build();
        surveyService.create(dto);
        return "surveyBoard";
    }

    @GetMapping("surveyBoard")
    public String listSurvey(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "20") int size, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("surveyNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Survey> surveyList = surveyService.getSurveyList(pageable, searchDto);
        model.addAttribute("surveyList", surveyList);
        return "surveyBoard";
    }

    @GetMapping("surveyHit")
    public String listSurvey(Model model, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("hit").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Survey> surveyList = surveyService.getSurveyList(pageable, searchDto);
        model.addAttribute("surveyList", surveyList);
        return "surveyHit";
    }


    @GetMapping("surveyRead")
    public String readget(HttpServletRequest request, @Param("surveyNo") Long surveyNo, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if (userNo == 2) {
            return "survey";
        }
        Survey survey = surveyService.selectOne(surveyNo);
        SurveyDto surveyDto = SurveyDto.builder()
                .surveyNo(survey.getSurveyNo())
                .userNo(survey.getUserNo())
                .title(survey.getTitle())
                .name(survey.getName())
                .pw(survey.getPw())
                .size1(survey.getSize1())
                .size2(survey.getSize2())
                .fileName2(survey.getFileName2())
                .fileName1(survey.getFileName1())
                .result1(survey.getResult1())
                .result2(survey.getResult2())
                .views(survey.getViews()+1L)
                .hit(survey.getHit())
                .startTime(survey.getStartTime())
                .endTime(survey.getEndTime())
                .build();
        surveyService.update(surveyDto);

        model.addAttribute("survey", surveyService.getSurvey(surveyNo));
        return "surveyRead";
    }

    @GetMapping(path = "/choice")
    public String test2(HttpServletRequest request, Model model,
                        @Param("surveyNo") Long surveyNo){
        HttpSession session = userService.sessionAutowired(request);
        Long id = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(id);
        Survey survey = surveyService.selectOne(surveyNo);

        SurveyDto dto = new SurveyDto();
        dto = dto.changeSurveyDto(survey);
        dto.setViews(survey.getViews()+1L);

        surveyService.update(dto);

        List<SurveyComment> comlist = surveyCommentService.selectAll(survey);
    //    List<SurveyComment> topcomlist = surveyCommentService.selecttopAll(survey);

        model.addAttribute("comment",comlist);
    //    model.addAttribute("top",topcomlist);
        model.addAttribute("name", user.getName()); // 작성자
        return "choice";
    }




    @PostMapping("/choice")
    public String test(@Param("surveyNo")Long surveyNo,String name,
                       String content, String regDate , HttpServletRequest request){
        HttpSession session = userService.sessionAutowired(request);
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        Long id= surveyCommentService.max();
        Survey survey = surveyService.selectOne(surveyNo);
        SurveyCommentDto dto = SurveyCommentDto.builder()
                .id(id)
                .surveyNo(survey)
                .name(name)
                .content(content)
                .regDate(regDate)
                .userNO(user)
                .build();
        surveyCommentService.create(dto);
        return "choice";
    }


}

