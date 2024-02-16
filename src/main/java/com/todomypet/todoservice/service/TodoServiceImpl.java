package com.todomypet.todoservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.todoservice.domain.node.RepeatType;
import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.domain.relationship.Have;
import com.todomypet.todoservice.dto.openFeign.UpdateExperiencePointReqDTO;
import com.todomypet.todoservice.dto.todo.*;
import com.todomypet.todoservice.exception.CustomException;
import com.todomypet.todoservice.exception.ErrorCode;
import com.todomypet.todoservice.repository.HaveRepository;
import com.todomypet.todoservice.repository.IncludeRepository;
import com.todomypet.todoservice.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final HaveRepository haveRepository;
    private final TodoRepository todoRepository;
    private final IncludeRepository includeRepository;
    private final PetServiceClient petServiceClient;


    @Override
    @Transactional
    public List<AddTodoResDTO> addTodo(String userId, AddTodoReqDTO todoInfoReqDTO) {

        List<AddTodoResDTO> response = new ArrayList<>();
        RepeatType repeatType = todoInfoReqDTO.getRepeatInfo().getRepeatType();
        List<Integer> repeatData = todoInfoReqDTO.getRepeatInfo().getRepeatData();

        StringBuilder repeatCode = new StringBuilder();
        Random rnd = new Random();

        while (true) {
            for (int i = 0; i < 2; i++) {
                repeatCode.append((char) (rnd.nextInt(26) + 65));
            }
            for (int i = 0; i < 9; i++) {
                repeatCode.append(rnd.nextInt(10));
            }

            if (todoRepository.getTodoByRepeatCode(repeatCode.toString()).isEmpty()) {
                break;
            }
        }

        for (TodoInfoReqDTO req : todoInfoReqDTO.getTodoInfos()) {
            if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, req.getCategoryId()) == null) {
                throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
            };

            Todo todo = Todo.builder().id(UlidCreator.getUlid().toString())
                    .content(req.getContent())
                    .startedAtDate(LocalDate.parse(req.getStartedAtDate()))
                    .startedAtTime(LocalTime.parse(req.getStartedAtTime()))
                    .endedAtDate(LocalDate.parse(req.getEndedAtDate()))
                    .endedAtTime(LocalTime.parse(req.getEndedAtTime()))
                    .receiveAlert(req.isReceiveAlert()).clearYN(false)
                    .getExperiencePointOrNot(false).markOnTheCalenderOrNot(req.isMarkOnTheCalenderOrNot())
                    .alertAt(req.getAlertAt())
                    .repeatType(repeatType)
                    .repeatData(repeatData)
                    .repeatCode(repeatCode.toString())
                    .build();

            String todoId = todoRepository.save(todo).getId();

            includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(todoId, req.getCategoryId());
            response.add(AddTodoResDTO.builder().todoId(todoId).build());
        }


        return response;
    }

    @Override
    @Transactional
    public void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO) {
        String categoryId = clearTodoReqDTO.getCategoryId();
        String todoId = clearTodoReqDTO.getTodoId();

        if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, categoryId) == null){
            throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
        }
        if (!includeRepository.existsIncludeRelationshipBetweenCategoryAndTodo(categoryId, todoId)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO);
        }

        if (!todoRepository.isGetExperiencePoint(todoId)) {
            try {
                String petSeq = petServiceClient.getMainPet(userId).getData();
                petServiceClient.updateExperiencePoint(userId, UpdateExperiencePointReqDTO.builder()
                        .petSeqId(petSeq).experiencePoint(5).build());
            } catch (Exception e) {
                throw new CustomException(ErrorCode.FEIGN_CLIENT_ERROR);
            }

        }

        todoRepository.updateClearYNAndGetExperienceByTodoId(clearTodoReqDTO.getTodoId());
    }

    @Override
    @Transactional
    public List<GetTodoByMonthResDTO> getTodoByMonth(String userId, String month) {
        List<GetTodoByMonthResDTO> response = new ArrayList<>();
        LocalDate searchTarget = LocalDate.parse(month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Todo> todos = todoRepository.getAllTodoByUserAndMonth(userId, searchTarget.getYear(),
                searchTarget.getMonthValue());

        for (Todo todo : todos) {
            Have have = haveRepository.getHaveByTodoId(todo.getId());
            response.add(GetTodoByMonthResDTO.builder().id(UUID.randomUUID().toString()).todoContent(todo.getContent())
                    .todoStartedAt(todo.getStartedAtDate().toString()).todoEndedAt(todo.getEndedAtDate().toString())
                    .categoryTextColorCode(have.getBgCode())
                    .categoryBgColorCode(have.getTextCode()).build());
        }

        return response;
    }

    @Override
    public List<GetTodoByDayResDTO> getTodoByDay(String userId, String day) {
        List<GetTodoByDayResDTO> response = new ArrayList<>();
        List<TodoInGetTodoByDayDTO> data1 = new ArrayList<>();
        data1.add(TodoInGetTodoByDayDTO.builder().content("투두마이펫 UI 디자인 작업").clearYN(false).alertAt(null).alertType(null).build());
        data1.add(TodoInGetTodoByDayDTO.builder().content("업무일지 작성하기").clearYN(true).alertAt("11:00:00").alertType(null).build());
        List<TodoInGetTodoByDayDTO> data2 = new ArrayList<>();
        response.add(GetTodoByDayResDTO.builder().categoryName("프로젝트").categoryColorCode("#FFC558")
                .todoList(data1).build());
        data2.add(TodoInGetTodoByDayDTO.builder().content("콘서트 티켓팅").clearYN(false).alertAt("11:00:00").alertType(null).build());
        data2.add(TodoInGetTodoByDayDTO.builder().content("홍대에서 동기 모임").clearYN(false).alertAt("15:00:00").alertType(null).build());
        response.add(GetTodoByDayResDTO.builder().categoryName("약속").categoryColorCode("#FF0DBB")
                .todoList(data2).build());
        return response;
    }

    @Override
    public void unclearTodo(String userId, UnclearTodoReqDTO unclearTodoReqDTO) {
        String categoryId = unclearTodoReqDTO.getCategoryId();
        String todoId = unclearTodoReqDTO.getTodoId();

        if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, categoryId) == null){
            throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
        }
        if (!includeRepository.existsIncludeRelationshipBetweenCategoryAndTodo(categoryId, todoId)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO);
        }

        todoRepository.updateClearYNToUnclearByTodoId(unclearTodoReqDTO.getTodoId());
    }

    @Override
    @Transactional
    public String deleteTodo(String userId, String todoId) {
        Todo todo = todoRepository.existsByUserIdAndTodoId(userId, todoId).orElseThrow(()
                -> new CustomException(ErrorCode.WRONG_USER_AND_TODO));
        todoRepository.deleteTodoByTodoId(todoId);
        return todo.getId();
    }
}
