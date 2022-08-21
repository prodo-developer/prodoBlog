package com.prodoblog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code": "400"
 *     "message: "잘못된 요청입니다.",
 *
 *     아래만 예시 생략
 *     "validation": {
 *         "title" : "타이틀을 입력해주세요."
 *         "content" : "내용을 입력해주세요."
 *     }
 * }
 */
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // validation를 보고싶지 않을때, 비어이지 않는 데이터만 보여줌
public class ErrorResponse {

    private final String code;
    private final String message;
    
    // null 일수 있어서 해쉬맵으로 셋팅
    private final Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
