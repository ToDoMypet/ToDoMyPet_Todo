package com.todomypet.todoservice.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
