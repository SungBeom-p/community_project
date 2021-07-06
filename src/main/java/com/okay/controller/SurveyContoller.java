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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Resource(name = "uploadSurveyPath")
    String path;

    @GetMapping("surveywrite")
    public String surveyGet(HttpServletRequest request) {
        return "surveywrite";
    }

    @PostMapping("surveywrite")
    public String surveyPost(String name, String title, String opinion1, String opinion2,
                             MultipartFile  uploadFile1, MultipartFile uploadFile2, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        String endtime =LocalDateTime.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String starttime =LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        File file1 = new File(path, uploadFile1.getOriginalFilename()); // 파입 입력
        File file2 = new File(path, uploadFile2.getOriginalFilename());

        try{
            uploadFile1.transferTo(file1);
            uploadFile2.transferTo(file2);
        }catch (Exception e){
            System.out.println("파일 전송 실패");
        }
        SurveyDto dto =SurveyDto.builder()
                .surveyNo(surveyService.max()+1L)
                .userNo(user)
                .name(name)
                .fileName1(uploadFile1.getOriginalFilename())
                .fileName2(uploadFile2.getOriginalFilename())
                .pw(user.getUserPw())
                .title(title)
                .option1(opinion1)
                .option2(opinion2)
                .path(path)
                .hit(0L)
                .result1(0L)
                .result2(0L)
                .views(0L)
                .endTime(endtime)
                .startTime(starttime)
                .build();
        surveyService.create(dto);
        return "redirect:/surveyBoard";
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

//0706 진ㅅㅓㅇ
    @GetMapping("surveyResultBoard")
    public String resultSurvey(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("surveyNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Survey> surveyList = surveyService.getSurveyList(pageable, searchDto);
        model.addAttribute("surveyList", surveyList);
        return "surveyResultBoard";
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
        // 댓글
        List<SurveyComment> comlist = surveyCommentService.selectAll(survey);

        Survey surveyId = surveyService.selectOne(surveyNo);

        SurveyDto dto = new SurveyDto();
        dto = dto.changeSurveyDto(survey);
        dto.setViews(survey.getViews()+1L);

        surveyService.update(dto);

        model.addAttribute("surveyNo",surveyNo);
        model.addAttribute("img", surveyId); // 이미지
        model.addAttribute("comment",comlist);
        model.addAttribute("name", user.getName()); // 작성자
        return "choice";
    }

    @PostMapping("/commentchoice")
    public String choice(HttpServletRequest request, @Param("surveyNo")Long surveyNo,String name, Long id, String content, String regDate){
        HttpSession session = userService.sessionAutowired(request);
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);

        Survey survey = surveyService.selectOne(surveyNo);

        SurveyCommentDto dto = SurveyCommentDto.builder()
                .id(id)
                .userNo(user)
                .surveyNo(survey)
                .name(name)
                .content(content)
                .regDate(regDate)
                .build();
        surveyCommentService.create(dto);
        return "choice";
    }

    @PostMapping("/choice")
    public String test(HttpServletRequest request, @Param("surveyNo")Long surveyNo,String name, Long id, String content, String regDate, String result){
        SurveyDto surveyDto = surveyService.selectOneDto(surveyNo);

        surveyService.update(surveyDto);
        surveyService.addResult(surveyNo,result);

        return "redirect:/surveyResult?surveyNo=" + surveyNo;
    }

    @GetMapping("/surveyResult")
    public String chart(HttpServletRequest request,Model model, @Param("surveyNo")Long surveyNo){
        HttpSession session = request.getSession();
        Long userId = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        Survey survey = surveyService.selectOne(surveyNo);
        // Img
        model.addAttribute("img1",survey.getFileName1());
        model.addAttribute("img2",survey.getFileName2());

        // Graph
        model.addAttribute("result1", survey.getResult1());
        model.addAttribute("result2", survey.getResult2());
        model.addAttribute("total", survey.getResult1() + survey.getResult2());

        // Time
        String startTime = survey.getStartTime();
        model.addAttribute("startTime",startTime);
        String endTime = survey.getEndTime();
        model.addAttribute("endTime", endTime);

        User user = userService.selectOne(userId);
        // modal
        model.addAttribute("name", user.getName());
        model.addAttribute("opnion1",survey.getOption1());
        model.addAttribute("opnion2",survey.getOption2());

        return "surveyResult";
    }

}

