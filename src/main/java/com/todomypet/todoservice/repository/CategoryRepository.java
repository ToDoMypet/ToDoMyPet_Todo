package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.node.Category;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableNeo4jRepositories
@Repository
public interface CategoryRepository extends Neo4jRepository<Category, String> {

    @Query("MATCH (u:User{id:$userId}) WITH u " +
            "MATCH (u)-[:HAVE]->(c:Category{name:\"미분류\"}) RETURN c")
    Category getDefaultCategoryIdByUserId(String userId);

    @Query("MATCH (c:Category{id:$categoryId}) DETACH DELETE c")
    void deleteCategoryById(String categoryId);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (u)-[:HAVE]->(c:Category) RETURN c ORDER BY c.id DESC")
    List<Category> getCategoryListByUserId(String userId);
}
