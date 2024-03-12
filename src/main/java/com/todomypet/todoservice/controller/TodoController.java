package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.todo.*;
import com.todomypet.todoservice.service.TodoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
                                                 @RequestBody AddTodoReqDTO addTodoReqList) {
        List<AddTodoResDTO> response = todoService.addTodo(userId, addTodoReqList);
        return new SuccessResDTO<List<AddTodoResDTO>>(response);
    }

    @Operation(summary = "투두 상세 보기", description = "할일의 상세 정보를 확인합니다.")
    @GetMapping("/todo/{todoId}")
    public SuccessResDTO<TodoDetailResDTO> getTodoDetail(@RequestHeader String userId, @PathVariable String todoId) {
        TodoDetailResDTO response = todoService.getTodoDetail(userId, todoId);
        return new SuccessResDTO<TodoDetailResDTO>(response);
    }

    @Operation(summary = "투두 수정", description = "투두 데이터를 업데이트합니다.")
    @PutMapping("/todo/{todoId}")
    public SuccessResDTO<String> updateTodo(@RequestHeader String userId, @PathVariable String todoId,
                                            @RequestBody UpdateTodoReqDTO updateInfos) {
        String response = todoService.updateTodo(userId, todoId, updateInfos);
        return new SuccessResDTO<String>(response);
    }

    @Operation(summary = "투두 달성", description = "투두를 달성합니다.")
    @PutMapping("/todo/clear")
    public SuccessResDTO<Void> clearTodo(@RequestHeader String userId,
                                         @RequestBody ClearTodoReqDTO clearTodoReqDTO) {
        todoService.clearTodo(userId, clearTodoReqDTO);
        return new SuccessResDTO<Void>(null);
    }

    @Operation(summary = "투두 달성 취소", description = "투두 달성을 취소합니다. 경험치 획득 내역은 변경되지 않습니다.")
    @PutMapping("/todo/unclear")
    public SuccessResDTO<Void> unclearTodo(@RequestHeader String userId,
                                       @RequestBody UnclearTodoReqDTO unclearTodoReqDTO) {
        todoService.unclearTodo(userId, unclearTodoReqDTO);
        return new SuccessResDTO<Void>(null);
    }

    @Operation(summary = "투두 삭제", description = "투두를 삭제합니다. 반복 옵션의 경우 기존 모든 투두가 삭제됩니다.")
    @DeleteMapping("/todo/{todoId}")
    public SuccessResDTO<String> deleteTodo(@RequestHeader String userId, @PathVariable String todoId) {
        String deletedTodoId = todoService.deleteTodo(userId, todoId);
        return new SuccessResDTO<String>(deletedTodoId);
    }

    @Operation(summary = "투두 반복 종료", description = "반복 설정된 투두를 종료합니다. 종료일 기준 이후 모든 반복 일정이 삭제됩니다. " +
                                                        "repeatEndDate는 YYYY-MM-dd 형태로 요청합니다.")
    @PutMapping("/todo/end-the-repeat")
    public SuccessResDTO<String> endTheRepeatTodo(@RequestHeader String userId,
                                                  @RequestBody EndTheRepeatTodoReqDTO endTheRepeatRequestInfo) {
        String deletedTodoId = todoService.endTheRepeatTodo(userId, endTheRepeatRequestInfo);
        return new SuccessResDTO<String>(deletedTodoId);
    }


    @Operation(summary = "월별 투두 조회", description = "월별 투두를 조회합니다. {month}는 YYYY-MM 형태로 조회하며, " +
                                                        "해당 월의 전후월 데이터를 함께 조회합니다.")
    @GetMapping("/todo/month/{month}")
    public SuccessResDTO<List<GetTodoByMonthResDTO>> getTodoByMonth(@RequestHeader String userId,
                                                                    @PathVariable String month) {
        List<GetTodoByMonthResDTO> response = todoService.getTodoByMonth(userId, month);
        return new SuccessResDTO<List<GetTodoByMonthResDTO>>(response);
    }

    @Operation(summary = "일별 투두 조회", description = "일별 투두를 조회합니다. {day}는 YYYY-MM-dd 형태로 조회합니다.")
    @PostMapping("/todo/daily/{day}")
    public SuccessResDTO<List<GetTodoByDayResDTO>> getTodoByDay(@RequestHeader String userId, @PathVariable String day) {
        List<GetTodoByDayResDTO> response = todoService.getTodoByDay(userId, day);
        return new SuccessResDTO<>(response);
    }

    @Hidden
    @PostMapping("/todo/get-by-alert-time")
    public SuccessResDTO<GetTodoByAlertTimeResListDTO> getByAlertTime(@RequestBody GetTodoByAlertTimeReqDTO req) {
        GetTodoByAlertTimeResListDTO response = GetTodoByAlertTimeResListDTO.builder()
                .response(todoService.getTodoByAlertAt(req.getAlertAt())).build();
        return new SuccessResDTO<GetTodoByAlertTimeResListDTO>(response);
    }

    @Hidden
    @DeleteMapping("/todo/delete-all-category-and-todo")
    public SuccessResDTO<List<GetTodoByAlertTimeResDTO>> deleteAllCategoryAndTodoByUserId(@RequestHeader String userId) {
        todoService.deleteAllCategoryAndTodoByUserId(userId);
        return new SuccessResDTO<>(null);
    }
}
