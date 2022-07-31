package com.prodoblog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

// @Builder.Default는 생성자 레벨에서 @Builder를 사용하지않고 전역변수(클래스 빌더)로 빼야합니다.
//@Data // getter, setter 생성
@Getter
@Setter
@Builder
public class PostSearch {

    public static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }

    // 페이징 처리
    public long getOffset() {
        return (long) (max(1, page) -1) * min(size, MAX_SIZE);
    }
}
