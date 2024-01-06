package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;
import com.todomypet.todoservice.dto.todo.ClearTodoReqDTO;
import com.todomypet.todoservice.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/todo")
    public SuccessResDTO<AddTodoResDTO> addTodo(@RequestHeader String userId,
                                                 @RequestBody AddTodoReqDTO addTodoReqDTO) {
        AddTodoResDTO response = todoService.addTodo(userId, addTodoReqDTO);
        return new SuccessResDTO<AddTodoResDTO>(response);
    }

    // todo: 로컬 테스트 필요
    @PostMapping("/todo/clear")
    public SuccessResDTO<Void> clearTodo(@RequestHeader String userId,
                                         @RequestBody ClearTodoReqDTO clearTodoReqDTO) {
        todoService.clearTodo(userId, clearTodoReqDTO);
        return new SuccessResDTO<Void>(null);
    }
}
