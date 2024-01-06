package com.todomypet.todoservice.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClearTodoReqDTO {
    private String categoryId;
    private String todoId;
}
