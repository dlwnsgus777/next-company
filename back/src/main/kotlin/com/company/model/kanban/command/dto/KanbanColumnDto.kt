package com.company.model.kanban.command.dto

import com.company.model.company.domain.ApplicationStatus

data class KanbanColumnDto(
    val id: String,
    val label: String,
    val statuses: List<ApplicationStatus>,
    val accentColor: String,
    val order: Int
)
