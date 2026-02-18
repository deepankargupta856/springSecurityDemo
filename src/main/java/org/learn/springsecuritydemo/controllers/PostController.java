package org.learn.springsecuritydemo.controllers;

import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.dto.PostDTO;
import org.learn.springsecuritydemo.services.PostService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @Secured("ROLE_USER")
    public List<PostDTO> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole{'USER'} OR hasAuthority{'POST_VIEW'}}")
    public PostDTO getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost){
        return postService.createNewPost(inputPost);
    }

    @PutMapping("/{id}")
    public PostDTO updatePost(@RequestBody PostDTO inputPost,
                              @PathVariable Long id){
        return postService.updatePost(inputPost,id);
    }

}
