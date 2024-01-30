package com.todomypet.todoservice.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetTodoByDayResDTO {
    private String categoryName;
    private String categoryColorCode;
    private List<TodoInGetTodoByDayDTO> todoList;
}
