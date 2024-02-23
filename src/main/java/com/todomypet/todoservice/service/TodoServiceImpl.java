package com.todomypet.todoservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.todoservice.domain.node.Category;
import com.todomypet.todoservice.domain.node.RepeatType;
import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.domain.relationship.Have;
import com.todomypet.todoservice.domain.relationship.Include;
import com.todomypet.todoservice.dto.openFeign.UpdateExperiencePointReqDTO;
import com.todomypet.todoservice.dto.todo.*;
import com.todomypet.todoservice.exception.CustomException;
import com.todomypet.todoservice.exception.ErrorCode;
import com.todomypet.todoservice.repository.CategoryRepository;
import com.todomypet.todoservice.repository.HaveRepository;
import com.todomypet.todoservice.repository.IncludeRepository;
import com.todomypet.todoservice.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CategoryRepository categoryRepository;


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

            Todo.TodoBuilder todoBuilder = Todo.builder().id(UlidCreator.getUlid().toString())
                .content(req.getContent())
                .startedAtDate(LocalDate.parse(req.getStartedAtDate()))
                .receiveAlert(req.isReceiveAlert()).clearYN(false)
                .getExperiencePointOrNot(false).markOnTheCalenderOrNot(req.isMarkOnTheCalenderOrNot())
                .alertAt(req.getAlertAt())
                .alertType(req.getAlertType())
                .repeatType(repeatType)
                .repeatData(repeatData)
                .repeatCode(repeatCode.toString());

            if (req.getStartedAtTime() != null) {
                todoBuilder.startedAtTime(LocalTime.parse(req.getStartedAtTime()));
            }
            if (req.getEndedAtDate() != null) {
                todoBuilder.endedAtDate(LocalDate.parse(req.getEndedAtDate()));
            }
            if (req.getEndedAtTime() != null) {
                todoBuilder.endedAtTime(LocalTime.parse(req.getEndedAtTime()));
            }
            if (todoInfoReqDTO.getRepeatInfo().getRepeatEndDate() != null) {
                todoBuilder.repeatStartDate(todoInfoReqDTO.getRepeatInfo().getRepeatEndDate());
            }
            if (todoInfoReqDTO.getRepeatInfo().getRepeatStartDate() != null) {
                todoBuilder.repeatEndDate(todoInfoReqDTO.getRepeatInfo().getRepeatStartDate());
            }

            Todo todo = todoBuilder.build();

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
        Todo todo = todoRepository.getOneTodoByTodoId(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_TODO_ID));

        if (todo.isClearYN()) {
            throw new CustomException(ErrorCode.ALREADY_CLEAR_TODO);
        }

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

        List<Todo> todos = todoRepository.getAllTodoByUserAndMonth(userId, searchTarget.getYear(), searchTarget.getMonthValue());

        for (Todo todo : todos) {
            Have have = haveRepository.getHaveByTodoId(todo.getId());
            if (todo.getEndedAtDate() == null) {
                response.add(GetTodoByMonthResDTO.builder().id(todo.getId()).todoContent(todo.getContent())
                        .todoStartedAt(todo.getStartedAtDate().toString()).todoEndedAt(null)
                        .categoryTextColorCode(have.getBgCode())
                        .categoryBgColorCode(have.getTextCode()).build());
            } else {
                response.add(GetTodoByMonthResDTO.builder().id(todo.getId()).todoContent(todo.getContent())
                        .todoStartedAt(todo.getStartedAtDate().toString()).todoEndedAt(todo.getEndedAtDate().toString())
                        .categoryTextColorCode(have.getBgCode())
                        .categoryBgColorCode(have.getTextCode()).build());
            }
        }

        return response;
    }

    @Override
    @Transactional
    public List<GetTodoByDayResDTO> getTodoByDay(String userId, String day) {
        List<GetTodoByDayResDTO> response = new ArrayList<>();

        LocalDate date = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Category> categories = categoryRepository.getCategoryListByUserId(userId);

        categories.forEach(category -> {
            List<TodoInGetTodoByDayDTO> todos = new ArrayList<>();
            List<Todo> todosForCategory = todoRepository.getAllTodoByCategoryIdAndDay(userId, category.getId(),
                    date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Have have = haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, category.getId());

            if (!todosForCategory.isEmpty()) {
                todosForCategory.forEach(todo -> {
                    todos.add(TodoInGetTodoByDayDTO.builder()
                            .todoId(todo.getId())
                            .content(todo.getContent())
                            .clearYN(todo.isClearYN())
                            .startedAtTime(todo.getStartedAtTime() != null ? String.valueOf(todo.getStartedAtTime()) : null)
                            .alertType(todo.getAlertType())
                            .build());
                });

                response.add(GetTodoByDayResDTO.builder()
                        .categoryId(category.getId())
                        .categoryName(category.getName())
                        .categoryColorCode(have != null ? have.getColorCode() : null)
                        .todoList(todos)
                        .build());
            }
        });

        return response;
    }

    @Override
    public void unclearTodo(String userId, UnclearTodoReqDTO unclearTodoReqDTO) {
        String categoryId = unclearTodoReqDTO.getCategoryId();
        String todoId = unclearTodoReqDTO.getTodoId();

        Todo todo = todoRepository.getOneTodoByTodoId(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_TODO_ID));

        if (!todo.isClearYN()) {
            throw new CustomException(ErrorCode.NOT_CLEARED_TODO);
        }

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

        if (todo.getRepeatType() == RepeatType.NONE_REPEAT) {
            todoRepository.deleteOneById(todoId);
        } else {
            String repeatCode = todo.getRepeatCode();
            todoRepository.deleteAllByRepeatCode(repeatCode);
        }

        return todo.getId();
    }

    @Override
    @Transactional
    public TodoDetailResDTO getTodoDetail(String userId, String todoId) {
        Todo todo = todoRepository.existsByUserIdAndTodoId(userId, todoId).orElseThrow(()
                -> new CustomException(ErrorCode.WRONG_USER_AND_TODO));
        Category category = categoryRepository.getCategoryByTodoId(todoId).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_EXISTS_CATEGORY));
        Have have = haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, category.getId());
        return TodoDetailResDTO.builder().todoId(todo.getId()).content(todo.getContent())
                .categoryName(category.getName()).categoryColorCode(have.getColorCode())
                .startedAtDate(todo.getStartedAtDate()).startedAtTime(todo.getStartedAtTime())
                .markOnTheCalenderOrNot(todo.isMarkOnTheCalenderOrNot())
                .alertAt(todo.getAlertAt()).alertType(todo.getAlertType())
                .repeatType(todo.getRepeatType()).repeatData(todo.getRepeatData())
                .endedAtDate(todo.getEndedAtDate()).endedAtTime(todo.getEndedAtTime()).build();
    }

    @Override
    @Transactional
    public String updateTodo(String userId, String todoId, UpdateTodoReqDTO updateInfos) {
//        Todo todo = todoRepository.getOneTodoByTodoId(todoId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_TODO_ID));
//
//        // 카테고리 수정되는 경우 고려해야 함
//
//        if (todo.getRepeatType() == RepeatType.NONE_REPEAT) {
//            if (includeRepository.existsIncludeRelationshipBetweenCategoryAndTodo(categoryId, todoId))
//            todoRepository.updateTodoByTodoId();
//            return null;
//        }
//
//        if (updateInfos.isUpdatePastRepeatDataOrNot()) {
//            // 이전 데이터 함께 업데이트
//        } else {
//            // 이후 데이터만 업데이트
//        }

        return null;
    }

    @Override
    @Transactional
    public String endTheRepeatTodo(String userId, String todoId) {
        Todo todo = todoRepository.existsByUserIdAndTodoId(userId, todoId).orElseThrow(()
                -> new CustomException(ErrorCode.WRONG_USER_AND_TODO));

        String repeatCode = todo.getRepeatCode();
        todoRepository.endTheRepeatTodoByRepeatCode(repeatCode);

        return todoId;
    }
}
