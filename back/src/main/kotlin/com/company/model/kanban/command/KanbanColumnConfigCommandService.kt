package com.company.model.kanban.command

import com.company.model.kanban.command.dto.SaveKanbanColumnConfigRequest
import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class KanbanColumnConfigCommandService(
    private val kanbanColumnConfigRepository: KanbanColumnConfigRepository,
    private val objectMapper: ObjectMapper
) {
    fun save(request: SaveKanbanColumnConfigRequest) {
        val columnsJson = objectMapper.writeValueAsString(request.columns)
        val existing = kanbanColumnConfigRepository.findTopByOrderByIdDesc()
        if (existing != null) {
            existing.columns = columnsJson
        } else {
            kanbanColumnConfigRepository.save(KanbanColumnConfig(columns = columnsJson))
        }
    }
}
