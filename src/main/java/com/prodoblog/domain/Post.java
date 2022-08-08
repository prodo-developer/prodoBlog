package com.prodoblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 10글자만 가져오도록
    public String getTitle() {
        // 서비스의 정책을 넣지마세요 절대!!!
//        return this.title.substring(0,10);
        return title;
    }

    // Setter 대신 직접 체인지하는 로직 만들기
    // String title, String content 가 아닌 String content, String title인 경우 버그 발견하기 힘듬.
//    public void change(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

    // 1. PostEditorBuilder이기때문에 즉, 빌더를 넘기기위해 빌더하지않은 .build를 시키면 안됨.
    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    // 2. fix된 post가 넘어옴
    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
