package com.todomypet.todoservice.domain.relationship;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

@RelationshipProperties
@Getter
public class Include {
    @RelationshipId
    private Long id;
}
