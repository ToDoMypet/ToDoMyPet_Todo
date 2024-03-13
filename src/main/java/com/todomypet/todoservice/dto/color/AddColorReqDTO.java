package com.todomypet.todoservice.dto.color;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddColorReqDTO {
    private String colorCode;
    private String bgCode;
    private String textCode;
}
