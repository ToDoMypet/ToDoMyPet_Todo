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
    WRONG_USER_AND_TODO(HttpStatus.FORBIDDEN, "T005", "사용자에게 해당 투두를 접근할 권한이 없습니다."),
    DEFAULT_CATEGORY_CANT_DELETED(HttpStatus.BAD_REQUEST, "T006", "미분류 카테고리는 삭제할 수 없습니다."),
    NOT_EXISTS_TODO_ID(HttpStatus.BAD_REQUEST, "T007", "해당 id를 가진 todo가 존재하지 않습니다."),
    NOT_EXISTS_CATEGORY(HttpStatus.INTERNAL_SERVER_ERROR, "T008", "카테고리가 존재하지 않습니다."),
    DEFAULT_CATEGORY_CANT_UPDATED(HttpStatus.BAD_REQUEST, "T009", "미분류 카테고리는 수정할 수 없습니다."),
    ALREADY_CLEAR_TODO(HttpStatus.BAD_REQUEST, "T010", "이미 달성한 todo입니다."),
    NOT_CLEARED_TODO(HttpStatus.BAD_REQUEST, "T011", "달성되지 않은 todo입니다."),
    CANT_FIND_TODOS_CATEGORY_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "T012", "해당 todo의 카테고리 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
