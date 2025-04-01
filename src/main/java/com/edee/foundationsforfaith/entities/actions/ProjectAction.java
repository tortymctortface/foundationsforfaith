package com.edee.foundationsforfaith.entities.actions;

public sealed interface ProjectAction permits StartProject, PauseProject, CompleteProject {
    String projectName();
}
