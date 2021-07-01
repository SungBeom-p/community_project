package com.okay.service;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import com.okay.domain.repository.CommentRepository;
import com.okay.dto.CommentDto;
import com.okay.dto.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CommentService extends Service{
    @Autowired
    CommentRepository commentRepository;

    public void create(CommentDto commentDto){
        Comment comment = commentDto.changeComment(commentDto);
        commentRepository.save(comment);
    }

    public void update(CommentDto commentDto){
        Comment comment = commentDto.changeComment(commentDto);
        commentRepository.save(comment);
    }

    public void delete(CommentDto commentDto){
        commentRepository.deleteById(commentDto.getCommentNo());
    }

    public CommentDto selectOne(CommentDto commentDto){
        Comment comment = commentDto.changeComment(commentDto);
        Optional<Comment> temp = commentRepository.findById(comment.getCommentNo());
        CommentDto result = commentDto.changeCommentDto(temp.get());
        return result;
    }

    public Comment selectOne(Long id){
        Optional<Comment> result = commentRepository.findById(id);
        result.ifPresent(comment -> {throw new IllegalAccessError();
        });
        return result.get();
    }

    public List<CommentDto> selectAll(CommentDto commentDto){
        List<Comment> temp = commentRepository.findAll();
        List<CommentDto> result = new ArrayList<>();
        temp.forEach(i->{
            result.add(commentDto.changeCommentDto(i));
        });
        return result;
    }
    public List<Comment> selectAll(Long id){
        return commentRepository.findAll();
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
        Long temp = presentPage;
        while (true) {
            if (temp % paging.getPageQty() == 1) {
                paging.setStartAt(temp);
                paging.setEndBy(paging.getStartAt() + (paging.getPageQty() - 1));
                break;
            } else {
                temp--;
            }
        }

        Double temp2 = Math.ceil(new Double(paging.getTotalElement()) / paging.getCommentQty());
        paging.setTotalPage(temp2.longValue());

        if (presentPage > paging.getTotalPage()) {
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

    @Transactional
    public Comment deletepost(Post postNo){
        Comment comment = commentRepository.deleteCommentByPostNo(postNo);
        return comment;
    }

    public List<Comment> commentsize(){
        List<Comment> list = commentRepository.findAllBy();
        return list;
    }

}
