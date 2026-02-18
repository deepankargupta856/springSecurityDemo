package org.learn.springsecuritydemo.services;

import org.learn.springsecuritydemo.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long id);

    PostDTO updatePost(PostDTO inputPost, Long id);

}
