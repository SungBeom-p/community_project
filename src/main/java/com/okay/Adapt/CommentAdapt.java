package com.okay.Adapt;

import com.okay.domain.entity.Comment;
import com.okay.dto.CommentDto;

public interface CommentAdapt {
    Comment changeComment(CommentDto commentDto);

    CommentDto changeCommentDto(Comment comment);
}
