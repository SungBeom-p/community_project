package com.okay.controller;

import com.okay.service.CommentService;
import com.okay.service.PostService;
import com.okay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
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



}
