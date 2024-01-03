package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.node.Category;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.stereotype.Repository;

@EnableNeo4jRepositories
@Repository
public interface CategoryRepository extends Neo4jRepository<Category, String> {

}
