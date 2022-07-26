package com.prodoblog.response;

import com.prodoblog.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
//@Builder
//@RequiredArgsConstructor
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    // 생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    // 방법1 빌더를 직접 메서드에 달아주고 필터링하기
    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
//        this.title = title.substring(0,10);
        this.title = title.substring(0,Math.min(title.length(), 10));
        this.content = content;
    }

    // 방법2 전역변수에 @Builder달아주고, 직접필터링하기
//    public String getTitle() {
//        return this.title.substring(0,10);
//    }
}
