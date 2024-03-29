package com.project.todoapp.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TaskDto {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String content;

    @NotBlank
    private String category;

    private boolean urgent;

    private LocalDateTime doneDate;

}
