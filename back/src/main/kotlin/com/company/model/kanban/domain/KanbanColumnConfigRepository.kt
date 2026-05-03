package com.company.model.kanban.domain

import com.company.model.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface KanbanColumnConfigRepository : JpaRepository<KanbanColumnConfig, Long> {
    fun findTopByOrderByIdDesc(): KanbanColumnConfig?
    fun findByMember(member: Member): KanbanColumnConfig?
}
