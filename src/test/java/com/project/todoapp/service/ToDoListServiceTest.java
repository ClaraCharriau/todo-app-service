package com.project.todoapp.service;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.exception.TaskNotFoundException;
import com.project.todoapp.exception.ZeroTaskFoundException;
import com.project.todoapp.mapper.TaskMapper;
import com.project.todoapp.model.TaskEntity;
import com.project.todoapp.repository.ToDoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Todo Service Tests")
@ExtendWith(MockitoExtension.class)
class ToDoListServiceTest {

    @InjectMocks
    ToDoListService toDoListService;
    @Mock
    ToDoListRepository toDoListRepository;
    @Mock
    TaskMapper taskMapper;
    TaskEntity taskEntity;
    TaskDto taskDto;
    UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        taskEntity = TaskEntity.builder()
                .id(taskId)
                .content("pay the bills")
                .doneDate(now)
                .category("bills")
                .urgent(true)
                .build();

        taskDto = TaskDto.builder()
                .id(taskId)
                .content("pay the bills")
                .doneDate(now)
                .category("bills")
                .urgent(true)
                .build();
    }

    @Test
    @DisplayName("should get all tasks")
    void should_get_all_tasks() throws ZeroTaskFoundException {
        // Given
        UUID taskId2 = UUID.randomUUID();
        TaskEntity taskEntity2 = TaskEntity.builder()
                .id(taskId2)
                .content("laundry")
                .doneDate(null)
                .category("cleaning")
                .urgent(false)
                .build();
        TaskDto taskDto2 = TaskDto.builder()
                .id(taskId2)
                .content("laundry")
                .doneDate(null)
                .category("cleaning")
                .urgent(false)
                .build();
        List<TaskEntity> taskEntities = new ArrayList<>(
                Arrays.asList(taskEntity, taskEntity2)
        );
        List<TaskDto> expectedTaskDtos = new ArrayList<>(
                Arrays.asList(taskDto, taskDto2)
        );
        when(toDoListRepository.findAll()).thenReturn(taskEntities);

        // When
        List<TaskDto> response = toDoListService.getToDoList();

        // Then
        assertThat(response).isEqualTo(expectedTaskDtos);
    }

    @Test
    @DisplayName("should get one task")
    void should_get_a_task() throws TaskNotFoundException {
        // Given
        when(toDoListRepository.existsById(taskId)).thenReturn(true);
        when(toDoListRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        // When
        TaskDto response = toDoListService.getTask(taskId);

        // Then
        assertThat(response).isEqualTo(taskDto);
        assertThat(response).extracting(TaskDto::getId, TaskDto::getCategory, TaskDto::getContent, TaskDto::getDoneDate, TaskDto::isUrgent)
                .containsExactly(taskEntity.getId(), taskEntity.getCategory(), taskEntity.getContent(), taskEntity.getDoneDate(), taskEntity.isUrgent());
    }

    @Test
    @DisplayName("should delete a task")
    void should_delete_a_Task() throws TaskNotFoundException {
        // Given
        when(toDoListRepository.existsById(taskId)).thenReturn(true);

        // When
        toDoListService.deleteTask(taskId);

        // Then
        verify(toDoListRepository, times(1)).deleteById(taskId);
        assertThatCode(() -> toDoListService.deleteTask(taskId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should create a task")
    void should_create_a_task() {
        // Given
        when(toDoListRepository.save(taskEntity)).thenReturn(taskEntity);

        // When
        UUID response = toDoListService.createTask(taskDto);

        // Then
        assertThat(response).isEqualTo(taskDto.getId());
        verify(toDoListRepository, times(1)).save(taskEntity);

        // Verify that the taskEntity passed to the repository is the same as the service
        var taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(toDoListRepository).save(taskEntityArgumentCaptor.capture());
        var capturedTask = taskEntityArgumentCaptor.getValue();
        assertThat(capturedTask).isEqualTo(taskEntity);
    }

    @Test
    @DisplayName("should update a task")
    void should_update_a_task() throws TaskNotFoundException {
        // Given
        when(toDoListRepository.existsById(taskId)).thenReturn(true);
        when(toDoListRepository.save(taskEntity)).thenReturn(taskEntity);

        // Given
        toDoListService.updateTask(taskDto, taskId);

        // Then
        verify(toDoListRepository, times(1)).save(taskEntity);
        assertThat(taskDto).isNotNull().extracting(TaskDto::getId, TaskDto::getContent, TaskDto::getDoneDate, TaskDto::getCategory, TaskDto::isUrgent)
                .containsExactly(taskEntity.getId(), taskEntity.getContent(), taskEntity.getDoneDate(), taskEntity.getCategory(), taskEntity.isUrgent());
    }

    @Nested
    @DisplayName("Exceptions tests on service")
    class ToDoListServiceExceptionTest {

        @Test
        @DisplayName("should throw ZeroTaskFoundException when getting a todolist")
        void should_throw_zerotaskfoundexception_when_getting_a_todolist() {
            // When
            Throwable exception = assertThrows(ZeroTaskFoundException.class, () -> toDoListService.getToDoList());

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception).isExactlyInstanceOf(ZeroTaskFoundException.class);
            assertThat(exception.getMessage()).isEqualTo("Zero task found.");
        }

        @Test
        @DisplayName("should throw TaskNotFoundException when getting one task")
        void should_throw_task_not_found_exception_when_getting_a_task() {
            // When
            Throwable exception = assertThrows(TaskNotFoundException.class, () -> toDoListService.getTask(taskId));

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception).isExactlyInstanceOf(TaskNotFoundException.class);
            assertThat(exception.getMessage()).isEqualTo("Task with id : %s not found.", taskId);
        }

        @Test
        @DisplayName("should throw TaskNotFoundException when deleting task")
        void should_throw_task_not_found_exception_on_delete() {
            // Given
            when(toDoListRepository.existsById(taskId)).thenReturn(false);

            // When
            Throwable exception = assertThrows(TaskNotFoundException.class, () -> toDoListService.deleteTask(taskId));

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception).isExactlyInstanceOf(TaskNotFoundException.class);
            assertThat(exception.getMessage()).isEqualTo("Task with id : %s not found.", taskId);
        }

        @Test
        @DisplayName("should throw TaskNotFoundException when updating task")
        void should_throw_task_not_found_exception_on_update() {
            // Given
            when(toDoListRepository.existsById(taskId)).thenReturn(false);

            // When
            Throwable exception = assertThrows(TaskNotFoundException.class, () -> toDoListService.updateTask(taskDto, taskId));

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception).isExactlyInstanceOf(TaskNotFoundException.class);
            assertThat(exception.getMessage()).isEqualTo("Task with id : %s not found.", taskId);
        }

        @Test
        @DisplayName("should throws Task Already Exist exception when creating task")
        void should_throw_task_already_exists_exception_on_create() {

        }
    }
}
