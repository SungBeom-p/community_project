package com.okay.controller;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.dto.PostDto;
import com.okay.service.CommentService;
import com.okay.service.PostService;
import com.okay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ComponentScan(basePackages = {"com.okay.service"})
@Controller
public class HelloController {
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    PostService postService;


    @GetMapping("/")
    public String main(HttpServletRequest request){ // 첫 진입시 Main 화면
        HttpSession session = userService.sessionAutowired(request);
        return "main";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        //session.setAttribute("id", "value");
        //String userid = (String)session.getAttribute("userId"); //리턴 타입은 Object
        //session.removeAttribute("userid");
        session.invalidate();// 세션의 모든 속성을 삭제
    return "redirect:/";
    }

    @GetMapping("/a")
    public void deletee(){
        System.out.println("come on!!!!!!!");
        PostDto dto = new PostDto();
        Long pno = 358L;
       Post post =  postService.selectOne(pno);
       Comment delcom = new Comment();
       dto = dto.changePostDto(post);
        if(pno == 358L){
        System.out.println("first delete" + delcom);
        System.out.println("last delete" +dto);
       commentService.deletepost(post);
       postService.removed(dto);
        }

    }



}
