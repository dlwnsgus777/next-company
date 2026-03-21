package com.company.model.kanban.command.dto

import jakarta.validation.constraints.NotEmpty

data class SaveKanbanColumnConfigRequest(
    @field:NotEmpty val columns: List<KanbanColumnDto>
)
