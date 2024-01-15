package com.todomypet.todoservice.service;

import com.todomypet.todoservice.domain.node.ColorSet;
import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.dto.category.AddCategoryReqDTO;
import com.todomypet.todoservice.domain.node.Category;
import com.todomypet.todoservice.dto.category.AddCategoryResDTO;
import com.todomypet.todoservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final HaveRepository haveRepository;
    private final ColorSetRepository colorSetRepository;
    private final TodoRepository todoRepository;
    private final IncludeRepository includeRepository;

    @Override
    @Transactional
    public AddCategoryResDTO addCategory(String userId, AddCategoryReqDTO addCategoryReqDTO) {
        Category category = Category.builder().name(addCategoryReqDTO.getName()).build();
        String savedCategoryId = categoryRepository.save(category).getId();

        ColorSet colorSet = colorSetRepository.getColorSetByColorCode(addCategoryReqDTO.getColorCode());

        haveRepository.createHaveRelationshipBetweenUserAndCategory(userId, savedCategoryId,
                colorSet.getColorCode(), colorSet.getBgCode(), colorSet.getTextCode());


        return AddCategoryResDTO.builder().categoryId(savedCategoryId).build();
    }

    @Override
    @Transactional
    public void deleteCategory(String userId, String categoryId) {
        List<Todo> todoList = todoRepository.getAllTodoByCategoryId(userId, categoryId);
        String defaultCategoryId = categoryRepository.getDefaultCategoryIdByUserId(userId).getId();
        for (Todo todo : todoList) {
            includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(todo.getId(), defaultCategoryId);
            categoryRepository.deleteCategoryById(categoryId);
        }
    }
}
