package com.company.model.kanban.query.dto

import com.company.model.kanban.command.dto.KanbanColumnDto

data class KanbanColumnConfigResponse(val columns: List<KanbanColumnDto>)
