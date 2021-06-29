package com.okay.dto;

import com.okay.Adapt.PostAdapt;
import com.okay.domain.entity.Post;
import com.okay.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PostDto implements PostAdapt {
    private Long postNo;
    private Long views;
    private User userNo;
    private String name;
    private String pw;
    private String category;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Override
    public Post changePost(PostDto postDto) {
        Post post = Post.builder()
                .postNo(postDto.getPostNo())
                .views(postDto.getViews())
                .userNo(postDto.getUserNo())
                .name(postDto.getName())
                .pw(postDto.getPw())
                .category(postDto.getCategory())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .regDate(postDto.getRegDate())
                .modDate(postDto.getModDate())
                .build();
        return post;
    }

    public PostDto changePostDto(Post post) {
        PostDto postDto = PostDto.builder()
                .postNo(post.getPostNo())
                .views(post.getViews())
                .userNo(post.getUserNo())
                .name(post.getName())
                .pw(post.getPw())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .regDate(post.getRegDate())
                .modDate(post.getModDate())
                .build();
        return postDto;
    }

}

