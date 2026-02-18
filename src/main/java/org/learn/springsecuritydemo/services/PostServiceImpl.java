package org.learn.springsecuritydemo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.springsecuritydemo.dto.PostDTO;
import org.learn.springsecuritydemo.entities.User;
import org.modelmapper.ModelMapper;
import org.learn.springsecuritydemo.entities.PostEntity;
import org.learn.springsecuritydemo.exceptions.ResourceNotFoundException;
import org.learn.springsecuritydemo.repositories.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper){
        this.postRepository = postRepository;
        this.mapper = mapper;
    } // can omit via lombok annotation of required args constructor


    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(post -> mapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO createNewPost(PostDTO inputPost) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity post = mapper.map(inputPost, PostEntity.class);
        post.setAuthor(user);
        return mapper.map(postRepository.save(post),PostDTO.class);
    }


    @Override
    public PostDTO getPostById(Long id) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("User {}", user);
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with given post id " + id + " does not exist"));
        return mapper.map(post,PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO inputPost, Long id) {
        PostEntity existingPost = postRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Post with given post id " + id + " does not exist"));

        inputPost.setId(id);

        mapper.map(inputPost, existingPost);

        PostEntity updatedPost = postRepository.save(existingPost);

        return mapper.map(updatedPost, PostDTO.class);
    }
}
