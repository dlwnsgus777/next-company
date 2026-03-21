package com.company.model.company.query.dto

import com.company.model.company.domain.JobChangeStatus
import com.company.model.company.domain.Company
import java.time.LocalDateTime

data class CompanyResponse(
    val id: Long,
    val name: String,
    val jobChangeStatus: JobChangeStatus,
    val memo: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(company: Company) = CompanyResponse(
            id = company.id,
            name = company.name,
            jobChangeStatus = company.jobChangeStatus,
            memo = company.memo,
            createdAt = company.createdAt
        )
    }
}
