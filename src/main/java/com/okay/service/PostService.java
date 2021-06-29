package com.okay.service;

import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.domain.repository.PostRepository;
import com.okay.dto.PostDto;
import com.okay.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public void remove(PostDto postDto){
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

    public Page<Post> getPostList(Pageable pageable, SearchDto searchDto) { // 글 목록 조회
        Page<Post> postList = null;
        Page<Post> nullPostList = null;
        switch (searchDto.getSearchFilter()) {
            case "title" : postList = postRepository.findAllByTitleContaining(searchDto.getSearchValue(), pageable); //제목으로 검색하는 경우
                break;
            case "content" : postList = postRepository.findAllByContentContaining(searchDto.getSearchValue(), pageable); //내용으로 검색하는 경우
                break;
            case "name" : postList = postRepository.findAllByNameContaining(searchDto.getSearchValue(), pageable); //작성자로 검색하는 경우
                break;
            case "all" : postList = postRepository.findAllByTitleContainingOrContentContainingOrNameContaining(searchDto.getSearchValue(), searchDto.getSearchValue(), searchDto.getSearchValue(), pageable); //전체 검색
                break;
            default : postList = postRepository.findAll(pageable); //기본값 전체글 목록 (Paging)
                break;
        }
        if(postList.isEmpty()) return nullPostList; //검색 결과가 없을 경우 빈 객체 반환

        return postList;
    }
    public List<Post> postCnt(){

        List<Post> postgallatyorder = postRepository.findFirst5ByOrderByViewsDesc();

        return postgallatyorder;
    }
    public Page<Post> getPostList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public PostDto getPost(Long postNo) {
        Post postEntity = postRepository.findByPostNo(postNo);
        PostDto postDto = new PostDto();
        postDto = postDto.changePostDto(postEntity);
        return postDto;
    }
    public List<Post> postCntTen(){

        List<Post> postgallatyorderten = postRepository.findFirst10ByOrderByViewsDesc();

        return postgallatyorderten;
    }

    public List<Post> postupdate(User userno){
        List<Post> updatepost = postRepository.findAllByUserNo(userno);

        return updatepost;
    }
}
