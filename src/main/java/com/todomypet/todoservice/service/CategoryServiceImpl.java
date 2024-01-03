package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.category.AddCategoryReqDTO;
import com.todomypet.todoservice.domain.node.Category;
import com.todomypet.todoservice.dto.category.AddCategoryResDTO;
import com.todomypet.todoservice.repository.CategoryRepository;
import com.todomypet.todoservice.repository.HaveRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final HaveRepository haveRepository;

    @Override
    @Transactional
    public AddCategoryResDTO addCategory(String userId, AddCategoryReqDTO addCategoryReqDTO) {
        Category category = Category.builder().name(addCategoryReqDTO.getName()).build();
        String savedCategoryId = categoryRepository.save(category).getId();

        haveRepository.createHaveRelationshipBetweenUserAndCategory(userId, savedCategoryId);

        return AddCategoryResDTO.builder().categoryId(savedCategoryId).build();
    }
}
