package com.prodoblog.service;

import com.prodoblog.domain.Post;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
//      방법1 : 지향하지 않음 (public 일때)
//      post.title = postCreate.getTitle();
//      post.content = postCreate.content();

//      방법2: postCreate -> Entity
//      Post post = new Post(postCreate.getTitle(), postCreate.getContent());

//      방법3: builder로 전환
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }
}
