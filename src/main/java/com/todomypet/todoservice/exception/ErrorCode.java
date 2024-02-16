package com.todomypet.todoservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    WRONG_CATEGORY_ID(HttpStatus.BAD_REQUEST, "T001", "잘못된 카테고리 id입니다."),
    NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO(HttpStatus.BAD_REQUEST, "T002", "카테고리와 투두가 일치하지 않습니다."),
    CATEGORY_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "T003", "중복된 카테고리명입니다."),
    FEIGN_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "T004", "서버간 통신 중 에러가 발생했습니다."),
    WRONG_USER_AND_TODO(HttpStatus.FORBIDDEN, "T005", "사용자에게 해당 투두를 삭제할 권한이 없습니다."),
    DEFAULT_CATEGORY_CANT_DELETED(HttpStatus.BAD_REQUEST, "T006", "미분류 카테고리는 삭제할 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
