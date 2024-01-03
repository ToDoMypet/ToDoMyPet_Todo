package com.todomypet.todoservice.domain.node;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("ColorSet")
@Builder
@Getter
public class ColorSet {
    @Id
    private String id;
    @Property("colorCode")
    private String colorCode;
    @Property("bgCode")
    private String bgCode;
    @Property("textCode")
    private String textCode;
}
