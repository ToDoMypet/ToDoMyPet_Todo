package com.todomypet.todoservice.dto.category;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateCategoryResDTO {
    private String categoryId;
    private boolean duplicatedOrNot;
}
