package com.prodoblog.controller;

import com.prodoblog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SSR -> JSP, thymeleaf, mustache, freemaker
 *             // html 서버에서 렌더링
 * SPA ->
 * vue -> vue  + SSR = nuxt
 * react -> react + SSR = next
 * javascript  <-> API (JSON) 로 렌더링
 *
 * http method
 * GET, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, CONNECT, HEAD
 *
 * 단순처리
 * @RequestParam String title, @RequestParam String content
 * log.info("title={}, cotent={}", title, content);
 *
 * public String post(@RequestParam Map<String, String> params)
 * log.info("params={}", params);
 *
 * json는 @ModelAttribute 방식이 아닌 @RequestBody 방식으로 가져온다.
 */

@RestController
@Slf4j
public class PostController {

    @GetMapping("/getPosts")
    public String getPosts() {
        return "Hello World";
    }

    // 글등록
   @PostMapping("/posts")
   public String post(@RequestBody PostCreate params) throws Exception {
        // 데이터를 검증하는 이유

        // 1. 클라이언트 개발자가 깜박할 수 있음. 실수로 값을 안보낼 수 있다.
        // 2. bug로 누락될 수있다.
        // 3. 외부에서 조작해서 보낼수있다. (보안문제)
        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        // 5. 서버 개발자의 대한 편안함을 위해서

        log.info("params={}", params);
        
        String title = params.getTitle();
        String content = params.getContent();
        
        if(title == null || title.equals("")) {
            throw new Exception("타이틀값이 없음!");    
        }

        if(content == null || content.equals("")) {
            throw new Exception("제목이 없음!");
        }
        
        return "Hello World";
   }
}
