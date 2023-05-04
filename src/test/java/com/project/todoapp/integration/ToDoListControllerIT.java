package com.project.todoapp.integration;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.project.todoapp.dto.TaskDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Integration tests on ToDoListController")
class ToDoListControllerIT {

    private UUID taskId;
    private UUID taskId2;
    private LocalDateTime now;
    @LocalServerPort
    protected int port;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setDB() throws Exception {
        taskId = UUID.randomUUID();
        taskId2 = UUID.randomUUID();

        now = LocalDateTime.now();

        RestAssured.port = port;
        RestAssured.basePath = "/todolist";

        Operation operation =
                sequenceOf(
                        DbSetupCommonOperations.DELETE_ALL,
                        insertInto("Task")
                                .columns("id", "content", "category", "urgent", "done_date")
                                .values(taskId, "task description", "bills", true, now)
                                .values(taskId2, "task description 2", "cleaning", false, null)
                                .build()
                );

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();
    }

    @Test
    @DisplayName("should get all tasks")
    void shouldGetAllTasks() {
        when()
                .get()
        .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("[0].id", is(taskId.toString()))
                .body("[0].content", is("task description"))
                .body("[0].category", is("bills"))
                .body("[0].urgent", is(true))
                .body("[0].doneDate", is(now.toString()))
                .body("[1].id", is(taskId2.toString()))
                .body("[1].content", is("task description 2"))
                .body("[1].category", is("cleaning"))
                .body("[1].urgent", is(false));
    }

    @Test
    @DisplayName("should throw zero task found exception")
    void shouldThrowZeroTaskFoundException() {
        Operation emptyDB = DbSetupCommonOperations.DELETE_ALL;

        DbSetup specialDBSetup = new DbSetup(new DataSourceDestination(dataSource), emptyDB);
        specialDBSetup.launch();

        when()
                .get("")
                .then()
                .log().ifValidationFails()
                .statusCode(404)
                .body("code", is("404"))
                .body("message", is("Zero task found."))
                .body("requestedURI", is("/todolist"));
    }

    @Test
    @DisplayName("should get a task")
    void shouldGetATask() {
        given()
                .pathParam("taskId", taskId)
        .when()
                .get("/{taskId}")
        .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", is(taskId.toString()))
                .body("content", is("task description"))
                .body("category", is("bills"))
                .body("urgent", is(true))
                .body("doneDate", is(now.toString()));
    }

    @Test
    @DisplayName("should throw exception if task don't exists on get")
    void shouldThrowExceptionIfTaskDontExistsOnGet() {
        UUID id = UUID.randomUUID();

        given()
                .pathParam("taskId", id)
        .when()
                .get("/{taskId}")
        .then()
                .log().ifValidationFails()
                .statusCode(404)
                .body("code", is("404"))
                .body("message", containsString("Task with id : " + id + " not found."))
                .body("requestedURI", containsString("/todolist/" + id));
    }

    @Test
    @DisplayName("should create a task")
    void shouldCreateATask() {
        UUID uuid = UUID.randomUUID();
        TaskDto newTask = TaskDto.builder().id(uuid).content("new task").category("other").urgent(true).build();

        given()
                .body(newTask)
                .contentType(ContentType.JSON)
        .when()
                .post()
        .then()
                .log().ifValidationFails()
                .statusCode(201);
    }

    @Test
    @Disabled
    @DisplayName("should throw exception if already exists in a put")
    void shouldThrowExceptionIfAlreadyExistsInAPut() {
        // TODO: Create already exists exception
    }

    @Test
    @DisplayName("should update a task")
    void shouldUpdateATask() {

        TaskDto updateTask = TaskDto.builder()
                    .id(taskId)
                    .content("update task")
                    .category("other")
                    .urgent(false)
                .build();

        given()
                .pathParam("taskId", taskId)
                .body(updateTask)
                .contentType(ContentType.JSON)
        .when()
                .patch("/{taskId}")
        .then()
                .log().ifValidationFails()
                .statusCode(201);
    }

    @Test
    @DisplayName("should delete a task")
    void shouldDeleteATask() {
        given()
                .pathParam("taskId", taskId)
        .when()
                .delete("/{taskId}")
        .then()
                .log().ifValidationFails()
                .statusCode(204);
    }

    @Test
    @DisplayName("should throw exception if task dont exists in a delete")
    void shouldThrowExceptionIfDontExistsInADelete() {
        UUID id = UUID.randomUUID();

        given()
                .pathParam("taskId", id)
        .when()
                .delete("/{taskId}")
        .then()
                .log().ifValidationFails()
                .statusCode(404)
                .body("code", is("404"))
                .body("message", containsString("Task with id : " + id + " not found."))
                .body("requestedURI", containsString("/todolist/" + id));
    }


    /**
     * Db Setup configuration.
     */
    protected static final DbSetupTracker DB_SETUP_TRACKER = new DbSetupTracker();

    protected void initDbSetup(Operation... operations) {
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), Operations.sequenceOf(operations));
        DB_SETUP_TRACKER.launchIfNecessary(dbSetup);
    }
    protected final void skipNextDbLaunch() {
        DB_SETUP_TRACKER.skipNextLaunch();
    }

}
