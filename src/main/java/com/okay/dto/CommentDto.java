package com.okay.dto;

import com.okay.Adapt.CommentAdapt;
import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import lombok.*;
import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentDto implements CommentAdapt {
    private Long commentNo;
    private Post postNo;
    private User userNo;
    private String name;
    private String pw;
    private String content;
    private String regDate;


    @Override
    public Comment changeComment(CommentDto commentDto) {
        Comment comment = Comment.builder()
                .commentNo(commentDto.getCommentNo())
                .postNo(commentDto.getPostNo())
                .userNo(commentDto.getUserNo())
                .name(commentDto.getName())
                .pw(commentDto.getPw())
                .content(commentDto.getContent())
                .regDate(commentDto.getRegDate())
                .build();
        return comment;
    }

    @Override
    public CommentDto changeCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .commentNo(comment.getCommentNo())
                .postNo(comment.getPostNo())
                .userNo(comment.getUserNo())
                .name(comment.getName())
                .pw(comment.getPw())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .build();
        return commentDto;
    }
}

