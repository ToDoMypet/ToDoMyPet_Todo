package com.todomypet.todoservice.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryReqDTO {
    private String name;
    private String colorCode;
}
