package com.prodoblog.exception;

public class InvalidRequest extends ProdologException{

    private static final String MESSAGE = "잘못된 요청 입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
