package com.prodoblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    // @ResponseBody에게 모델을 뷰로 렌더링하지 ㅇ낳고 반환된 객체를 응답 본문에 쓰도록 지시합니다.
    // @ResponseBody가 Map을 꺼내서 리턴합니다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, String> invalidRequestHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();

        HashMap<String, String> response = new HashMap<>();
        response.put(field, message);
        log.info("exceptionHandler error=", e);

        return response;
    }
}
