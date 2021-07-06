//package com.okay.controller;
//
//import com.okay.service.AnalysisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//@Controller
//public class AnalysisController {
//    @Autowired
//    AnalysisService analysisService;
//
//    @GetMapping("/about")
//    public String about(HttpServletRequest request, Model model){
//        HttpSession session = request.getSession();
//        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
//
//        int[] arr = analysisService.calculatePost(userNo);
//        int[] comment = analysisService.calculateComment(userNo);
//        int[] all = analysisService.calculateAll(userNo);
//        model.addAttribute("week", arr);
//        model.addAttribute("comment",comment);
//        model.addAttribute("all", all);
//        return "about";
//    }
//
//
//}
