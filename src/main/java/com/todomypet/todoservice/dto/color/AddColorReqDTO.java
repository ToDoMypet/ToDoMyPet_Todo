package com.todomypet.todoservice.dto.color;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Property;

@Builder
@Getter
public class AddColorReqDTO {
    private String colorCode;
    private String bgCode;
    private String textCode;
}
