package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.relationship.Include;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableNeo4jRepositories
public interface IncludeRepository extends Neo4jRepository<Include, Long> {

    @Query("MATCH (t:Todo{id:$todoId}) WITH t MATCH (c:Category{id:$categoryId}) " +
            "CREATE (c)-[:INCLUDE]->(t)")
    void createIncludeRelationshipBetweenCategoryAndTodo(String todoId, String categoryId);

    @Query("MATCH (c:Category{id:$categoryId}) WITH c MATCH (t:Todo{id:$todoId}) " +
            "RETURN EXISTS((c)-[:INCLUDE]->(t))")
    boolean existsIncludeRelationshipBetweenCategoryAndTodo(String categoryId, String todoId);

    @Query("MATCH (c:Category)-[i:INCLUDE]->(t:Todo{$todoId}) RETURN i")
    Optional<Include> getIncludeByTodoId(String todoId);

    @Query("MATCH (c:Category{id:$categoryId})-[i:INCLUDE]->(t:Todo{id:$todoId}) DELETE i")
    void deleteIncludeBetweenCategoryAndTodo(String categoryId, String todoId);
}
