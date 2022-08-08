package com.prodoblog.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;
    private final String content;


    // 생성자 안에서 필터링도 가능하다.
//    this.title = title != null ? title;
//    this.content = content != null ? content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
