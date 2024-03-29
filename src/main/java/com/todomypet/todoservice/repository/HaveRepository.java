package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.relationship.Have;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@EnableNeo4jRepositories
public interface HaveRepository extends Neo4jRepository<Have, Long> {
    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (c:Category{id:$categoryId}) " +
            "CREATE (u)-[:HAVE{colorCode:$colorCode, bgCode:$bgCode, textCode:$textCode}]->(c)")
    void createHaveRelationshipBetweenUserAndCategory(String userId, String categoryId,
                                                      String colorCode, String bgCode, String textCode);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (u)-[h:HAVE]->(c:Category{id:$categoryId}) " +
            "RETURN h{.colorCode}")
    Have existsHaveRelationshipBetweenUserAndCategory(String userId, String categoryId);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (u)-[h:HAVE]->(c:Category{name:$categoryName}) " +
            "RETURN h")
    Have existsHaveRelationshipBetweenUserAndCategoryName(String userId, String categoryName);

    @Query("MATCH (:User)-[h:HAVE]->(:Category)-[:INCLUDE]->(:Todo{id:$todoId}) " +
            "RETURN h{.bgCode, .textCode}")
    Have getHaveByTodoId(String todoId);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (u)-[h:HAVE]->(c:Category{id:$categoryId}) " +
            "SET h.colorCode = $colorCode, h.bgCode = $bgCode, h.textCode = $textCode")
    void updateHaveRelationshipColor(String userId, String categoryId,
                                     String colorCode, String bgCode, String textCode);
}
