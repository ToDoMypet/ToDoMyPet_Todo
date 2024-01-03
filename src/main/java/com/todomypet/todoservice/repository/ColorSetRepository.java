package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.node.ColorSet;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorSetRepository extends Neo4jRepository<ColorSet, String> {
    @Query("MATCH (c:ColorSet) RETURN c")
    List<ColorSet> getAllColors();

    @Query("MATCH (c:ColorSet{colorCode:$colorCode}) RETURN c")
    ColorSet getColorSetByColorCode(String colorCode);
}
