package com.todomypet.todoservice.dto.category;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddCategoryReqDTO {
    private String name;
}
