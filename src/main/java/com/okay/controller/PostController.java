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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PostController {
    @Resource(name = "uploadPostPath")
    String path;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    SurveyService surveyService;


    @GetMapping("/search")
    public String search(Model model, @RequestParam(value="page", defaultValue="1") int page
            ,@RequestParam(value="size", defaultValue="20") int size, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> searchList = postService.getSearchList(pageable, searchDto);
        model.addAttribute("searchList", searchList);
        return "search";
    }


    @GetMapping("/board")
    public String board(Model model, @RequestParam(value="page", defaultValue="1") int page
            ,@RequestParam(value="size", defaultValue="20") int size, String category, SearchDto searchDto) {
        category = "off";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> postList = postService.getPostList(pageable, category, searchDto);
        model.addAttribute("postList", postList);
        return "board";
    }

    @GetMapping("/gallery")
    public String gallaryList(Model model) {
        System.out.println("컨트롤러 출력되나??");
        List<Post> cntList = postService.postCntTen("off"); //poslist limit5 orderbyviews desc
        List<Post> noticeList = postService.postCnt("on"); //noticeist limit5 orderbyviews desc
        List<Survey> surveyList = surveyService.surveyListfive(); //poslist limit5 orderbyviews desc
        model.addAttribute("post", cntList);
        model.addAttribute("notice", noticeList);
        model.addAttribute("survey", surveyList);
        return "gallery";
    }


    @GetMapping("/write")
    public String getWrite(HttpServletRequest request, Model model){
        HttpSession session = userService.sessionAutowired(request);
        Long id = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(id);
        model.addAttribute("name", user.getName()); // 작성자 넘김
        model.addAttribute("pw",user.getUserPw());
        return "write";
    }

    @PostMapping("/write")
    public String postWrite(HttpServletRequest request, String title, String name,
                            MultipartFile uploadFile, String pw , String content, String category){
        if(category ==null){
            category="off";
        }
        HttpSession session = userService.sessionAutowired(request);
        Long id = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(id);
        String regDate =LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        File file = new File(path,uploadFile.getOriginalFilename()); // 파일 입력
        try{
            uploadFile.transferTo(file);
        }catch (Exception e){
            System.out.println("파일 전송 실패");
        }
        PostDto postDto = PostDto.builder()
                .postNo(postService.max()+1L)
                .userNo(user)
                .category(category)
                .title(title)
                .name(name)
                .pw(pw)
                .fileName(uploadFile.getOriginalFilename())
                .content(content)
                .modDate(regDate)
                .regDate(regDate)
                .views(0L)
                .build();
        postService.create(postDto);
        PostDto dto = postService.selectOne(postDto); // 7월 2일 오후 10시 47분 추가
        return "redirect:/post?postNo=" + dto.getPostNo(); // 7월 2일 오후 10시 47분 추가
    }
    @GetMapping("/notice") // 7.2 추가
    public String getNoticeList(Model model, @RequestParam(value="page", defaultValue="1") int page
            ,@RequestParam(value="size", defaultValue="20") int size, String category, SearchDto searchDto) {
        category = "on";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> noticeList = postService.getNoticeList(pageable, category, searchDto);
        model.addAttribute("noticeList", noticeList);
        return "noticeBoard";
    }

    //고개센터 리스트 뿌리기 20210702
    @GetMapping("/service")
    public String serviceList(Model model, @RequestParam(value="page", defaultValue="1") int page
            ,@RequestParam(value="size", defaultValue="20") int size,String category, SearchDto searchDto){
        category ="service";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> servicelist = postService.getServiceList(pageable, category, searchDto);
        model.addAttribute("serviceList", servicelist);
        return"serviceBoard"; //고객센터
    }


    //회원일때 고객센터 글쓰기 가는링크
    @GetMapping("/question")
    public String getQuestion(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        model.addAttribute("user", user);
        return"question";
    }
    @PostMapping("/question")
    public String postQuestion(HttpServletRequest request,String name, String title, String content){
        HttpSession session = request.getSession();
        String category ="service";
        Long userNo = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        User user = userService.selectOne(userNo);
        String regDate =LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        PostDto dto = PostDto.builder()
                    .postNo(postService.max()+1L)
                    .userNo(user)
                    .category(category)
                    .content(content)
                    .name(name)
                    .title(title)
                    .pw(user.getUserPw())
                    .regDate(regDate)
                    .modDate(regDate)
                    .views(0L)
                    .build();
        postService.create(dto);
        return"redirect:/service";
    }


    @GetMapping("/post")
    public String post(HttpServletRequest request, Model model, Long postNo,
                       Long presentPage) {
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userId")));

        if (presentPage == null) {
            presentPage = 1L;
        }
        PostDto post = postService.getPost(postNo);
        model.addAttribute("post", post);
// 0706 위에 수정
        List<CommentDto> fullCommentList = commentService.getFullCommentList(postNo);
        Paging commentPaging = commentService.paging(presentPage, Long.valueOf(fullCommentList.size()));
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
        return"post";
    }

    @PostMapping("/edit")
    public String edit(Model model, String func, Long postNo,
                       String category, Long views,
                       String title, String name, String pw, String content
    ) {
        PostDto post = postService.getPost(postNo);
        if (func.equals("edit")) {
            model.addAttribute("post", post);
            return "edit";
        } else {
            postService.editPost(post.getUserNo(), postNo, views, title, name, pw, category, content, post.getRegDate());
            return "redirect:/post?postNo=" + postNo;
        }
    }

    @PostMapping("/remove")
    public String delete(Long postNo) {
        commentService.deleteAll(postNo);
        postService.delete(postNo);
        return "redirect:/board";
    }

    @PostMapping("/new-comment")
    public String newComment(Long postNo, String name,
                             String pw, String content, Long userNo) {

        commentService.newComment(postNo, userNo, name, pw, content);

        return "redirect:/post?postNo=" + postNo;
    }

    @PostMapping("/remove-comment")
    public String deleteComment(Long commentNo, Long postNo) {
        commentService.delete(commentNo);
        return "redirect:/post?postNo=" + postNo;
    }


}
