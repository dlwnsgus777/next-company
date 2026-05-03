package com.company.model.kanban.query

import com.company.model.kanban.command.dto.KanbanColumnDto
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.company.model.kanban.query.dto.KanbanColumnConfigResponse
import com.company.model.member.domain.Member
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class KanbanColumnConfigQueryService(
    private val kanbanColumnConfigRepository: KanbanColumnConfigRepository,
    private val objectMapper: ObjectMapper
) {
    fun get(member: Member): KanbanColumnConfigResponse? {
        val config = kanbanColumnConfigRepository.findByMember(member) ?: return null
        val columns: List<KanbanColumnDto> = objectMapper.readValue(
            config.columns,
            object : TypeReference<List<KanbanColumnDto>>() {}
        )
        return KanbanColumnConfigResponse(columns)
    }
}
