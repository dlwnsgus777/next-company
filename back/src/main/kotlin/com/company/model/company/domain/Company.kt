package com.company.model.company.domain

import com.company.config.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "company")
class Company(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var jobChangeStatus: JobChangeStatus = JobChangeStatus.NOT_APPLIED,

    @Column(columnDefinition = "TEXT")
    var memo: String? = null
) : BaseEntity()
