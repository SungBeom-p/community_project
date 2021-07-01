package com.okay.service;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.domain.repository.PostRepository;
import com.okay.dto.PostDto;
import com.okay.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class PostService extends Service{

    @Autowired
    PostRepository postRepository;

    public void create(PostDto postDto){
        Post post = postDto.changePost(postDto);
        postRepository.save(post);
    }

    @Transactional
    public void removed(PostDto postDto){
        postRepository.deleteById(postDto.getPostNo());
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

    public List<PostDto> selectAll(PostDto postDto){
        List<Post> temp = postRepository.findAll();
        List<PostDto> result = new ArrayList<>();
        temp.forEach(i->{
            result.add(postDto.changePostDto(i));
        });
        return result;
    }
    public List<Post> selectAll(Long id){
        return postRepository.findAll();
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
    public boolean validateId_Pw(Long id, String pw){ // 비회원 전용 Id와 Pw 확인
        boolean result = false;
        Optional<Post> temp = postRepository.findById(id);
        if(temp.get().equals(pw)){
            result = true;
        }
        return result;
    }
    //메인페이지에서의 전체 검색
    public Page<Post> getSearchList(Pageable pageable, SearchDto searchDto) {
        Page<Post> searchList = null;
        Page<Post> nullSearchList = null;
        switch (searchDto.getSearchFilter()) {
            case "all":
                searchList = postRepository.findAllByTitleContainingOrContentContainingOrNameContaining(searchDto.getSearchValue(), searchDto.getSearchValue(), searchDto.getSearchValue(), pageable);
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
            case "post_title" : postList = postRepository.findAllByTitleContainingAndCategory(searchDto.getSearchValue(), "off", pageable); //제목으로 검색하는 경우
                break;
            case "post_content" : postList = postRepository.findAllByContentContainingAndCategory(searchDto.getSearchValue(), "off", pageable); //내용으로 검색하는 경우
                break;
            case "post_name" : postList = postRepository.findAllByNameContainingAndCategory(searchDto.getSearchValue(), "off", pageable); //작성자로 검색하는 경우
                break;
            default : postList = postRepository.findAllByCategory("off", pageable); //기본값 전체글 목록 (Paging)
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
            case "notice_title" : noticeList = postRepository.findAllByTitleContainingAndCategory(searchDto.getSearchValue(), "notice", pageable); //제목으로 검색하는 경우
                break;
            case "notice_content" : noticeList = postRepository.findAllByContentContainingAndCategory(searchDto.getSearchValue(), "notice", pageable); //내용으로 검색하는 경우
                break;
            case "notice_name" : noticeList = postRepository.findAllByNameContainingAndCategory(searchDto.getSearchValue(), "notice", pageable); //작성자로 검색하는 경우
                break;
            default : noticeList = postRepository.findAllByCategory("notice", pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if(noticeList.isEmpty()) return nullNoticeList; //검색 결과가 없을 경우 빈 객체 반환

        return noticeList;
    }

    public Page<Post> getClientList(Pageable pageable, String category, SearchDto searchDto) {
        // 고객센터 목록 조회
        Page<Post> clientList = null;
        Page<Post> nullClientList = null;
        switch (searchDto.getSearchFilter()) {
            case "client_title" : clientList = postRepository.findAllByTitleContainingAndCategory(searchDto.getSearchValue(), "client", pageable); //제목으로 검색하는 경우
                break;
            case "client_content" : clientList = postRepository.findAllByContentContainingAndCategory(searchDto.getSearchValue(), "client", pageable); //내용으로 검색하는 경우
                break;
            case "client_name" : clientList = postRepository.findAllByNameContainingAndCategory(searchDto.getSearchValue(), "client", pageable); //작성자로 검색하는 경우
                break;
            default : clientList = postRepository.findAllByCategory("client", pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if(clientList.isEmpty()) return nullClientList; //검색 결과가 없을 경우 빈 객체 반환

        return clientList;
    }
    public List<Post> postCnt(){

        List<Post> postgallatyorder = postRepository.findFirst5ByOrderByViewsDesc();

        return postgallatyorder;
    }
    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public List<Post> postCntTen(){

        List<Post> postgallatyorderten = postRepository.findFirst10ByOrderByViewsDesc();

        return postgallatyorderten;
    }

    public List<Post> postupdate(User userno){
        List<Post> updatepost = postRepository.findAllByUserNo(userno);

        return updatepost;
    }

    public void create(Post post){

        postRepository.save(post);
    }
    //postcategori가 고객센터인 리스트
    public List<Post> categoryservice(String category){
        List<Post> list = postRepository.findAllByCategory(category);

        return list;
    }










    //인환씨


    public PostDto getPost(Long postNo) {
        Post postEntity = postRepository.findByPostNo(postNo);
        PostDto postDto = new PostDto();
        postDto = postDto.changePostDto(postEntity);
        return postDto;
    }


    public void editPost(User userNo, Long postNo, Long views, String title,
                         String name, String pw, String category, String content,
                         LocalDateTime regDate) {
        User user = userRepository.findByUserNo(userNo.getUserNo());
        Post post = Post.builder()
                .userNo(user)
                .postNo(postNo)
                .views(views)
                .title(title)
                .name(name)
                .pw(pw)
                .category(category)
                .content(content)
                .regDate(regDate)
                .modDate(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    public void delete(Long postNo) {
        postRepository.deleteById(postNo);
    }





}
