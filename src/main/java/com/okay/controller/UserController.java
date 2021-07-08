package com.okay.controller;

import com.okay.domain.entity.*;
import com.okay.dto.*;
import com.okay.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    @GetMapping("/")
    public String main(HttpServletRequest request){ // 첫 진입시 Main 화면
        HttpSession session = userService.sessionAutowired(request);
        return "main";
    }
    //세션제거
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();// 세션의 모든 속성을 삭제
        return "redirect:/";
    }


    @ResponseBody
    @PostMapping("/userIdCheck")
    public ResponseEntity postCheckId(String userId) throws Exception {
        List<User> check = userService.idCheck(userId);
        String flag="";
        if(check.size() == 0){
            flag="true";

        }
        else {
            flag="false";
        }
        return ResponseEntity.ok(flag);
    }

    @GetMapping("/login")
    public String login(){ // 로그인 HTML 이동
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(String Id, String Pw, HttpServletRequest request, Model model){ // login session Check
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
        //userService.login(dto.getUserId(), dto.getUserPw()) ;
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = today.format(formatter);

        //0707 수정 시작
        UserDto dt= new UserDto();
        if(dt.getUserNo()== null){
            dt.setUserNo(0L);
        }else{
            dt.setUserNo(userService.max()+1L);
        }
        //0707 수정 끝
        UserDto userDto = UserDto.builder()
                .userNo(dt.getUserNo())
                .userId(dto.getUserId())
                .userPw(dto.getUserPw())
                .name(dto.getName())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .regDate(now)
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
        return "redirect:/register";
    }


    @GetMapping("/mypage") // 체크용
    public String firstMypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo == 2){
            return "redirect:/gallery";
        }
        model.addAttribute("user", userService.selectOne(userNo));
        return "mypage";
    }

    @GetMapping("/userEdit") // 개인 프로필 수정페이지
    public String getMypageEdit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo == 2){
            return "redirect:/gallery";
        }
        model.addAttribute("user", userService.selectOne(userNo));
        return "userEdit";
    }

    @PostMapping("/userEdit") // 체크용
    public String postMypageEdit(HttpServletRequest request, UserDto dto) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        if (user != null) {
            dto.setUserNo(user.getUserNo());
            dto.setRegDate(user.getRegDate());
        }
        userService.update(dto);
        return "redirect:/mypage";
    }

    @GetMapping("/myadmin") //관리자일경우만
    public String adminMenu(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo != 1){
            return "redirect:/gallery";
        }
        model.addAttribute("comment",commentService.commentSize().size() +surveyCommentService.selectSize().size());
        model.addAttribute("post", postService.selectAll().size() + surveyService.selectAdmin().size());
        model.addAttribute("count", userService.allUser().size());
        model.addAttribute("user", userService.allUser());
        return "myadmin";
    }
    @GetMapping("/myactive") //회원 || 관리자 일경우만
    public String activeMenu(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo == 2 ){
            return "redirect:/gallery";
        }
        User user = userService.selectOne(userNo);

        model.addAttribute("postlist",postService.listPost(user));
        model.addAttribute("surveylist",surveyService.listSurvey(user));
        model.addAttribute("commentlist",commentService.listComment(user));
        model.addAttribute("svcommentlist",surveyCommentService.listComment(user));

        model.addAttribute("comment",commentService.activeCommentSize(user).size() +surveyCommentService.selectCommentSize(user).size());
        model.addAttribute("post", postService.postUpdate(user).size() + surveyService.selectActive(user).size());
        return "myactive";
    }

    @GetMapping("/admindelete")
    public String deleteAdmin(HttpServletRequest request,Long userNo){
        HttpSession session = request.getSession();
        Long check = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(check != 1){
            return "redirect:/gallery";
        }

        User useren = userService.selectOne(userNo);
        if(userNo != 2){
        //0705 회원삭제시 post의 userNo를 2로 변경
        List<Post> postUp = postService.postUpdate(useren);
            postUp.stream().forEach(i->{
            Post pooup = postService.selectOne(i.getPostNo());
            PostDto dto = new PostDto();
            dto = dto.changePostDto(pooup);
            dto.setUserNo(userService.selectOne(2L));
            postService.update(dto);
        });
        //0705 회원삭제시 comment의 userNo를 2로 변경
        List<Comment> commentList = commentService.activeCommentSize(useren);
            commentList.forEach(i->{
            Comment comment = commentService.selectOne(i.getCommentNo());
            CommentDto comentdto = new CommentDto();

            comentdto = comentdto.changeCommentDto(comment);
            comentdto.setUserNo(userService.selectOne(2L));
            commentService.update(comentdto);
        });
        //survey의 userNo를 2로 변경
        List<Survey> surveyList = surveyService.selectActive(useren);
            surveyList.forEach(i->{
            Survey survey = surveyService.selectOne(i.getSurveyNo());
            SurveyDto surveyDto = new SurveyDto();
            surveyDto = surveyDto.changeSurveyDto(survey);
            surveyDto.setUserNo(userService.selectOne(2L));
            surveyService.update(surveyDto);
        });
        //surveyComment의 userNo를 2로 변경
        List<SurveyComment> selectCommentSize = surveyCommentService.selectCommentSize(useren);
            selectCommentSize.forEach(i->{
            SurveyComment surveycomment = surveyCommentService.selectOne(i.getId());
            SurveyCommentDto surveyCommentDto = new SurveyCommentDto();
            surveyCommentDto = surveyCommentDto.changeSurveyCommentDto(surveycomment);
            surveyCommentDto.setUserNo(userService.selectOne(2L));
            surveyCommentService.update(surveyCommentDto);
        });
        }
        userService.remove(userNo);
        return"mypage";
    }
}
