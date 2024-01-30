package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;
import com.todomypet.todoservice.dto.todo.ClearTodoReqDTO;
import com.todomypet.todoservice.dto.todo.GetTodoByMonthResDTO;

import java.util.List;

public interface TodoService {
    AddTodoResDTO addTodo(String userId, AddTodoReqDTO addTodoReqDTO);

    void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO);

    List<GetTodoByMonthResDTO> getTodoByMonth(String userId, String month);
}
