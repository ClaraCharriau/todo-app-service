package com.project.todoapp.integration;

import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

public class DbSetupCommonOperations {

    public static final Operation DELETE_ALL =
            deleteAllFrom("Task");
}
