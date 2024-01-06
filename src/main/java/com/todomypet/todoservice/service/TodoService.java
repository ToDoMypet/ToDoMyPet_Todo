package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;
import com.todomypet.todoservice.dto.todo.ClearTodoReqDTO;

public interface TodoService {
    AddTodoResDTO addTodo(String userId, AddTodoReqDTO addTodoReqDTO);

    void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO);
}
