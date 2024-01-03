package com.todomypet.todoservice.domain.node;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDateTime;

@Node("Todo")
public class Todo {
    @Id
    private String id;
    @Property("category")
    private String category;
    @Property("content")
    private String content;
    @Property("startedAt")
    private LocalDateTime startedAt;
    @Property("endedAt")
    private LocalDateTime endedAt;
    @Property("alertAt")
    private LocalDateTime alertAt;
    @Property("receiveAlert")
    private boolean receiveAlert;
    @Property("clearYN")
    private boolean clearYN;
    @Property("getExperiencePointOrNot")
    private boolean getExperiencePointOrNot;
    @Property("markOnTheCalenderOrNot")
    private boolean markOnTheCalenderOrNot;
}
