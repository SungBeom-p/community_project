package com.okay.service;


import com.okay.domain.entity.Post;
import com.okay.domain.repository.CommentRepository;
import com.okay.domain.repository.PostRepository;
import com.okay.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
@org.springframework.stereotype.Service
public abstract class Service {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

}
