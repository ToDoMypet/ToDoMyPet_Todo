package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.node.ColorSet;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories
public interface ColorSetRepository extends Neo4jRepository<ColorSet, String> {

}
