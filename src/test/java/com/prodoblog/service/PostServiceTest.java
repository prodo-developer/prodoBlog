package com.prodoblog.service;

import com.prodoblog.domain.Post;
import com.prodoblog.exception.PostNotFound;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostCreate;
import com.prodoblog.request.PostEdit;
import com.prodoblog.request.PostSearch;
import com.prodoblog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;
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
        List<Post> requestPost = IntStream.range(0, 20)
                                .mapToObj(i -> Post.builder()
                                         .title("프로도 제목 : " + i)
                                         .content("매지션 내용 : " + i)
                                         .build())
                                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        // 수동 페이지 번호 넘기기
//        Pageable pageable = PageRequest.of(0, 5, Sort.by(DESC, "id"));

        PostSearch postSearch = PostSearch.builder()
                                .page(1)
                                .build();

        // when
        List<PostResponse> postList = postService.getList(postSearch);

        // then
        // one-indexed-parameters: true를 통해 1부터 인덱스 가져올수있음
        assertEquals(10L, postList.size());
        assertEquals("프로도 제목 : 19", postList.get(0).getTitle());
//        assertEquals("프로도 제목 : 30", postList.get(0).getTitle());
//        assertEquals("프로도 제목 : 26", postList.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void testUpdate() {
        // given
        Post post = Post.builder()
                .title("프로도")
                .content("관악봉천")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("라이언")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("라이언", changePost.getTitle());
    }

    @Test
    @DisplayName("글 제목&내용 수정")
    void testUpdate2() {
        // given
        Post post = Post.builder()
                .title("프로도")
                .content("관악봉천")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("라이언")
                .content("관악신림")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("라이언", changePost.getTitle());
        assertEquals("관악신림", changePost.getContent());
    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test6() {
        // given
        Post post = Post.builder()
                .title("프로도")
                .content("관악봉천")
                .build();
        postRepository.save(post);

        // excepted
        assertThrows(PostNotFound.class, () -> {
                postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 수정 - 존재하지 않는 글")
    void test7() {
        // given
        Post post = Post.builder()
                .title("프로도")
                .content("관악봉천")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("라이언")
                .build();

        // when
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("프로도")
                .content("관악봉천")
                .build();

        postRepository.save(post);

        // excepted
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }
}