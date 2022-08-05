package com.prodoblog.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostEdit {

    //@Valid에서 체크한 기준으로 빈값을 체크한다. null도 체크해줌.
    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder // 생성자가 쪽있어야 함
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
