package com.todomypet.todoservice.domain.relationship;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

@Getter
@RelationshipProperties
public class Have {
    @RelationshipId
    private Long id;

    @Property("colorCode")
    private String colorCode;

    @Property("bgCode")
    private String bgCode;

    @Property("textCode")
    private String textCode;
}
