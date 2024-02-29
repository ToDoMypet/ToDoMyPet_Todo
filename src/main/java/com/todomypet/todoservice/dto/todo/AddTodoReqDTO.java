package com.todomypet.todoservice.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTodoReqDTO {
    private List<TodoInfoReqDTO> todoInfos;
    private RepeatInfoReqDTO repeatInfo;
}
