package com.todomypet.todoservice.domain.relationship;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

@RelationshipProperties
public class Have {
    @RelationshipId
    private Long id;
}
