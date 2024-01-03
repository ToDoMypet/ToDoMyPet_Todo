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
}
