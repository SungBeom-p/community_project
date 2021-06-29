package com.okay.controller;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.dto.CommentDto;
import com.okay.dto.Paging;
import com.okay.dto.PostDto;
import com.okay.dto.SearchDto;
import com.okay.service.CommentService;
import com.okay.service.PostService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @GetMapping("/board")
    public String board(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="20") int size, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postNo").descending()); // 0부터 담기기때문에..-1 requestparam->페이징 받아줌
        Page<Post> postList = postService.getPostList(pageable, searchDto);
        model.addAttribute("postList", postList);
        return "board";
    }
    @GetMapping("/gallery")
    public String gallarylist(Model model) {
        System.out.println("컨트롤러 출력되나??");
        //List<Post> alist = postservice.postgallaty();
        List<Post> cntlist = postService.postCntTen(); //poslist limit5 orderbyviews desc
        List<Post> storelist = postService.postCnt(); //poslist limit5 orderbyviews desc
    //  List<Post> cntlist = postservice.postcnt(); //storelist limit5 orderbyviews desc
    //  List<Post> cntlist = postservice.postcnt(); //hobbylist limit5 orderbyviews desc
        model.addAttribute("post", cntlist);
        model.addAttribute("store", storelist);
    //  model.addAttribute("post",cntlist);
    //  model.addAttribute("store",storelist);
    //전체글리스트    model.addAttribute("hobby",hobbylist);

        return "gallery";
    }

    @GetMapping("/post")
    public String test(Model model, Long postNo, Long presentPage) {
        if (presentPage == null) {
            presentPage = 1L;
        }

        model.addAttribute("post", postService.getPost(postNo));
        List<CommentDto> fullCommentList = commentService.getFullCommentList(postNo);
        Paging commentPaging = commentService.paging(presentPage, new Long(fullCommentList.size()));
        model.addAttribute("commentPaging", commentPaging);
        if (commentPaging.getTotalElement() > 0) {
            List<CommentDto> commentList = commentService.getCommentList(fullCommentList, commentPaging);
            model.addAttribute("commentList", commentList);
        }
        return "post";
    }

    @GetMapping("/edit")
    public String edit(Model model, PostDto postDto, String func) {
        if (func == null) {
            func = "";
            postDto = postService.getPost(postDto.getPostNo());
            model.addAttribute("post", postService.getPost(postDto.getPostNo()));
        } else if (func.equals("edit")) {
            postService.update(postDto);
            return "redirect:/post?postNo=" + postDto.getPostNo();
        }
        return "edit";
    }

    @GetMapping("/write")
    public String getWrite(HttpServletRequest request){
        HttpSession session = userService.sessionAutowired(request);
        return "write";
    }
    @PostMapping("/write")
    public String postWrite(String title, String name, String password ,String content, String category){
//        PostDto postDto = PostDto.builder()
//                .postNo()
//                .userNo()
//                .category()
//                .build();
        return "";
    }
}
