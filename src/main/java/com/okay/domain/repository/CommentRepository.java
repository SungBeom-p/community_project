package com.okay.domain.repository;

import com.okay.domain.entity.Comment;
import com.okay.domain.entity.Post;
import com.okay.dto.Paging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostNo (Post post);

}
