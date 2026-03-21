package com.company.model.kanban.domain

import org.springframework.data.jpa.repository.JpaRepository

interface KanbanColumnConfigRepository : JpaRepository<KanbanColumnConfig, Long> {
    fun findTopByOrderByIdDesc(): KanbanColumnConfig?
}
