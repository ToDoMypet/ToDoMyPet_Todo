package com.todomypet.todoservice.dto;

import lombok.Getter;

@Getter
public class SuccessResDTO<T> {
    public static final String CODE = "001";
    public static final String MESSAGE = "성공했습니다.";

    private final String code;
    private final String message;
    private final T data;

    public SuccessResDTO(T data) {
        this.code = CODE;
        this.message = MESSAGE;
        this.data = data;
    }
}
