package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.node.Todo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableNeo4jRepositories
public interface TodoRepository extends Neo4jRepository<Todo, String> {
}
