package com.prodoblog.service;

import com.prodoblog.domain.Post;
import com.prodoblog.domain.PostEditor;
import com.prodoblog.exception.PostNotFound;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostEdit;
import com.prodoblog.request.PostCreate;
import com.prodoblog.request.PostSearch;
import com.prodoblog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

    }

    public Post getRss(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return post;
    }

//    public List<PostResponse> getList() {
//        return postRepository.findAll().stream()
//                .map(post -> PostResponse.builder()
//                        .id(post.getId())
//                        .title(post.getTitle())
//                        .content(post.getContent())
//                        .build())
//                .collect(Collectors.toList());
//    }

//      2단계
//      return postRepository.findAll().stream()
//                .map(PostResponse::new)
//                .collect(Collectors.toList());

    // 자주사용하는 빌더 패턴은 생성자 오버로딩을 통해 아래와 같이 리팩토링
    public List<PostResponse> getList(PostSearch postSearch) {
        // web -> page 1 -> 0
        // Sort.by 내림차순

        // 아래에 수동으로 만든 페이지가 의미없음
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(DEFAULT_DIRECTION.DESC, "id"));

//      spring data jpa
//        return postRepository.findAll(page).stream()
//                .map(PostResponse::new)
//                .collect(Collectors.toList());

//      querydsl
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public PostResponse edit(Long id, PostEdit poseEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));


        // 검증 필터링
//        if(poseEdit.getTitle() != null) {
//            editorBuilder.title(poseEdit.getTitle());
//        }
//
//        if(poseEdit.getContent() != null) {
//            editorBuilder.content(poseEdit.getContent());
//        }
//        // 컨텐츠는 그대로 이동
//        post.edit(editorBuilder.build());

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        // fix안된 변경이 필요한 값들만 수정
        PostEditor postEditor = editorBuilder
                .title(poseEdit.getTitle())
                .content(poseEdit.getContent())
                .build();
        post.edit(postEditor);

//        post.change(poseEdit.getTitle(), poseEdit.getContent());

//      트랜잭션을 통해 아래내용을 생략해도 된다.
//        postRepository.save(post);

        return new PostResponse(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        postRepository.delete(post);
    }
}
