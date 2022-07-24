package com.prodoblog.service;

import com.prodoblog.domain.Post;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostCreate;
import com.prodoblog.response.PostResponse;
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

    public PostResponse get(Long id) {
        // 가져와서 즉시 꺼내는것이 좋음
//        Optional<Post> postOptional = postRepository.findById(id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return response;
    }

    public Post getRss(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return post;
    }
}
