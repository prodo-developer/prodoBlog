package com.prodoblog.service;

import com.prodoblog.domain.Post;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostCreate;
import com.prodoblog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest // 통합테스트 (MockMvc 사용 못함)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    // test 메서드들이 실행될때 항상 실행이 되도록 함
    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void writeTest() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void readTest() {
        // given
        Post reqPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(reqPost);

        // Long postID = 1L;

        // when
        PostResponse response = postService.get(reqPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void readListTest() {
//        // given
//        postRepository.saveAll(List.of(
//                Post.builder()
//                        .title("foo1")
//                        .content("bar1")
//                        .build(),
//                Post.builder()
//                        .title("foo2")
//                        .content("bar2")
//                        .build()
//        ));
//
//
//        // when
//        List<PostResponse> postList = postService.getList();
//
//        // then
//        assertEquals(2L, postList.size());
//    }

    @Test
    @DisplayName("글 1페이지 조회")
    void onePageTest() {
        // given (30개)
        List<Post> requestPost = IntStream.range(1, 31)
                                .mapToObj(i -> Post.builder()
                                         .title("프로도 제목 : " + i)
                                         .content("매지션 내용 : " + i)
                                         .build())
                                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        // 수동 페이지 번호 넘기기
        Pageable pageable = PageRequest.of(0, 5, Sort.by(DESC, "id"));

        // when
        List<PostResponse> postList = postService.getList(pageable);

        // then
        // one-indexed-parameters: true를 통해 1부터 인덱스 가져올수있음
        assertEquals(5L, postList.size());
        assertEquals("프로도 제목 : 30", postList.get(0).getTitle());
        assertEquals("프로도 제목 : 26", postList.get(4).getTitle());
    }
}