package com.company.model.kanban.domain

import com.company.config.entity.BaseEntity
import com.company.model.member.domain.Member
import jakarta.persistence.*

@Entity
@Table(
    name = "kanban_column_config",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_kanban_column_config_member", columnNames = ["member_id"])
    ]
)
class KanbanColumnConfig(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Column(columnDefinition = "TEXT", nullable = false)
    var columns: String
) : BaseEntity()
