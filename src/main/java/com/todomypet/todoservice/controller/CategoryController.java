package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.category.AddCategoryReqDTO;
import com.todomypet.todoservice.dto.category.AddCategoryResDTO;
import com.todomypet.todoservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public SuccessResDTO<AddCategoryResDTO> addColor(@RequestHeader String userId,
                                                     @RequestBody AddCategoryReqDTO addCategoryReqDTO) {
        AddCategoryResDTO response = categoryService.addCategory(userId, addCategoryReqDTO);
        return new SuccessResDTO<AddCategoryResDTO>(response);
    }
}
