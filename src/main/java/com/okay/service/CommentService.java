package com.okay.service;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.domain.repository.CommentRepository;
import com.okay.dto.CommentDto;
import com.okay.dto.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CommentService extends Service{
    @Autowired
    CommentRepository commentRepository;

    LocalDate today = LocalDate.now();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String now = today.format(formatter);

    public void update(CommentDto commentDto){
        Comment comment = commentDto.changeComment(commentDto);
        commentRepository.save(comment);
    }

    public Comment selectOne(Long id){
        Optional<Comment> result = commentRepository.findById(id);
        return result.get();
    }

    //관리자 회원관리에 사용
    public List<Comment> commentSize(){
        List<Comment> list = commentRepository.findAll();
        return list;
    }

    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<Comment> activeCommentSize(User userNo){
        List<Comment> list = commentRepository.findAllByUserNo(userNo);
        return list;
    }
    //회원 와 관리자 가 mypqge 활동내역에 사용
    public List<Comment> listComment(User userNo){
        List<Comment> list = commentRepository.findFirst5ByUserNoOrderByCommentNoDesc(userNo);
        return list;
    }


    public List<CommentDto> getFullCommentList(Long postNo) {
        Optional<Post> post = postRepository.findById(postNo);
        List<Comment> commentEntityList = commentRepository.findByPostNo(post.get());
        List<CommentDto> commentDtoList = new ArrayList<>();
        CommentDto commentDto = new CommentDto();
        commentEntityList.forEach((comment) -> {
            commentDtoList.add(commentDto.changeCommentDto(comment));
        });
        Collections.reverse(commentDtoList);
        return commentDtoList;
    }

    public Paging paging(Long presentPage, Long totalComment) {
        Paging paging = new Paging();
        paging.setPresentPage(presentPage);
        paging.setTotalElement(totalComment);
        Long page = presentPage;   //현재 페이지

        while (true) {
            if (page % paging.getPageQty() == 1) {
                paging.setStartAt(page);
                paging.setEndBy(paging.getStartAt() + (paging.getPageQty() - 1));
                break;
            } else {
                page--;
            }
        }
        //전체 페이지
        Double totalPage = Math.ceil(Double.valueOf(paging.getTotalElement()) / paging.getCommentQty());
        paging.setTotalPage(totalPage.longValue());

        if (page > paging.getTotalPage()) {
            paging.setPresentPage(paging.getTotalPage());
        }
        if (paging.getStartAt() > paging.getTotalPage()) {
            paging.setStartAt(paging.getTotalPage());
        }
        if (paging.getEndBy() > paging.getTotalPage()) {
            paging.setEndBy(paging.getTotalPage());
        }

        return paging;
    }

    //10개만 추출 댓글페이지
    public List<CommentDto> getCommentList(List<CommentDto> fullCommentList, Paging paging) {
        List<CommentDto> commentList = new ArrayList<>();
        Long startComment = (paging.getPresentPage() - 1) * paging.getCommentQty() + 1; //31
        Long endComment = startComment + paging.getCommentQty(); //41
        if (endComment > paging.getTotalElement()) {
            endComment = paging.getTotalElement();
        }
        for (; startComment <= endComment; startComment++) {
            commentList.add(fullCommentList.get(startComment.intValue() - 1));
        }
        return commentList;
    }

    public void newComment(Long postNo, Long userNo, String name, String pw, String content) {
        User user = userRepository.getOne(userNo);
        Post post = postRepository.getOne(postNo);
        BigDecimal max = commentRepository.max();
        // 0707 수정 시작
        CommentDto dto= new CommentDto();
        if(dto.getCommentNo()== null){
            dto.setCommentNo(0L);
        }else{
            dto.setCommentNo(Long.valueOf(String.valueOf(commentRepository.max())+1L));
        }

        Comment comment = Comment.builder()
                .commentNo(dto.getCommentNo())
                // 0707 수정 끝
                .postNo(post)
                .userNo(user)
                .name(name)
                .pw(pw)
                .content(content)
                .regDate(now)
                .build();
        commentRepository.save(comment);
    }
    public void delete(Long commentNo) {
        commentRepository.deleteById(commentNo);
    }

    public void deleteAll(Long postNo) {
        Optional<Post> post = postRepository.findById(postNo);
        commentRepository.deleteAllByPostNo(post.get());
    }


}
