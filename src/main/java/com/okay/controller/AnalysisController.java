package com.okay.controller;

import com.okay.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    @GetMapping("/about")
    public String about(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if(userNo>2){
            // User의 데이터 분석
            int[] allCnt = analysisService.calculateAll(userNo);
            int[] postCnt = analysisService.calculatePost(userNo);
            int[] commentCnt = analysisService.calculateComment(userNo);
            model.addAttribute("post", postCnt);
            model.addAttribute("comment",commentCnt);
            model.addAttribute("all", allCnt);

            return "about";
        }else{
            // 관리자용 데이터 분석
            int[] adminAll = analysisService.adminCalculateAll();
            int[] adminRegisterAll = analysisService.adminCalculateRegister();
            int[] file = analysisService.adminCalculateFile();


            model.addAttribute("adminAll", adminAll);
            model.addAttribute("registerAll", adminRegisterAll);
            model.addAttribute("file", file);
            return "adminAbout";
        }


    }


}