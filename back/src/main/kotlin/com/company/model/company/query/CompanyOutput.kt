package com.company.model.company.query

import com.company.model.company.domain.Company
import com.company.model.company.domain.JobChangeStatus
import java.time.LocalDateTime

data class CompanyOutput(
    val id: Long,
    val name: String,
    val jobChangeStatus: JobChangeStatus,
    val memo: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(company: Company) = CompanyOutput(
            id = company.id,
            name = company.name,
            jobChangeStatus = company.jobChangeStatus,
            memo = company.memo,
            createdAt = company.createdAt
        )
    }
}