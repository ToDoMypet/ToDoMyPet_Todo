package com.todomypet.todoservice.dto.todo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetTodoByAlertTimeResDTO {
    private String todoId;
    private String todoContent;
}
