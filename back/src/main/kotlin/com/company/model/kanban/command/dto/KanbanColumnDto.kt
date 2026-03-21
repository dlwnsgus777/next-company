package com.company.model.kanban.command.dto

import com.company.model.company.domain.JobChangeStatus

data class KanbanColumnDto(
    val id: String,
    val label: String,
    val statuses: List<JobChangeStatus>,
    val accentColor: String,
    val order: Int
)
