package com.okay.service;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.domain.repository.PostRepository;
import com.okay.dto.PostDto;
import com.okay.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class PostService extends Service{

    @Autowired
    PostRepository postRepository;

    LocalDate today = LocalDate.now();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String now = today.format(formatter);



    public void create(PostDto postDto){
        Post post = postDto.changePost(postDto);
        postRepository.save(post);
    }

    public void update(PostDto postDto){
        Post post = postDto.changePost(postDto);
        postRepository.save(post);
    }

    public PostDto selectOne(PostDto postDto){
        Post post = postDto.changePost(postDto);
        Optional<Post> temp = postRepository.findById(postDto.getPostNo());
        PostDto result = postDto.changePostDto(temp.get());
        return result;
    }

    public Post selectOne(Long id){
        Optional<Post> result = postRepository.findById(id);
        return result.get();
    }
public List<PostDto> selectAll(){
        PostDto postDto = new PostDto();
        List<Post> temp = postRepository.findAll();
        List<PostDto> result = new ArrayList<>();
        temp.forEach(i->{
            result.add(postDto.changePostDto(i));
        });
        return result;
    }
    //메인페이지에서의 전체 검색
    public Page<Post> getSearchList(Pageable pageable, SearchDto searchDto) {
        Page<Post> searchList = null;
        Page<Post> nullSearchList = null;
        switch (searchDto.getSearchFilter()) {
            case "all":
                searchList = postRepository.findAllByTitleContainingOrContentContainingOrNameContaining(searchDto.getSearchValue()
                        , searchDto.getSearchValue(), searchDto.getSearchValue(), pageable);
                break;
        }
        if(searchList.isEmpty()) return nullSearchList; //검색 결과가 없을 경우 빈 객체 반환
        return searchList;
    }

    public Page<Post> getPostList(Pageable pageable, String category, SearchDto searchDto) {
        // 자유글 목록 조회
        Page<Post> postList = null;
        Page<Post> nullPostList = null;
        switch (searchDto.getSearchFilter()) {
            case "post_title" : postList = postRepository.findAllByTitleContainingAndCategory(searchDto.getSearchValue(), category, pageable); //제목으로 검색하는 경우
                break;
            case "post_content" : postList = postRepository.findAllByContentContainingAndCategory(searchDto.getSearchValue(), category, pageable); //내용으로 검색하는 경우
                break;
            case "post_name" : postList = postRepository.findAllByNameContainingAndCategory(searchDto.getSearchValue(), category, pageable); //작성자로 검색하는 경우
                break;
            default : postList = postRepository.findAllByCategory(category, pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if(postList.isEmpty()) return nullPostList; //검색 결과가 없을 경우 빈 객체 반환

        return postList;
    }

    public Page<Post> getNoticeList(Pageable pageable, String category, SearchDto searchDto) {
        // 공지글 목록 조회
        Page<Post> noticeList = null;
        Page<Post> nullNoticeList = null;
        switch (searchDto.getSearchFilter()) {
            case "notice_title" : noticeList = postRepository.findAllByTitleContainingAndCategory(searchDto.getSearchValue(), category, pageable); //제목으로 검색하는 경우
                break;
            case "notice_content" : noticeList = postRepository.findAllByContentContainingAndCategory(searchDto.getSearchValue(), category, pageable); //내용으로 검색하는 경우
                break;
            case "notice_name" : noticeList = postRepository.findAllByNameContainingAndCategory(searchDto.getSearchValue(), category, pageable); //작성자로 검색하는 경우
                break;
            default : noticeList = postRepository.findAllByCategory(category, pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if(noticeList.isEmpty()) return nullNoticeList; //검색 결과가 없을 경우 빈 객체 반환

        return noticeList;
    }

    public Page<Post> getServiceList(Pageable pageable, String category, SearchDto searchDto) {
        // 고객센터 목록 조회
        Page<Post> serviceList = null;
        Page<Post> nullServiceList = null;
        switch (searchDto.getSearchFilter()) {
            case "service_title" : serviceList = postRepository.findAllByTitleContainingAndCategory(searchDto.getSearchValue(), category, pageable); //제목으로 검색하는 경우
                break;
            case "service_content" : serviceList = postRepository.findAllByContentContainingAndCategory(searchDto.getSearchValue(), category, pageable); //내용으로 검색하는 경우
                break;
            case "service_name" : serviceList = postRepository.findAllByNameContainingAndCategory(searchDto.getSearchValue(), category, pageable); //작성자로 검색하는 경우
                break;
            default : serviceList = postRepository.findAllByCategory(category, pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if(serviceList.isEmpty()) return nullServiceList; //검색 결과가 없을 경우 빈 객체 반환
        return serviceList;
    }


    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<Post> listPost(User userNo){
        List<Post> list = postRepository.findFirst5ByUserNoOrderByPostNoDesc(userNo);
        return list;
    }

    public List<Post> postCnt(String category){
        List<Post> postGalleryOrder = postRepository.findFirst5ByCategoryOrderByViewsDesc(category);
        return postGalleryOrder;
    }

    public List<Post> postCntTen(String category){
        List<Post> postGalleryOrderTen = postRepository.findFirst10ByCategoryOrderByViewsDesc(category);

        return postGalleryOrderTen;
    }

    public List<Post> postUpdate(User userNo){
        List<Post> updatePost = postRepository.findAllByUserNo(userNo);
        return updatePost;
    }


//    public Post postviewupdate(Long postNo){
//        Post updateviewpost = postRepository.findAllByPostNo(postNo);
//        PostDto postDto = new PostDto();
//        postDto = postDto.changePostDto(updateviewpost);
//        postDto.setViews(updateviewpost.getViews()+1L);
//        Post post = postDto.changePost(postDto);
//        postRepository.save(post);
//        return post;
//    }

    public Long max(){
        BigDecimal max = postRepository.max();
        Long no = Long.valueOf(String.valueOf(max));
        return no;
    }


    public PostDto getPost(Long postNo) {
        Optional<Post> postEntity = postRepository.findById(postNo);
        PostDto postDto = new PostDto();
        postDto = postDto.changePostDto(postEntity.get());
        return postDto;
    }


    public void editPost(User userNo, Long postNo, Long views, String title,
                         String name, String pw, String category, String content,
                         String regDate) {
        User user = userRepository.findById(userNo.getUserNo()).get();
        Post post = Post.builder()
                .userNo(user)
                .postNo(postNo)
                .views(views)
                .title(title)
                .name(name)
                .pw(pw)
                .category(category)
                .content(content)
                .regDate(now)
                .modDate(now)
                .build();
                postRepository.save(post);
    }
    public void delete(Long postNo) {
        postRepository.deleteById(postNo);
    }
}
