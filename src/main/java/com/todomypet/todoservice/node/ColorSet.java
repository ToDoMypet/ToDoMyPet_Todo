package com.todomypet.todoservice.node;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("ColorSet")
public class ColorSet {
    @Id
    private String id;
    @Property("category")
    private String category;
    @Property("colorCode")
    private String colorCode;
    @Property("bgCode")
    private String bgCode;
    @Property("textCode")
    private String textCode;
}
