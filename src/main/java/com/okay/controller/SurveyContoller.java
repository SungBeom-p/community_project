package com.okay.controller;

import com.okay.domain.entity.Survey;
import com.okay.domain.entity.SurveyComment;
import com.okay.domain.entity.User;
import com.okay.dto.PostDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDate;
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
 //파일 저장경로
    @Resource(name = "uploadSurveyPath")
    String path;

    LocalDate today = LocalDate.now();
    LocalDate endDay = LocalDate.now().plusWeeks(2);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String now = today.format(formatter);
    String end = endDay.format(formatter);

    @GetMapping("surveyWrite")
    public String surveyGet(HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        model.addAttribute("name",user.getName());
        return "surveywrite";
    }

    @PostMapping("surveyWrite")
    public String surveyPost(String name, String title, String opinion1, String opinion2,
                             MultipartFile  uploadFile1, MultipartFile uploadFile2, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        File file1 = new File(path, uploadFile1.getOriginalFilename()); // 파입 입력
        File file2 = new File(path, uploadFile2.getOriginalFilename());

        try{
            uploadFile1.transferTo(file1);
            uploadFile2.transferTo(file2);
        }catch (Exception e){
            System.out.println("파일 전송 실패");
        }
        SurveyDto dt= new SurveyDto();
        if(dt.getSurveyNo()== null){
            dt.setSurveyNo(0L);
        }else{
            dt.setSurveyNo(surveyService.max()+1L);
        }

        SurveyDto dto =SurveyDto.builder()
                .surveyNo(dt.getSurveyNo())
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
                .endTime(end)
                .startTime(now)
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

//0707
    @GetMapping("surveyResultBoard")
    public String resultSurvey(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("surveyNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Survey> surveyList = surveyService.getSurveyList(pageable, searchDto);
        model.addAttribute("surveyList", surveyList);
        return "surveyResultBoard";
    }

    @GetMapping(path = "/choice")
    public String getSurvey(HttpServletRequest request, Model model,
                        @Param("surveyNo") Long surveyNo){
        HttpSession session = userService.sessionAutowired(request);
        Long id = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(id);
        Survey survey = surveyService.selectOne(surveyNo);
        // 댓글
        //**file1 , 2 둘중 하나만 삽입하고 저장할시에 어떻게 되는가? 진성? 논리적으로 보여봐  **
        List<SurveyComment> comList = surveyCommentService.selectAll(survey);
        Survey surveyEntity = surveyService.selectOne(surveyNo);
        SurveyDto dto = new SurveyDto();
        dto = dto.changeSurveyDto(survey);
        dto.setViews(survey.getViews()+1L);
        //views update 사용
        surveyService.update(dto);

        model.addAttribute("surveyNo",surveyNo);
        model.addAttribute("img", surveyEntity); // 이미지
        model.addAttribute("comment",comList);
        model.addAttribute("name", user.getName()); // 작성자
        return "choice";
    }

    @ResponseBody
    @PostMapping("/commentChoice")
    public ResponseEntity choice(HttpServletRequest request, @Param("surveyNo")Long surveyNo,String name
            ,String content, String regDate){
        HttpSession session = userService.sessionAutowired(request);
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        System.out.println(userNo);
        System.out.println(content);
        System.out.println(regDate);
        System.out.println();
        String flag = "";
        User user = userService.selectOne(userNo);
        Survey survey = surveyService.selectOne(surveyNo);
        SurveyCommentDto dt= new SurveyCommentDto();
        try{
            dt.setId(surveyCommentService.max()+1L);
        }catch (Exception e){
            dt.setId(1L);
        }
        SurveyCommentDto dto = SurveyCommentDto.builder()
                .id(dt.getId())
                .userNo(user)
                .surveyNo(survey)
                .name(name)
                .content(content)
                .regDate(now)
                .build();
        surveyCommentService.create(dto);
        flag = "true";
        return ResponseEntity.ok(flag);
    }

    //0708 변화점
    @ResponseBody
    @PostMapping("/commentResultChoice")
    public ResponseEntity resultChoice(HttpServletRequest request, @Param("surveyNo")Long surveyNo,String name, String content, String regDate){
        HttpSession session = userService.sessionAutowired(request);
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        Survey survey = surveyService.selectOne(surveyNo);
        String flag = "";
        SurveyCommentDto dto = SurveyCommentDto.builder()
                .id(surveyCommentService.max()+1L)
                .userNo(user)
                .surveyNo(survey)
                .name(name)
                .content(content)
                .regDate(regDate)
                .build();
        surveyCommentService.create(dto);
        flag = "true";
        return ResponseEntity.ok(flag);
    }


    @PostMapping("/choice")
    public String test(@Param("surveyNo")Long surveyNo,String result){
        surveyService.addResult(surveyNo,result);
        return "redirect:/surveyResult?surveyNo=" + surveyNo;
    }

    @GetMapping("/surveyResult")
    public String chart(HttpServletRequest request,Model model,
                        @Param("surveyNo")Long surveyNo){
        HttpSession session = request.getSession();
        Long userId = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        Survey survey = surveyService.selectOne(surveyNo);
        List<SurveyComment> comList = surveyCommentService.selectAll(survey);
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
        // modal , user.name용도 아직 미정
        model.addAttribute("img",survey);
        model.addAttribute("name", user.getName());
        model.addAttribute("opnion1",survey.getOption1());
        model.addAttribute("opnion2",survey.getOption2());
        model.addAttribute("comment",comList);

        return "surveyResult";
    }

    //조아요 0708
    @ResponseBody
    @PostMapping("hitSend")
    public ResponseEntity postLike(Long surveyNo){
        Survey survey = surveyService.likeButton(surveyNo);
        SurveyDto surveyDto = new SurveyDto();
        surveyDto = surveyDto.changeSurveyDto(survey);
        surveyDto.setHit(survey.getHit()+1L);
        surveyService.update(surveyDto);
        return ResponseEntity.ok(String.valueOf(surveyDto.getHit()));
    }

    @ResponseBody
    @PostMapping("resultHitrSend")
    public ResponseEntity postResultLike(Long surveyNo){
        Survey survey = surveyService.likeButton(surveyNo);
        SurveyDto surveyDto = new SurveyDto();
        surveyDto = surveyDto.changeSurveyDto(survey);
        surveyDto.setHit(survey.getHit()+1L);
        surveyService.update(surveyDto);
        return ResponseEntity.ok(String.valueOf(surveyDto.getHit()));
    }

    // 7.8
    @PostMapping("/surveyRemove")
    public String delete(Long surveyNo) {
        surveyCommentService.deleteAll(surveyNo);
        surveyService.delete(surveyNo);
        return "redirect:/surveyBoard";
    }

}

