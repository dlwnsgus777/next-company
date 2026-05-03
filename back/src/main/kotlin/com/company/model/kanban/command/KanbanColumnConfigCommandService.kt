package com.company.model.kanban.command

import com.company.model.kanban.command.dto.SaveKanbanColumnConfigRequest
import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.company.model.member.domain.Member
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class KanbanColumnConfigCommandService(
    private val kanbanColumnConfigRepository: KanbanColumnConfigRepository,
    private val objectMapper: ObjectMapper
) {
    fun save(member: Member, request: SaveKanbanColumnConfigRequest) {
        val columnsJson = objectMapper.writeValueAsString(request.columns)
        val existing = kanbanColumnConfigRepository.findByMember(member)
        if (existing != null) {
            existing.columns = columnsJson
        } else {
            kanbanColumnConfigRepository.save(KanbanColumnConfig(member = member, columns = columnsJson))
        }
    }
}
