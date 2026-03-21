package com.company.model.kanban.domain

import com.company.config.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "kanban_column_config")
class KanbanColumnConfig(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(columnDefinition = "TEXT", nullable = false)
    var columns: String
) : BaseEntity()
