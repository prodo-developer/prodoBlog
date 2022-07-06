package com.prodoblog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc; // 컨트롤러에 요청

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // 글 제목
        // 글 내용
        // 사용자
            // id, user, level

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목 입니다.")
                        .param("content", "글 내용 입니다 prodo")
                )   // application/json
                .andExpect(status().isOk()) // 서버통신
                .andExpect(MockMvcResultMatchers.content().string("Hello World")) // 해당문자가 일치하는가?
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 json 방식으로 출력한다.")
    void jsonTest() throws Exception {
        // 글 제목
        // 글 내용

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // 안쓰면 타입에러나서 415 에러남
                        .content("{\"title\": \"제목 등록\", \"content\": \"제이슨 내용입니다.\"}")
                )   // application/json
                .andExpect(status().isOk()) // 서버통신
                .andExpect(MockMvcResultMatchers.content().string("{}")) // 해당문자가 일치하는가?
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수입니다.")
    void test2() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // 안쓰면 타입에러나서 415 에러남
                // {"title": ""}
                // {"title": null}
                        .content("{\"title\": null, \"content\": \"제이슨 내용입니다.\"}")
//                        .content("{\"title\": null, \"content\": null}") // 둘다 null 값일때
                )   // application/json
//                .andExpect(status().isOk()) // 서버통신
//                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요.")) // 해당문자가 일치하는가?
                .andExpect(status().isBadRequest()) // 서버통신
                .andExpect(jsonPath("$.code").value("400")) // 해당문자가 일치하는가?
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 해당문자가 일치하는가?
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요.")) // 해당문자가 일치하는가?
                .andDo(print());
    }
}