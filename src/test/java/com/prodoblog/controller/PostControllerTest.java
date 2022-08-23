package com.prodoblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prodoblog.domain.Post;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostCreate;
import com.prodoblog.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest // 웹레이어 컨트롤러 테스트
@AutoConfigureMockMvc // MockMvc용 테스트
@SpringBootTest // 통합테스트 (MockMvc 사용 못함)
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc; // 컨트롤러에 요청

    @Autowired
    private PostRepository postRepository;

    // test 메서드들이 실행될때 항상 실행이 되도록 함
    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

//    @Test
//    @DisplayName("/posts 요청시 Hello World를 출력한다.")
//    void test() throws Exception {
//        // Map방식
//        mockMvc.perform(post("/posts")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title", "글 제목 입니다.")
//                        .param("content", "글 내용 입니다 prodo")
//                )   // application/json
//                .andExpect(status().isOk()) // 서버통신
//                .andExpect(MockMvcResultMatchers.content().string("Hello World")) // 해당문자가 일치하는가?
//                .andDo(print());
//    }

    @Test
    @DisplayName("/posts 요청시 json 방식으로 출력한다.")
    void jsonTest() throws Exception {
        // given
//        PostCreate request = new PostCreate("제목입니다.", "내용입니다.");
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // json 문자 필터링
//        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // 안쓰면 타입에러나서 415 에러남
                        .content(json)
//                        .content("{\"title\": \"제목 등록\", \"content\": \"제이슨 내용입니다.\"}")
                )   // application/json
                .andExpect(status().isOk()) // 서버통신
                .andExpect(content().string("")) // 해당문자가 일치하는가?
                .andDo(print());
        
        // DB -> post 1개 등록
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수입니다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // 안쓰면 타입에러나서 415 에러남
                // {"title": ""}
                // {"title": null}
//                        .content("{\"title\": null, \"content\": \"제이슨 내용입니다.\"}")
                                .content(json)
                )   // application/json
//                .andExpect(status().isOk()) // 서버통신
//                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요.")) // 해당문자가 일치하는가?
                .andExpect(status().isBadRequest()) // 서버통신
                .andExpect(jsonPath("$.code").value("400")) // 해당문자가 일치하는가?
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 해당문자가 일치하는가?
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요.")) // 해당문자가 일치하는가?
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // before
        // 매번 삭제해줘야되는 신경쓰기 번거로움
        postRepository.deleteAll();

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // 안쓰면 타입에러나서 415 에러남
                        .content(json)
//                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                )   // application/json
                .andExpect(status().isOk()) // 서버통신
                .andDo(print());


        // then
        // DB -> post 1개 등록
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                    .title("foofoofoofoofoo")
                    .content("bar")
                    .build();

        postRepository.save(post);

        // 클라 요구사항
        // json응답에서 title값 길이를 최대 10글자로 해주세요.
        // Post entity <-> PoseResponse


        // expected (when&then)
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)) // 안쓰면 타입에러나서 415 에러남
                        .andExpect(status().isOk()) // 서버통신
                        .andExpect(jsonPath("$.id").value(post.getId()))
                        .andExpect(jsonPath("$.title").value("foofoofoof"))
                        .andExpect(jsonPath("$.content").value("bar"))
                        .andDo(print());

        // then
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        List<Post> requestPost = IntStream.range(1, 20)
                .mapToObj(i -> Post.builder()
                        .title("프로도 제목 : " + i)
                        .content("매지션 내용 : " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        // expected (when&then)
        // 1페이지, 정렬(내림차순), 5개만
        // mockMvc.perform(get("/posts?page=1&sort=id,desc&size=5")
        // 방법 2
        // application.yml에서 default-page-size를 셋팅하면 생략 가능하다.
        mockMvc.perform(get("/posts?page=1&size=10")
                            .contentType(MediaType.APPLICATION_JSON)) // 안쓰면 타입에러나서 415 에러남
                        .andExpect(status().isOk()) // 서버통신
                        .andExpect(jsonPath("$.length()", is(10)))
                        .andExpect(jsonPath("$[0].title").value("프로도 제목 : 19"))
                        .andExpect(jsonPath("$[0].content").value("매지션 내용 : 19"))
                        .andDo(print());

        // then
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        List<Post> requestPost = IntStream.range(1, 20)
                .mapToObj(i -> Post.builder()
                        .title("프로도 제목 : " + i)
                        .content("매지션 내용 : " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPost);

        // Offset must not be negative 방지
        mockMvc.perform(get("/posts?page=0&size=10")
                            .contentType(MediaType.APPLICATION_JSON)) // 안쓰면 타입에러나서 415 에러남
                        .andExpect(status().isOk()) // 서버통신
                        .andExpect(jsonPath("$.length()", is(10)))
                        .andExpect(jsonPath("$[0].title").value("프로도 제목 : 19"))
                        .andExpect(jsonPath("$[0].content").value("매지션 내용 : 19"))
                        .andDo(print());

        // then
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
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

        mockMvc.perform(patch("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                            .contentType(MediaType.APPLICATION_JSON) // 안쓰면 타입에러나서 415 에러남
                            .content(objectMapper.writeValueAsString(postEdit)))
                        .andExpect(status().isOk()) // 서버통신
                        .andDo(print());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("프로도")
                .content("관악봉천")
                .build();

        postRepository.save(post);

        mockMvc.perform(delete("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                            .contentType(MediaType.APPLICATION_JSON)) // 안쓰면 타입에러나서 415 에러남
                        .andExpect(status().isOk()) // 서버통신
                        .andDo(print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception{
        // given
//        Post post = Post.builder()
//                .title("프로도")
//                .content("관악봉천")
//                .build();
//
//        postRepository.save(post);

        mockMvc.perform(delete("/posts/{postId}", 1L) // PATCH /posts/{postId}
                        .contentType(MediaType.APPLICATION_JSON)) // 안쓰면 타입에러나서 415 에러남
                .andExpect(status().isNotFound()) // 서버통신
                .andDo(print());
    }
}