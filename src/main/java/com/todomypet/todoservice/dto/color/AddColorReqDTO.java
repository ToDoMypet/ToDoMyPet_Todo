package com.todomypet.todoservice.dto.color;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddColorReqDTO {
    private String colorCode;
    private String bgCode;
    private String textCode;
}
