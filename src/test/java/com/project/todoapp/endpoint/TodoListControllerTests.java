package com.project.todoapp.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.service.ToDoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ToDoListController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Controller unit tests")
public class TodoListControllerTests {

    private MockMvc mockMvc;
    @MockBean
    private ToDoListService toDoListService;
    @InjectMocks
    private ToDoListController toDoListController;
    @InjectMocks
    private ObjectMapper objectMapper;
    public static final String PATH = "/todolist";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(toDoListController).build();
    }

    @Test
    @DisplayName("should status be bad request if the body is invalid on post")
    void shouldBadRequestBodyInvalidPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TaskDto.builder().build())))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("should status be bad request if the body is invalid on patch")
    void shouldBadRequestBodyInvalidPatch() throws Exception {
        // Given
        var uuid = UUID.randomUUID();
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.patch(PATH + "/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TaskDto.builder().id(uuid).build())))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}
