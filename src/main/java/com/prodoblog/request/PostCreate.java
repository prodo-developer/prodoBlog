package com.prodoblog.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {

    //@Valid에서 체크한 기준으로 빈값을 체크한다. null도 체크해줌.
    @NotBlank(message = "타이틀을 입력해주세요")
    public String title;

    @NotBlank(message = "내용을 입력해주세요")
    public String content;

}
