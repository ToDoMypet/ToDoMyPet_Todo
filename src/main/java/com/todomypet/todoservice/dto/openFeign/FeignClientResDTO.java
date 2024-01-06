package com.todomypet.todoservice.dto.openFeign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FeignClientResDTO<T> {
    private String code;
    private String message;
    private T data;
}
