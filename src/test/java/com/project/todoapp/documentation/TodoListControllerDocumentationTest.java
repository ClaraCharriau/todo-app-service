package com.project.todoapp.documentation;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.endpoint.ToDoListController;
import com.project.todoapp.service.ToDoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ToDoListController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Documentation test")
@AutoConfigureRestDocs
class TodoListControllerDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoListService toDoListService;

    private List<TaskDto> listTaskDto;

    private UUID uuidOne;

    private UUID uuidTwo;

    private TaskDto mockTaskDto;

    @BeforeEach
    void initMocks() {
        uuidOne = UUID.randomUUID();
        uuidTwo = UUID.randomUUID();

        mockTaskDto = TaskDto.builder().id(uuidOne).content("test").urgent(true).category("bills").doneDate(null).build();
        TaskDto secondMockTaskDto = TaskDto.builder().id(uuidTwo).content("test").urgent(true).category("bills").doneDate(null).build();

        listTaskDto = new ArrayList<>();
        listTaskDto.add(mockTaskDto);
        listTaskDto.add(secondMockTaskDto);
    }


    @Test
    @DisplayName("Document GET /todolist")
    void get_all_todos() throws Exception {

        given(toDoListService.getToDoList()).willReturn(listTaskDto);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/todolist"))
                .andExpect(status().isOk())
                .andDo(document("gettodolist",
                        responseFields(
                                fieldWithPath("[].id").description("the task id").type(JsonFieldType.STRING),
                                fieldWithPath("[].content").description("the task description").type(JsonFieldType.STRING),
                                fieldWithPath("[].category").description("the task category").type(JsonFieldType.STRING),
                                fieldWithPath("[].urgent").description("the task priority").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("[].doneDate").description("the done date").type(JsonFieldType.STRING).optional())));
    }

    @Test
    @DisplayName("Document GET /todolist/{id}")
    void get_task() throws Exception {

        given(toDoListService.getTask(uuidOne)).willReturn(mockTaskDto);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/todolist/{id}", uuidOne))
                .andExpect(status().isOk())
                .andDo(document("gettask",
                        pathParameters(
                                parameterWithName("id").description("ID of searched the task")),
                        responseFields(
                                fieldWithPath("id").description("the task id").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("the task description").type(JsonFieldType.STRING),
                                fieldWithPath("category").description("the task category").type(JsonFieldType.STRING),
                                fieldWithPath("urgent").description("the task priority").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("doneDate").description("the done date").type(JsonFieldType.STRING).optional())));

    }

    @Test
    @DisplayName("Document DELETE /todolist/{id}")
    void delete_task() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/todolist/{id}", uuidOne))
                .andExpect(status().isNoContent()).andDo(document("deletetask",
                        pathParameters(parameterWithName("id").description("ID of the task to get"))));
    }

    @Test
    @DisplayName("Document CREATE /todolist/")
    void create_task() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/todolist")
                .content("{\"id\":\"" + uuidOne + "\",\"content\":\"test\",\"category\":\"bills\",\"urgent\":true,\"doneDate\":null}")
                .contentType(APPLICATION_JSON)).andExpect(status().isCreated())
                .andDo(document("createtask",
                        requestFields(
                                fieldWithPath("id").description("the task id").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("the task description").type(JsonFieldType.STRING),
                                fieldWithPath("category").description("the task category").type(JsonFieldType.STRING),
                                fieldWithPath("urgent").description("the task priority").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("doneDate").description("the done date").type(JsonFieldType.STRING).optional())
        //,
                // TODO: redirect after creation
                //responseHeaders(
                //        headerWithName("location").description("The location of the new resource.")
                //)
        ));
    }

    @Test
    @DisplayName("Document UPDATE /todolist/{id}")
    void update_task() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/todolist/{id}", uuidOne)
                .content("{\"id\":\"" + uuidOne + "\",\"content\":\"test\",\"category\":\"bills\",\"urgent\":true,\"doneDate\":null}")
                .contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(document("updatetask",
                        pathParameters(
                                parameterWithName("id").description("ID of the task to get")),
                        requestFields(
                                fieldWithPath("id").description("the task id").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("the task description").type(JsonFieldType.STRING),
                                fieldWithPath("category").description("the task category").type(JsonFieldType.STRING),
                                fieldWithPath("urgent").description("the task priority").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("doneDate").description("the done date").type(JsonFieldType.STRING).optional())));
    }

}
