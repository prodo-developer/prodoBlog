package com.prodoblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
   public String post(@RequestParam Map<String, String> params) {
        log.info("params={}", params);
        return "Hello World";
   }
}
