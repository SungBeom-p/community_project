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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PostController {
    //파일 저장경로
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
    LocalDateTime time = LocalDateTime.now();
    // 오늘 날짜
    LocalDate today = LocalDate.now();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String now = today.format(formatter);
    String last = yesterday.format(formatter);

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
        List<Post> cntList = postService.postCntFive("off"); //poslist limit5 orderbyviews desc
        List<Post> noticeList = postService.postCnt("on"); //noticeist limit5 orderbyviews desc
        List<Survey> surveyList = surveyService.surveyListfive(); //poslist limit5 orderbyviews desc
        BufferedReader br = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;

        if(Long.parseLong(String.valueOf(time.getHour()))<8){
            now = last;
        }

        try {
            String urlStr ="http://apis.data.go.kr/1480523/MetalMeasuringResultService/MetalService?serviceKey=pygR8TY6ssILm9rtlqhq%2BuhPL8jhCkpVKBKWFtg8TPzXmf5cDVqYDbZgp4KkadbTjNd7G1reUFUZjVn8BfPbiQ%3D%3D&pageNo=1&numOfRows=12&resultType=XML&date="+now+"&stationcode=1&itemcode=90303&timecode=RH02";

            URL url = new URL(urlStr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String result = "";
            String line = "";

            result = br.readLine();
            while ((line = br.readLine()) != null) {
                result = result + line + "\n";
            }

            System.out.println("Sucress");

            // xml 파싱하기
            InputSource is = new InputSource(new StringReader(result));

            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            // XPathExpression expr = xpath.compile("/response/body/items/item");
            XPathExpression expr = xpath.compile("//items/item");
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            String[] timeArr = new String[nodeList.getLength()];
            String[] contentArr = new String[nodeList.getLength()];

            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList child = nodeList.item(i).getChildNodes();

                for (int j = 0; j < child.getLength(); j++) {
                    Node node = child.item(j);
                    if(node.getNodeName().equals("sdate")){
                        timeArr[i] = node.getTextContent().substring(8,10);
                    }
                    if(node.getNodeName().equals("value")){
                        contentArr[i] = node.getTextContent();
                    }
                }
            }


            urlStr = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=pygR8TY6ssILm9rtlqhq%2BuhPL8jhCkpVKBKWFtg8TPzXmf5cDVqYDbZgp4KkadbTjNd7G1reUFUZjVn8BfPbiQ%3D%3D" +
                    "&pageNo=1&numOfRows=10&startCreateDt="+now+"&endCreateDt="+now;
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            result = "";
            line = "";

            while ((line = br.readLine()) != null) {
                result = result + line + "\n";
            }
            System.out.println("Sucress");

            is = new InputSource(new StringReader(result));

            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);

            xpathFactory = XPathFactory.newInstance();
            xpath = xpathFactory.newXPath();
            // XPathExpression expr = xpath.compile("/response/body/items/item");
            expr = xpath.compile("//items/item");
            nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            String[] divisionArr = new String[nodeList.getLength()];
            String[] contentArr2 = new String[nodeList.getLength()];

            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList child = nodeList.item(i).getChildNodes();

                for (int j = 0; j < child.getLength(); j++) {
                    Node node = child.item(j);

                    if(node.getNodeName().equals("gubun")){
                        divisionArr[i] = node.getTextContent();
                    }
                    if(node.getNodeName().equals("incDec")) {
                        contentArr2[i] = node.getTextContent();
                    }
                }

            }
            model.addAttribute("num", contentArr2[contentArr2.length-1]);
            model.addAttribute("day", now);
            model.addAttribute("timeArr", timeArr);
            model.addAttribute("contentArr", contentArr);
            model.addAttribute("division",divisionArr);
            model.addAttribute("cnt",contentArr2);
            model.addAttribute("post", cntList);
            model.addAttribute("notice", noticeList);
            model.addAttribute("survey", surveyList);
        } catch (Exception e) {
            System.out.println("Fail");
            System.out.println(e.getMessage());
        }

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
        File file = new File(path,uploadFile.getOriginalFilename()); // 파일 입력
        try{
            uploadFile.transferTo(file);
        }catch (Exception e){
            System.out.println("파일 전송 실패");
        }
        // 0707 수정 시작
        PostDto dt= new PostDto();

        try{
            dt.setPostNo(postService.max()+1L);
        }catch (Exception e){
            dt.setPostNo(1L);
        }

        PostDto postDto = PostDto.builder()
                .postNo(dt.getPostNo())
                .userNo(user)
                .category(category)
                .title(title)
                .name(name)
                .pw(pw)
                .fileName(uploadFile.getOriginalFilename())
                .content(content)
                .modDate(now)
                .regDate(now)
                .views(0L)
                .build();
        postService.create(postDto);
        return "redirect:/post?postNo="+postDto.getPostNo();
        // 0707 수정 끝
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
        PostDto dto = PostDto.builder()
                .postNo(postService.max()+1L)
                .userNo(user)
                .category(category)
                .content(content)
                .name(name)
                .title(title)
                .fileName("")
                .pw(user.getUserPw())
                .regDate(now)
                .modDate(now)
                .views(0L)
                .build();
        postService.create(dto);
        return"redirect:/service";
    }


    @GetMapping("/post")
    public String post(HttpServletRequest request, Model model, Long postNo,Long presentPage) {
        HttpSession session = request.getSession();
        if(presentPage == null){
            presentPage = 1L;
        }

        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userId")));
        try{
            Post entity = postService.selectOne(postNo);
            PostDto post = new PostDto();
            post = post.changePostDto(entity);
            model.addAttribute("post", post);
        }catch (Exception e){
            return "main";
        }

        Post post = postService.selectOne(postNo);
        System.out.println(post.getFileName());
        model.addAttribute("img", post.getFileName());

        // 0706 위에 수정
        List<CommentDto> fullCommentList = commentService.getFullCommentList(postNo);
        Paging commentPaging = commentService.paging(presentPage, Long.valueOf(fullCommentList.size()));
        model.addAttribute("commentPaging", commentPaging);

        if (commentPaging.getTotalElement() > 0) {
            List<CommentDto> commentList = commentService.getCommentList(fullCommentList, commentPaging);
            model.addAttribute("commentList", commentList);
        }

        User user = userService.getUser(userNum);
        model.addAttribute("userNo", user.getUserNo());
        model.addAttribute("name", user.getName());
        return "post";
    }

    @PostMapping("/edit")
    public String edit(Model model,Long postNo) {
        // postNo의 값을 불러오기
        Post post = postService.selectOne(postNo);
        model.addAttribute("post",post);

        return "edit";
    }
    @PostMapping("/editPost")
    public String editPost(Long postNo, String title,String pw,String category, String name, String content, MultipartFile uploadFile){
        Post post = postService.selectOne(postNo);

        PostDto dto = new PostDto();
        PostDto postDto = dto.changePostDto(post);

        postDto.setContent(content);
        postDto.setCategory(post.getCategory());
        postDto.setTitle(title);
        postDto.setName(name);
        postDto.setPw(post.getPw());

        postService.update(postDto);
        return "redirect:/post?postNo="+postDto.getPostNo();
    }

    @PostMapping("/remove")
    public String delete(Long postNo, String category) {
        String page = "";
        if (category.equals("on")) {
            page = "redirect:/notice";
        } else if (category.equals("off")) {
            page = "redirect:/board";
        } else {
            page = "redirect:/service";
        }
        commentService.deleteAll(postNo);
        postService.delete(postNo);
        return page;
    }

    @PostMapping("/newComment")
    public String newComment(Long postNo, String name,
                             String pw, String content, Long userNo) {

        commentService.newComment(postNo, userNo, name, pw, content);

        return "redirect:/post?postNo=" + postNo;
    }

    @PostMapping("/removeComment")
    public String deleteComment(Long commentNo, Long postNo) {
        commentService.delete(commentNo);
        return "redirect:/post?postNo=" + postNo;
    }


}