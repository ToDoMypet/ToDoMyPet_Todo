package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.node.Todo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@EnableNeo4jRepositories
public interface TodoRepository extends Neo4jRepository<Todo, String> {
    @Query("MATCH (t:Todo{id:$todoId}) RETURN t.getExperiencePointOrNot")
    boolean isGetExperiencePoint(String todoId);

    @Query("MATCH (t:Todo{id:$todoId}) SET t.clearYN = true, t.getExperiencePointOrNot = true")
    void updateClearYNAndGetExperienceByTodoId(String todoId);
}
