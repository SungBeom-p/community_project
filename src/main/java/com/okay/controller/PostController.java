package com.okay.controller;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.Survey;
import com.okay.domain.entity.User;
import com.okay.dto.CommentDto;
import com.okay.dto.Paging;
import com.okay.dto.PostDto;
import com.okay.dto.SearchDto;
import com.okay.service.CommentService;
import com.okay.service.PostService;
import com.okay.service.SurveyService;
import com.okay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    SurveyService surveyService;


    @GetMapping("/search")
    public String search(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="20") int size, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> searchList = postService.getSearchList(pageable, searchDto);
        model.addAttribute("searchList", searchList);
        return "search";
    }

    //공지사항 리스트 20210702
    @GetMapping("/noticeBoard")
    public String noticeboard(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="20") int size, String category, SearchDto searchDto) {
        category ="on";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> noticelist = postService.getNoticeList(pageable, category, searchDto);
        model.addAttribute("noticelist", noticelist);
        return "noticeBoard";
    }
    @GetMapping("/board")
    public String board(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="20") int size, String category, SearchDto searchDto) {
        category = "off";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> postList = postService.getPostList(pageable, category, searchDto);
        model.addAttribute("postList", postList);
        return "board";
    }

    @GetMapping("/gallery")
    public String gallarylist(Model model) {
        System.out.println("컨트롤러 출력되나??");
        List<Post> cntlist = postService.postCntTen("off"); //poslist limit5 orderbyviews desc
        List<Post> noticelist = postService.postCnt("on"); //noticeist limit5 orderbyviews desc
        List<Survey> surveyList = surveyService.surveyListfive(); //poslist limit5 orderbyviews desc
        model.addAttribute("post", cntlist);
        model.addAttribute("notice", noticelist);
        model.addAttribute("survey", surveyList);
        return "gallery";
    }


    @GetMapping("/write")
    public String getWrite(HttpServletRequest request){
        HttpSession session = userService.sessionAutowired(request);
        return "write";
    }
    @PostMapping("/write")
    public String postWrite(HttpServletRequest request, String title, String name, String userPw ,String content, String category){
        if(category ==null){
            category="off";
        }

        HttpSession session = userService.sessionAutowired(request);
        Long id = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        System.out.println("@@@@@@@@@@@@@@");
        System.out.println(id);

        User user = userService.selectOne(id);

        PostDto postDto = PostDto.builder()
                .postNo(postService.max()+1L)
                .userNo(user)
                .category(category)
                .title(title)
                .name(name)
                .pw(userPw)
                .content(content)
                .modDate(LocalDateTime.now())
                .regDate(LocalDateTime.now())
                .views(0L)
                .build();
        postService.create(postDto);
        PostDto postDto1 = postService.selectOne(postDto); // 7월 2일 오후 10시 47분 추가
        return "redirect:/post?postNo=" + postDto1.getPostNo(); // 7월 2일 오후 10시 47분 추가
    }

    //고개센터 리스트 뿌리기 20210702
    @GetMapping("service")
    public String serviceList(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="20") int size, String category, SearchDto searchDto){
        category ="service";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> servicelist = postService.getClientList(pageable, category, searchDto);
        model.addAttribute("servicelist", servicelist);
        return"serviceboard"; //고객센터
    }
    //회원일때 고객센터 글쓰기 가는링크
    @GetMapping("question")
    public String getquestion(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        model.addAttribute("user", user);
        return"question";
    }
    //비회원일때 고객센터 글쓰기가는링크
//    @GetMapping("nonquestion")
//    public String nongetquestion(){
//        return"question";
//    }
    //비회원일때 고객센터 글쓰기가는링크
    //회원 비회원 다 글쓰고 postlink
    @PostMapping("question")
    public String postquestion(HttpServletRequest request,String name, String title, String content){
        HttpSession session = request.getSession();
        String cate ="service";
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        PostDto dto = new PostDto();
            dto = PostDto.builder()
                    .postNo(postService.max()+1L)
                    .userNo(user)
                    .category(cate)
                    .content(content)
                    .name(name)
                    .title(title)
                    .pw(user.getUserPw())
                    .regDate(LocalDateTime.now())
                    .modDate(LocalDateTime.now())
                    .views(0L)
                    .build();
        postService.create(dto);
        return"redirect:/service";
    }











    //인환씨


    @GetMapping("/post")
    public String post(HttpServletRequest request, Model model, Long postNo, Long presentPage, String func,
                       Long userNo, String name, String pw, String content, Long commentNo) {
        postService.postviewupdate(postNo); //접속시 view가 1증가
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userId")));

        if (presentPage == null) {
            presentPage = 1L;
        }
        if (func == null) {
            func = "";
        }

        if (func.equals("editPost")) {
            return "redirect:/edit?postNo=" + postNo;
        } else if (func.equals("deletePost")) {
            commentService.deleteAll(postNo);
            postService.delete(postNo);
            return "main";
        } else if (func.equals("newComment")) {
            commentService.newComment(postNo, userNo, name, pw, content);
            return "redirect:/post?postNo=" + postNo;
        } else if (func.equals("deleteComment")) {
            commentService.delete(commentNo);
            return "redirect:/post?postNo=" + postNo;
        }

//        get postDto and exception handling
        PostDto post = postService.getPost(postNo);
        if (post == null) {
            return "main";
        } else {
            model.addAttribute("post", post);
        }


        List<CommentDto> fullCommentList = commentService.getFullCommentList(postNo);
        Paging commentPaging = commentService.paging(presentPage, new Long(fullCommentList.size()));
        model.addAttribute("commentPaging", commentPaging);
        if (commentPaging.getTotalElement() > 0) {
            List<CommentDto> commentList = commentService.getCommentList(fullCommentList, commentPaging);
            model.addAttribute("commentList", commentList);
        }

        if (userNum != 2L) {
            User user = userService.getUser(userNum);

            model.addAttribute("userNo", user.getUserNo());
            model.addAttribute("name", user.getName());
        } else {
            model.addAttribute("userNo", 2);
        }
        return "post";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long postNo, String func, Long views,
                       String title, String name, String pw, String category,
                       String content, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        if (func == null) {
            func = "";
        }

        PostDto post = postService.getPost(postNo);
        model.addAttribute("post", post);

        if (func.equals("editPost")) {
            postService.editPost(post.getUserNo(), postNo, views, title, name, pw, category, content, post.getRegDate());
            return "redirect:/post?postNo=" + postNo;
        }

        if (post.getUserNo().getUserNo() > 2 || userNum == 1) {
            System.out.println("ininininininininin!!!!" +userNum);
            return "edit_members";
        } else {
            return "edit_nMembers";
        }
    }














}
