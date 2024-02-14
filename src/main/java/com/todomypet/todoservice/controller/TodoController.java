package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.todo.*;
import com.todomypet.todoservice.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Todo Controller")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @Operation(summary = "투두 추가", description = "새로운 투두를 추가합니다.")
    @PostMapping("/todo")
    public SuccessResDTO<List<AddTodoResDTO>> addTodo(@RequestHeader String userId,
                                                 @RequestBody List<AddTodoReqDTO> addTodoReqList) {
        List<AddTodoResDTO> response = todoService.addTodo(userId, addTodoReqList);
        return new SuccessResDTO<List<AddTodoResDTO>>(response);
    }

    @Operation(summary = "투두 달성", description = "투두를 달성합니다.")
    @PostMapping("/todo/clear")
    public SuccessResDTO<Void> clearTodo(@RequestHeader String userId,
                                         @RequestBody ClearTodoReqDTO clearTodoReqDTO) {
        todoService.clearTodo(userId, clearTodoReqDTO);
        return new SuccessResDTO<Void>(null);
    }

    @Operation(summary = "월별 투두 조회", description = "월별 투두를 조회합니다. {month}는 YYYY-MM 형태로 조회하며, " +
                                                        "해당 월의 전후월 데이터를 함께 조회합니다. **현재 가데이터**")
    @GetMapping("/todo/month/{month}")
    public SuccessResDTO<List<GetTodoByMonthResDTO>> getTodoByMonth(@RequestHeader String userId,
                                                                    @PathVariable String month) {
        List<GetTodoByMonthResDTO> response = todoService.getTodoByMonth(userId, month);
        return new SuccessResDTO<List<GetTodoByMonthResDTO>>(response);
    }

    @Operation(summary = "일별 투두 조회", description = "일별 투두를 조회합니다. {day}는 YYYY-MM-dd 형태로 조회합니다. **현재 가데이터**")
    @GetMapping("/todo/daily/{day}")
    public SuccessResDTO<List<GetTodoByDayResDTO>> getTodoByDay(@RequestHeader String userId, @PathVariable String day) {
        List<GetTodoByDayResDTO> response = todoService.getTodoByDay(userId, day);
        return new SuccessResDTO<>(response);
    }
}
