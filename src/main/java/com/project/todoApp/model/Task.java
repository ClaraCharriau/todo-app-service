package com.project.todoApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    private String content;

    private String category;

    private boolean urgent;

    private LocalDate doneDate;

}
