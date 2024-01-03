package com.todomypet.todoservice.domain.node;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
@Builder
@Getter
public class Category {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    @Property("name")
    private String name;

}
