package com.todomypet.todoservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
