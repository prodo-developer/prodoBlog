package com.prodoblog.exception;

public abstract class ProdologException extends RuntimeException{

    public ProdologException(String message) {
        super(message);
    }

    public ProdologException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
