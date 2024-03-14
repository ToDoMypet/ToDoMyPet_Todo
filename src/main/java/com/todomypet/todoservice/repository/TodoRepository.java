package com.todomypet.todoservice.repository;

import com.todomypet.todoservice.domain.node.AlertType;
import com.todomypet.todoservice.domain.node.Todo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@EnableNeo4jRepositories
public interface TodoRepository extends Neo4jRepository<Todo, String> {
    @Query("MATCH (t:Todo{id:$todoId}) RETURN t.getExperiencePointOrNot")
    boolean isGetExperiencePoint(String todoId);

    @Query("MATCH (t:Todo{id:$todoId}) SET t.clearYN = true, t.getExperiencePointOrNot = true")
    void updateClearYNAndGetExperienceByTodoId(String todoId);

    @Query("MATCH (u:User{id:$userId}) WITH u " +
            "MATCH (u)-[:HAVE]-(c:Category{id:$categoryId})-[i:INCLUDE]->(t:Todo) " +
            "return t")
    List<Todo> getAllTodoByCategoryId(String userId, String categoryId);

    @Query("MATCH (t:Todo{id:$todoId}) SET t.clearYN = false")
    void updateClearYNToUnclearByTodoId(String todoId);

    @Query("MATCH (t:Todo{id:$todoId}) DETACH DELETE t")
    void deleteTodoByTodoId(String todoId);

    @Query("MATCH (u:User{id:$userId}) WITH u " +
            "MATCH (u)-[:HAVE]->(c:Category)-[:INCLUDE]->(t:Todo{id:$todoId}) " +
            "RETURN t")
    Optional<Todo> getTodoByUserIdAndTodoId(String userId, String todoId);

    @Query("MATCH (u:User {id:$userId})-[:HAVE]->(c:Category)-[:INCLUDE]->(t:Todo) " +
            "WHERE ((date({year:$preYear, month:$preMonth, day:1}) <= t.startedAtDate <= date({year:$postYear, month:$postMonth, day:$dayOfPostMonth}) OR " +
            "date({year:$preYear, month:$preMonth, day:1}) <= t.endedAtDate <= date({year:$postYear, month:$postMonth, day:$dayOfPostMonth})) OR " +
            "(t.startedAtDate < date({year:$preYear, month:$preMonth, day:1}) AND t.endedAtDate > date({year:$postYear, month:$postMonth, day:$dayOfPostMonth}))) AND " +
            "t.markOnTheCalenderOrNot = true " +
            "RETURN DISTINCT t ORDER BY t.id ASC")
    List<Todo> getAllTodoByUserAndMonth(String userId, int preYear, int preMonth,
                                        int postYear, int postMonth, int dayOfPostMonth);

    @Query("MATCH (t:Todo) WHERE t.repeatCode = $repeatCode RETURN t")
    List<Todo> getTodoByRepeatCode(String repeatCode);

    @Query("MATCH (t:Todo{id:$todoId}) DETACH DELETE t")
    void deleteOneById(String todoId);

    @Query("MATCH (t:Todo{id:$todoId}) RETURN t")
    Optional<Todo> getOneTodoByTodoId(String todoId);

    @Query("MATCH (t:Todo{repeatCode:$repeatCode}) DETACH DELETE t")
    void deleteAllByRepeatCode(String repeatCode);

    @Query("MATCH (t:Todo{repeatCode:$repeatCode}) " +
            "WHERE t.startedAtDate >= date({year:$year, month:$month, day:$day}) DETACH DELETE t")
    void endTheRepeatTodoByRepeatCode(String repeatCode, int year, int month, int day);

    @Query("MATCH (u:User{id:$userId}) WITH u " +
            "MATCH (u)-[:HAVE]-(c:Category{id:$categoryId})-[i:INCLUDE]->(t:Todo) " +
            "WHERE t.startedAtDate = date({year:$year, month:$month, day:$day}) OR " +
            "(t.startedAtDate <= date({year:$year, month:$month, day:$day}) AND t.endedAtDate >= date({year:$year, month:$month, day:$day})) " +
            "return t")
    List<Todo> getAllTodoByCategoryIdAndDay(String userId, String categoryId, int year, int month, int day);

    @Query("MATCH (t:Todo{id:$todoId}) SET t.content = $content, " +
            "t.receiveAlert = $receiveAlert, t.markOnTheCalenderOrNot = $markOnTheCalenderOrNot, t.alertAt = $alertAt, " +
            "t.startedAtDate = $startedAtDate, t.startedAtTime = $startedAtTime, t.endedAtDate = $endedAtDate, t.endedAtTime = $endedAtTime, t.alertType = $alertType")
    void updateTodoByTodoId(String todoId, String content, boolean receiveAlert, boolean markOnTheCalenderOrNot,
                            LocalDateTime alertAt, LocalDate startedAtDate, AlertType alertType);

    @Query("MATCH (t:Todo{repeatCode:$repeatCode}) SET t.repeatEndDate = $repeatEndDate")
    void updateTodoRepeatEndDateByRepeatCode(String repeatCode, LocalDate repeatEndDate);

    @Query("MATCH (t:Todo{alertAt:$alertAt}) RETURN t")
    List<Todo> getAllTodoByAlertAt(LocalDateTime alertAt);

    @Query("MATCH (u:User{id:$userId}) WITH u MATCH (u)-[:HAVE]->(:Category)-[:INCLUDE]->(t:Todo) DETACH DELETE t")
    void deleteAllTodoByUserId(String userId);

    @Query("MATCH (t:Todo{id:$todoId}) SET t.startedAtTime = $startedAtTime, t.endedAtDate = $endedAtDate, t.endedAtTime = $endedAtTime")
    void updateTodoDateTimeInfo(@Param("startedAtTime") LocalTime startedAtTime,
                                @Param("endedAtDate") LocalDate endedAtDate,
                                @Param("endedAtTime") LocalTime endedAtTime);
}
