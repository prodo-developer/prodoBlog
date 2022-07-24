package com.prodoblog.controller;

import com.prodoblog.domain.Post;
import com.prodoblog.request.PostCreate;
import com.prodoblog.response.PostResponse;
import com.prodoblog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
 *
 * 단순검사
 *
 *         String title = params.getTitle();
 *         String content = params.getContent();
 *
 *
 * if(title == null || title.equals("")) {
 *   throw new Exception("타이틀값이 없음!");
 * }
 *
 * if(content == null || content.equals("")) {
 *   throw new Exception("제목이 없음!");
 * }
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/getPosts")
    public String getPosts() {
        return "Hello World";
    }

    // 글등록
//    @PostMapping("/posts")
//    public Map<String, String> post(@RequestBody @Valid PostCreate request){
//        // 데이터를 검증하는 이유
//
//        // 1. 클라이언트 개발자가 깜박할 수 있음. 실수로 값을 안보낼 수 있다.
//        // 2. bug로 누락될 수있다.
//        // 3. 외부에서 조작해서 보낼수있다. (보안문제)
//        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
//        // 5. 서버 개발자의 대한 편안함을 위해서
//
////        if(result.hasErrors()) {
////            List<FieldError> fieldErrors = result.getFieldErrors();
////            FieldError fieldFirstError = fieldErrors.get(0);
////            String fieldName = fieldFirstError.getField(); //title
////            String errorMessage = fieldFirstError.getDefaultMessage(); // 에러메시지
////
////            Map<String, String> error = new HashMap<>();
////            error.put(fieldName, errorMessage);
////            return error;
////        }
//
//        log.info("request={}", request);
//        postService.write(request);
//
//        return Map.of();
//    }

    // @ControllerAdvice를 통해 모든 컨트롤러를 검증통제 가능.

    // 글등록
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request){
        // 데이터를 검증하는 이유

        log.info("request={}", request);
        // case1. 저장한 데이터 Entity -> response로 응답하기
        // case2. 저장한 데이터에 primary_id -> response로 응답하기
        //      Client에는 수신한 id를 post 조회 API를 통해서 글 데이터를 수신받음
        // Bad Case: 서버에서 반드시 이렇게 할겁니다 fix
        //              -> 서버에서 유연하게 대응하는것이 좋습니다.
        //              -> 한번에 일괄적으로 처리되는 케이스가 없습니다. 잘 관리하는 형태가 필요.
        postService.write(request);
    }

    /**
     * /posts 글전 체 조회(거ㅏㅁ색+ 페이징)
     * /posts/{postId} -> 글 한개만 조회
     * (name= "postId") 안적으면 그냥 아이디로 postId 해도됨
     *
     * josn 형태로 반환
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name= "postId") Long id) {
        PostResponse response = postService.get(id);
        // 응답클래스를 분리하세요 (서비스 정책에 맞게)
        return response;
    }

    // 같은 정책으로 체크할경우 동일하게 들어감(타이틀값 테스트트)
   @GetMapping("/posts/{postId}/rss")
    public Post getRss(@PathVariable(name= "postId") Long id) {
        Post post = postService.getRss(id);
        return post;
    }
}
