package org.learn.springsecuritydemo.utils;

import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.dto.PostDTO;
import org.learn.springsecuritydemo.entities.User;
import org.learn.springsecuritydemo.services.PostService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;

    public boolean isOwnerOfPost(Long postId){
        PostDTO post = postService.getPostById(postId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return post.getAuthor().getId().equals(user.getId());
    }
}
