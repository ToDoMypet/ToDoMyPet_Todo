package com.todomypet.todoservice.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoReqDTO {
    private List<TodoInfoReqDTO> todoInfos;
    private RepeatInfoReqDTO repeatInfo;
    private LocalDate editFrom;
}
