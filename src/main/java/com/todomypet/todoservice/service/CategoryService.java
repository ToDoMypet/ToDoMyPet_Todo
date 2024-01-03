package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.category.AddCategoryReqDTO;
import com.todomypet.todoservice.dto.category.AddCategoryResDTO;

public interface CategoryService {
    AddCategoryResDTO addCategory(String userId, AddCategoryReqDTO addCategoryReqDTO);
}
