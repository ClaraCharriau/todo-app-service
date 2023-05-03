package com.project.todoapp.integration;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.project.todoapp.repository.ToDoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.UUID;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("it")
@DataJpaTest(excludeAutoConfiguration = TestDatabaseAutoConfiguration.class)
@DisplayName("Integration tests on ToDoListRepository")
class ToDoListRepositoryIT  {

    @Autowired
    private ToDoListRepository toDoListRepository;
    @Autowired
    protected DataSource dataSource;

    private UUID taskId;

    @BeforeEach
    void setDB() throws Exception {
        taskId = UUID.randomUUID();

        Operation operation =
                sequenceOf(
                        DbSetupCommonOperations.DELETE_ALL,
                        insertInto("Task")
                                .columns("id", "content", "category", "urgent")
                                .values(taskId, "task description", "bills", true)
                                .build()
                );
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();
    }

    @Test
    @DisplayName("Test should exists by address")
    void shouldExistsByAddress() {

        // when
        boolean existsById = toDoListRepository.existsById(taskId);

        // then
        assertThat(existsById).isTrue();
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
