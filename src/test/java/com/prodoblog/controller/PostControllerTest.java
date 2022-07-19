package com.prodoblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prodoblog.domain.Post;
import com.prodoblog.repository.PostRepository;
import com.prodoblog.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}