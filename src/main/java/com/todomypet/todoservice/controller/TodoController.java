package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;
import com.todomypet.todoservice.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/todo")
    public SuccessResDTO<AddTodoResDTO> addTodo (@RequestHeader String userId,
                                                 @RequestBody AddTodoReqDTO addTodoReqDTO) {
        AddTodoResDTO response = todoService.addTodo(userId, addTodoReqDTO);
        return new SuccessResDTO<AddTodoResDTO>(response);
    }
}
