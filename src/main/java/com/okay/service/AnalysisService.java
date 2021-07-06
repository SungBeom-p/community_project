//package com.okay.service;
//
//import com.okay.domain.entity.*;
//import com.okay.domain.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@org.springframework.stereotype.Service
//public class AnalysisService extends Service {
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    CommentRepository commentRepository;
//
//    @Autowired
//    SurveyRepository surveyRepository;
//
//    @Autowired
//    SurveyCommentRepository surveyCommentRepository;
//
//    public int[] calculatePost(Long id){ // User의 글 작성 추이
//        Optional<User> temp = userRepository.findById(id);
//
//
//        LocalDateTime today = LocalDateTime.now();
//
//        List<Post> allByUserNo = postRepository.findAllByUserNo(temp.get());
//        List<Post> list0 = new ArrayList<>();
//        List<Post> list1 = new ArrayList<>();
//        List<Post> list2 = new ArrayList<>();
//        List<Post> list3 = new ArrayList<>();
//        List<Post> list4 = new ArrayList<>();
//        List<Post> list5 = new ArrayList<>();
//        List<Post> list6 = new ArrayList<>();
//        List<Post> list7 = new ArrayList<>();
//        List<Post> list8 = new ArrayList<>();
//
//        allByUserNo.forEach(i->{
//            if(i.getRegDate().compareTo(today.minusWeeks(8))<0){ // 8주전
//                list8.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(7))<0){ // 7주전
//                list7.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(6))<0){ // 6주전
//                list6.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(5))<0) { // 5주전
//                list5.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(4))<0) { // 4주전
//                list4.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(3))<0) { // 3주전
//                list3.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(2))<0) { // 2주전
//                list2.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(1))<0) { // 1주전
//                list1.add(i);
//            }else if(i.getRegDate().compareTo(today.plusSeconds(1))<0){ // Now
//                list0.add(i);
//            }
//        });
//        int arr[] = new int[]{list8.size(), list7.size(), list6.size(), list5.size(), list4.size(), list3.size(), list2.size(),
//        list1.size(), list0.size()};
//        return arr;
//    }
//
//    public int[] calculateComment(Long id){ // User의 댓글 작성 추이
//        Optional<User> temp = userRepository.findById(id);
//
//
//        LocalDateTime today = LocalDateTime.now();
//
//        List<Comment> allByUserNo = commentRepository.findAllByUserNo(temp.get());
//
//        List<Comment> list0 = new ArrayList<>();
//        List<Comment> list1 = new ArrayList<>();
//        List<Comment> list2 = new ArrayList<>();
//        List<Comment> list3 = new ArrayList<>();
//        List<Comment> list4 = new ArrayList<>();
//        List<Comment> list5 = new ArrayList<>();
//        List<Comment> list6 = new ArrayList<>();
//        List<Comment> list7 = new ArrayList<>();
//        List<Comment> list8 = new ArrayList<>();
//
//        allByUserNo.forEach(i->{
//            if(i.getRegDate().compareTo(today.minusWeeks(8))<0){ // 8주전
//                list8.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(7))<0){ // 7주전
//                list7.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(6))<0){ // 6주전
//                list6.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(5))<0) { // 5주전
//                list5.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(4))<0) { // 4주전
//                list4.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(3))<0) { // 3주전
//                list3.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(2))<0) { // 2주전
//                list2.add(i);
//            }else if(i.getRegDate().compareTo(today.minusWeeks(1))<0) { // 1주전
//                list1.add(i);
//            }else if(i.getRegDate().compareTo(today.plusSeconds(1))<0){ // Now
//                list0.add(i);
//            }
//        });
//        int arr[] = new int[]{list8.size(), list7.size(), list6.size(), list5.size(), list4.size(), list3.size(), list2.size(),
//                list1.size(), list0.size()};
//        return arr;
//    }
//
//    public int[] calculateAll(Long id){ // 활동 분석
//        Optional<User> temp = userRepository.findById(id);
//
//        List<Post> post = postRepository.findAllByUserNo(temp.get());
//        List<Comment> comment = commentRepository.findAllByUserNo(temp.get());
//        List<Survey> survey = surveyRepository.findAllByUserNo(temp.get());
//        List<SurveyComment> surveyComment= surveyCommentRepository.findAllByUserNo(temp.get());
//        int arr[] = new int[]{post.size(),comment.size(), survey.size(), surveyComment.size()};
//        return arr;
//    }
//
//
//}
