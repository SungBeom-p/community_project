package com.okay.Adapt;

import com.okay.domain.entity.Post;
import com.okay.dto.PostDto;

public interface PostAdapt {
    Post changePost(PostDto postDto);

    PostDto changePostDto(Post post);
}
