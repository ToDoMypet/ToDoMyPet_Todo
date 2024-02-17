package com.todomypet.todoservice.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class TodoDetailResDTO {
    private String todoId;
    private String content;
    private String categoryName;
    private String categoryColorCode;
    private LocalDate startedAtDate;
    private LocalTime startedAtTime;
    private LocalDate endedAtDate;
    private LocalTime endedAtTime;
}
