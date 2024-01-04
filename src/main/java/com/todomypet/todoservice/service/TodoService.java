package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;

public interface TodoService {
    AddTodoResDTO addTodo(String userId, AddTodoReqDTO addTodoReqDTO);
}
