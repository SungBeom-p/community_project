package com.okay.controller;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.dto.PostDto;
import com.okay.dto.UserDto;
import com.okay.service.CommentService;
import com.okay.service.PostService;
import com.okay.service.UserService;
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
                    .userNo(dto.getUserNo())
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
        model.addAttribute("user", userService.selectOne(userNo));
        return "mypage";
    }

    @GetMapping("/userEdit") // 체크용
    public String getmypageedit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
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
    @GetMapping("/myadmin")
    public String adminmenu(HttpServletRequest request, Model model) {
        System.out.println("admin in!!!~~~~~~~");
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo != 1){
            return "gallery";
        }

        model.addAttribute("post", postService.selectAll().size());
        model.addAttribute("count", userService.allUser().size());
        model.addAttribute("user", userService.allUser());
        return "myadmin";
    }

    @GetMapping("/admindelete")
    public String deleteadmin(HttpServletRequest request,Long userNo){
        User useren = userService.selectOne(userNo);
        List<Post> postup = postService.postupdate(useren);
        postup.stream().forEach(i->{
            Post pooup = postService.selectOne(i.getPostNo());
            PostDto dto = new PostDto();
            dto.setPostNo(pooup.getPostNo());
            dto.setUserNo(userService.selectOne(2L));
            dto.setPw(pooup.getPw());
            dto.setTitle(pooup.getTitle());
            dto.setContent(pooup.getContent());
            dto.setCategory(pooup.getCategory());
            dto.setName(pooup.getName());
            dto.setModDate(pooup.getModDate());
            dto.setRegDate(pooup.getRegDate());
            dto.setViews(pooup.getViews());
            postService.update(dto);
        });
        userService.remove(userNo);
        return"mypage";
    }

}
