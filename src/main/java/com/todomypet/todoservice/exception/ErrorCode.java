package com.todomypet.todoservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    WRONG_CATEGORY_ID(HttpStatus.BAD_REQUEST, "T001", "잘못된 카테고리 id입니다."),
    NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO(HttpStatus.BAD_REQUEST, "T002", "카테고리와 투두가 일치하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
