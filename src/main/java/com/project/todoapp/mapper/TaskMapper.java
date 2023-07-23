package com.project.todoapp.mapper;

import com.project.todoapp.dto.TaskDto;
import com.project.todoapp.model.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskEntity toEntity(TaskDto taskDto);

    TaskDto toDto(TaskEntity taskEntity);

    List<TaskDto> toDto(List<TaskEntity> taskEntities);
}
