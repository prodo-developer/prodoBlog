package com.prodoblog.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor // 생성자 추가
// @Builder // 매개변수 순서를 검증하기 위해서 아래에 적는걸 추천
public class PostCreate {

    //@Valid에서 체크한 기준으로 빈값을 체크한다. null도 체크해줌.
    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder // 생성자가 쪽있어야 함
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 빌더의 장점
    // 1. 가독성이 좋다.
    // 2. 값생성에 대한 유연함 new ooo -> builder().oo.oo.build();
    // 3. 필요한 값만 받을 수 있다. // -> 오버로딩 가능한 조건
    // 4. 객체의 불변성
    public PostCreate changeTitle(String title) {
        return PostCreate.builder()
                .title(title)
                .content(content)
                .build();
    }
}
