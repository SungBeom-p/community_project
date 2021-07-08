
package com.okay.service;

import com.okay.domain.entity.*;
import com.okay.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@org.springframework.stereotype.Service
public class AnalysisService extends Service {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    SurveyCommentRepository surveyCommentRepository;


    public int[] calculatePost(Long id){ // User의 글 작성 추이
        Optional<User> temp = userRepository.findById(id);

        LocalDate today = LocalDate.now();

        List<Post> postList = postRepository.findAllByUserNo(temp.get());
        List<Survey> surveyList = surveyRepository.findAllByUserNo(temp.get());
        List<LocalDate> postRegDateList = new ArrayList<>();
        List<LocalDate> surveyRegDateList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        postList.forEach(i->{
            LocalDate date = LocalDate.parse(i.getRegDate(), formatter);
            postRegDateList.add(date);
        });
        surveyList.forEach(i->{
            LocalDate date = LocalDate.parse(i.getStartTime(), formatter);
            surveyRegDateList.add(date);
        });

        // 배열 초기화
        int[] dateAnalysis = {
                0,0,0,0,0,0,0,0,0
        };

        // regDate 정렬
        postRegDateList.forEach(i->{
            if(i.compareTo(today.minusWeeks(8))<0){ // 8주전
                dateAnalysis[0]++;
            }else if(i.compareTo(today.minusWeeks(7))<0){ // 7주전
                dateAnalysis[1]++;
            }else if(i.compareTo(today.minusWeeks(6))<0){ // 6주전
                dateAnalysis[2]++;
            }else if(i.compareTo(today.minusWeeks(5))<0) { // 5주전
                dateAnalysis[3]++;
            }else if(i.compareTo(today.minusWeeks(4))<0) { // 4주전
                dateAnalysis[4]++;
            }else if(i.compareTo(today.minusWeeks(3))<0) { // 3주전
                dateAnalysis[5]++;
            }else if(i.compareTo(today.minusWeeks(2))<0) { // 2주전
                dateAnalysis[6]++;
            }else if(i.compareTo(today.minusWeeks(1))<0) { // 1주전
                dateAnalysis[7]++;
            }else{ // Now
                dateAnalysis[8]++;
            }
        });

        surveyRegDateList.forEach(i->{
            if(i.compareTo(today.minusWeeks(8))<0){ // 8주전
                dateAnalysis[0]++;
            }else if(i.compareTo(today.minusWeeks(7))<0){ // 7주전
                dateAnalysis[1]++;
            }else if(i.compareTo(today.minusWeeks(6))<0){ // 6주전
                dateAnalysis[2]++;
            }else if(i.compareTo(today.minusWeeks(5))<0) { // 5주전
                dateAnalysis[3]++;
            }else if(i.compareTo(today.minusWeeks(4))<0) { // 4주전
                dateAnalysis[4]++;
            }else if(i.compareTo(today.minusWeeks(3))<0) { // 3주전
                dateAnalysis[5]++;
            }else if(i.compareTo(today.minusWeeks(2))<0) { // 2주전
                dateAnalysis[6]++;
            }else if(i.compareTo(today.minusWeeks(1))<0) { // 1주전
                dateAnalysis[7]++;
            }else{ // Now
                dateAnalysis[8]++;
            }
        });
        return dateAnalysis;
    }


    public int[] calculateComment(Long id){ // User의 댓글 작성 추이
        Optional<User> temp = userRepository.findById(id);

        LocalDate today = LocalDate.now();

        List<Comment> commetList = commentRepository.findAllByUserNo(temp.get());
        List<SurveyComment> surveyCommentList = surveyCommentRepository.findAllByUserNo(temp.get());

        List<LocalDate> commentRegDateList = new ArrayList<>();
        List<LocalDate> surveyCommentRegDateList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        commetList.forEach(i->{
            LocalDate date = LocalDate.parse(i.getRegDate(), formatter);
            commentRegDateList.add(date);
        });

        surveyCommentList.forEach(i->{
            LocalDate date = LocalDate.parse(i.getRegDate(), formatter);
            surveyCommentRegDateList.add(date);
        });

        // 배열 초기화
        int[] dateAnalysis = {
                0,0,0,0,0,0,0,0,0
        };

        // regDate 정렬
        commentRegDateList.forEach(i->{
            if(i.compareTo(today.minusWeeks(8))<0){ // 8주전
                dateAnalysis[0]++;
            }else if(i.compareTo(today.minusWeeks(7))<0){ // 7주전
                dateAnalysis[1]++;
            }else if(i.compareTo(today.minusWeeks(6))<0){ // 6주전
                dateAnalysis[2]++;
            }else if(i.compareTo(today.minusWeeks(5))<0) { // 5주전
                dateAnalysis[3]++;
            }else if(i.compareTo(today.minusWeeks(4))<0) { // 4주전
                dateAnalysis[4]++;
            }else if(i.compareTo(today.minusWeeks(3))<0) { // 3주전
                dateAnalysis[5]++;
            }else if(i.compareTo(today.minusWeeks(2))<0) { // 2주전
                dateAnalysis[6]++;
            }else if(i.compareTo(today.minusWeeks(1))<0) { // 1주전
                dateAnalysis[7]++;
            }else{ // Now
                dateAnalysis[8]++;
            }
        });

        surveyCommentRegDateList.forEach(i->{
            if(i.compareTo(today.minusWeeks(8))<0){ // 8주전
                dateAnalysis[0]++;
            }else if(i.compareTo(today.minusWeeks(7))<0){ // 7주전
                dateAnalysis[1]++;
            }else if(i.compareTo(today.minusWeeks(6))<0){ // 6주전
                dateAnalysis[2]++;
            }else if(i.compareTo(today.minusWeeks(5))<0) { // 5주전
                dateAnalysis[3]++;
            }else if(i.compareTo(today.minusWeeks(4))<0) { // 4주전
                dateAnalysis[4]++;
            }else if(i.compareTo(today.minusWeeks(3))<0) { // 3주전
                dateAnalysis[5]++;
            }else if(i.compareTo(today.minusWeeks(2))<0) { // 2주전
                dateAnalysis[6]++;
            }else if(i.compareTo(today.minusWeeks(1))<0) { // 1주전
                dateAnalysis[7]++;
            }else{ // Now
                dateAnalysis[8]++;
            }
        });
        return dateAnalysis;
    }



    public int[] calculateAll(Long id){ // 활동 분석
        Optional<User> temp = userRepository.findById(id);
        List<Post> post = postRepository.findAllByUserNo(temp.get());
        List<Comment> comment = commentRepository.findAllByUserNo(temp.get());
        List<Survey> survey = surveyRepository.findAllByUserNo(temp.get());
        List<SurveyComment> surveyComment= surveyCommentRepository.findAllByUserNo(temp.get());
        int arr[] = new int[]{post.size(),comment.size(), survey.size(), surveyComment.size()};
        return arr;
    }


    public int[] adminCalculateAll(){ // 관리자의 페이지 분석
        // 공지사항
        List<Post> noticeList = postRepository.findAllByCategory("on");
        // 고객센터
        List<Post> serviceList = postRepository.findAllByCategory("service");
        // 자유 게시판
        List<Post> postList = postRepository.findAllByCategory("off");
        // 투표
        List<Survey> surveyList = surveyRepository.findAll();
        // 댓글 리스트
        List<Comment> commentList = commentRepository.findAll();
        // 투표 댓글 리스트
        List<SurveyComment> surveyCommentList = surveyCommentRepository.findAll();

        int[] arr = new int[]{
                noticeList.size(),serviceList.size(),postList.size(),surveyList.size(),commentList.size()+serviceList.size()
        };
        return arr;
    }

    public int[] adminCalculateRegister(){ // 관리자용 회원가입 수치

        List<User> userList = userRepository.findAll();

        LocalDate today = LocalDate.now();

        List<LocalDate> userRegDateList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        AtomicInteger cnt = new AtomicInteger();
        userList.forEach(i->{
            LocalDate date = LocalDate.parse(i.getRegDate(), formatter);
            userRegDateList.add(date);

            System.out.println(userRegDateList.get(cnt.get()));
            cnt.addAndGet(1);
        });


        // 배열 초기화
        int[] dateAnalysis = {
                0,0,0,0,0,0,0,0,0
        };

        // regDate 정렬
        userRegDateList.forEach(i->{
            if(i.compareTo(today.minusWeeks(8))<0){ // 8주전
                dateAnalysis[0]++;
            }else if(i.compareTo(today.minusWeeks(7))<0){ // 7주전
                dateAnalysis[1]++;
            }else if(i.compareTo(today.minusWeeks(6))<0){ // 6주전
                dateAnalysis[2]++;
            }else if(i.compareTo(today.minusWeeks(5))<0) { // 5주전
                dateAnalysis[3]++;
            }else if(i.compareTo(today.minusWeeks(4))<0) { // 4주전
                dateAnalysis[4]++;
            }else if(i.compareTo(today.minusWeeks(3))<0) { // 3주전
                dateAnalysis[5]++;
            }else if(i.compareTo(today.minusWeeks(2))<0) { // 2주전
                dateAnalysis[6]++;
            }else if(i.compareTo(today.minusWeeks(1))<0) { // 1주전
                dateAnalysis[7]++;
            }else{ // Now
                dateAnalysis[8]++;
            }
        });
        return dateAnalysis;
    }

    public int[] adminCalculateFile(){ // 파일이 있는 게시글과 파일이 없는 게시글 비교
        List<Post> postList = postRepository.findAll();
        List<Survey> surveyList = surveyRepository.findAll();

        List<String> fileList = new ArrayList<>();
        postList.forEach(i->{
            if(i.getFileName().equals("")){

            }else{
                fileList.add(i.getFileName());
            }
        });
        surveyList.forEach(i->{
            if(i.getFileName1().equals("") && i.getFileName2().equals("")){

            }else{
                fileList.add(i.getFileName1());
            }
        });

        int[] data = new int[]{(postList.size()+surveyList.size()) - fileList.size(), fileList.size()};
        return data;
    }



}