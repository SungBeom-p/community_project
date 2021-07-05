package com.okay.controller;

import com.okay.domain.entity.*;
import com.okay.dto.*;
import com.okay.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@ComponentScan(basePackages = {"com.okay.service"})
@Controller
public class UserController extends Exception{

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    SurveyCommentService surveyCommentService;
    @Autowired
    SurveyService surveyService;


    @GetMapping("/login")
    public String login(){ // 로그인 HTML 이동
        return "login";
    }

    @PostMapping("/login")
    public String postlogin(String Id, String Pw, HttpServletRequest request, Model model){ // login session Check
        UserDto dto = userService.login(Id,Pw); // 로그인 체크
        HttpSession session = request.getSession();
        if(dto.getUserNo()==2L){
            session.setAttribute("userId",2);
            session.setAttribute("loginCheck","false");
            return "login";
        }else if(dto.getUserNo()==1L){
            session.setAttribute("userId", 1);
            return "main";
        }else{
            session.setAttribute("userId",dto.getUserNo());
            return "main";
        }
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request){ // 회원가입 HTML 이동
        userService.sessionAutowired(request);
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(UserDto dto){ // 회원가입 Server Data 전송
            userService.login(dto.getUserId(), dto.getUserPw());
            UserDto userDto = UserDto.builder()
                    .userNo(userService.max()+1L)
                    .userId(dto.getUserId())
                    .userPw(dto.getUserPw())
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .gender(dto.getGender())
                    .regDate(LocalDateTime.now())
                    .build();
            userService.create(userDto);
            return "main";
    }

    @GetMapping("/userInfoCheck")
    public String userInfoCheck1(){
        return "userInfoCheck";
    }

    @PostMapping("/userInfoCheck")
    public String userInfoCheck2(){
        return "register";
    }


    @GetMapping("/mypage") // 체크용
    public String mypages(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo == 2){
            return "redirect:/gallery";
        }
        model.addAttribute("user", userService.selectOne(userNo));
        return "mypage";
    }

    @GetMapping("/userEdit") // 체크용
    public String getmypageedit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo == 2){
            return "redirect:/gallery";
        }
        model.addAttribute("user", userService.selectOne(userNo));
        return "userEdit";
    }

    @PostMapping("/userEdit") // 체크용
    public String postmypageedit(HttpServletRequest request, UserDto dto) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        if (user != null) {
            dto.setUserNo(user.getUserNo());
            dto.setRegDate(LocalDateTime.now());
        }

        userService.update(dto);

        return "redirect:/mypage";
    }
    @GetMapping("/myadmin") //관리자일경우만
    public String adminmenu(HttpServletRequest request, Model model) {
        System.out.println("admin in!!!~~~~~~~");
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo != 1){
            return "redirect:/gallery";
        }
        model.addAttribute("comment",commentService.commentsize().size() +surveyCommentService.selectsize().size());
        model.addAttribute("post", postService.selectAll().size() + surveyService.selectadmin().size());
        model.addAttribute("count", userService.allUser().size());
        model.addAttribute("user", userService.allUser());
        return "myadmin";
    }
    @GetMapping("/myactive") //회원 || 관리자 일경우만
    public String activemenu(HttpServletRequest request, Model model) {
        System.out.println("mypage active in!!!~~~~~~~");
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo == 2 ){
            return "redirect:/gallery";
        }
        User user = userService.selectOne(userNo);

        model.addAttribute("postlist",postService.listpost(user));
        model.addAttribute("surveylist",surveyService.listsurvey(user));
        model.addAttribute("commentlist",commentService.listcomment(user));
        model.addAttribute("svcommentlist",surveyCommentService.listcomment(user));

        model.addAttribute("comment",commentService.activeommentsize(user).size() +surveyCommentService.selectcommentsize(user).size());
        model.addAttribute("post", postService.postupdate(user).size() + surveyService.selectActive(user).size());
        //model.addAttribute("user", userService.allUser());
        return "myactive";
    }

    @GetMapping("/admindelete")
    public String deleteadmin(HttpServletRequest request,Long userNo){
        HttpSession session = request.getSession();
        Long check = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(check != 1){
            return "redirect:/gallery";
        }
        User useren = userService.selectOne(userNo);
        if(userNo != 2){
        //0705 회원삭제시 post의 userNo를 2로 변경
        List<Post> postup = postService.postupdate(useren);
        postup.stream().forEach(i->{
            Post pooup = postService.selectOne(i.getPostNo());
            PostDto dto = new PostDto();
                    //        PostDto.builder()
            //        .postNo(pooup.getPostNo())
            //        .userNo(userService.selectOne(2L))
            //        .content(pooup.getContent())
            //        .views(pooup.getViews())
            //        .name(pooup.getName())
            //        .modDate(pooup.getModDate())
            //        .regDate(pooup.getRegDate())
            //        .category(pooup.getCategory())
            //        .fileName(pooup.getFileName())
            //        .pw(pooup.getPw())
            //        .title(pooup.getTitle())
            //        .build();
            dto = dto.changePostDto(pooup);
            dto.setUserNo(userService.selectOne(2L));
            postService.update(dto);
        });
        //0705 회원삭제시 comment의 userNo를 2로 변경
        List<Comment> commentlist = commentService.activeommentsize(useren);
        commentlist.forEach(i->{
            Comment comment = commentService.selectOne(i.getCommentNo());
            CommentDto comentdto = new CommentDto();
                    //        CommentDto.builder()
            //        .commentNo(comment.getCommentNo())
            //        .userNo(userService.selectOne(2L))
            //        .postNo(comment.getPostNo())
            //        .content(comment.getContent())
            //        .name(comment.getName())
            //        .pw(comment.getPw())
            //        .regDate(comment.getRegDate())
            //            .build();
           comentdto = comentdto.changeCommentDto(comment);
            comentdto.setUserNo(userService.selectOne(2L));
            commentService.update(comentdto);
        });

        //survey의 userNo를 2로 변경
        List<Survey> surveylist = surveyService.selectActive(useren);
        surveylist.forEach(i->{
            Survey survey = surveyService.selectOne(i.getSurveyNo());
            SurveyDto surveyDto = new SurveyDto();
                    //        SurveyDto.builder()
            //        .surveyNo(survey.getSurveyNo())
            //        .userNo(userService.selectOne(2L))
            //        .path(survey.getPath())
            //        .name(survey.getName())
            //        .title(survey.getTitle())
            //        .pw(survey.getPw())
            //        .fileName2(survey.getFileName2())
            //        .fileName1(survey.getFileName1())
            //        .result2(survey.getResult2())
            //        .result1(survey.getResult1())
            //        .startTime(survey.getStartTime())
            //        .endTime(survey.getEndTime())
            //        .option1(survey.getOption1())
            //        .option2(survey.getOption2())
            //        .size1(survey.getSize1())
            //        .size2(survey.getSize2())
            //        .views(survey.getViews())
            //        .hit(survey.getHit())
            //        .build();
                surveyDto = surveyDto.changeSurveyDto(survey);
            surveyDto.setUserNo(userService.selectOne(2L));
            surveyService.update(surveyDto);
        });
        //surveyComment의 userNo를 2로 변경
        List<SurveyComment> selectcommentsize = surveyCommentService.selectcommentsize(useren);
        selectcommentsize.forEach(i->{
            SurveyComment surveycomment = surveyCommentService.selectOne(i.getId());
            SurveyCommentDto surveyCommentDto = new SurveyCommentDto();
            //SurveyCommentDto.builder()
            //        .id(surveycomment.getId())
            //        .userNO(userService.selectOne(2L))
            //        .name(surveycomment.getName())
            //        .surveyNo(surveycomment.getSurveyNo())
            //        .regDate(surveycomment.getRegDate())
            //        .content(surveycomment.getContent())
            //        .build();
            surveyCommentDto = surveyCommentDto.changeSurveyCommentDto(surveycomment);
            surveyCommentDto.setUserNO(userService.selectOne(2L));
            surveyCommentService.update(surveyCommentDto);
        });
        }
        userService.remove(userNo);
        return"mypage";
    }

}
