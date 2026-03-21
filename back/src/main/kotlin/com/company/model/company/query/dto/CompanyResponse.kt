package com.company.model.company.query.dto

import com.company.model.company.domain.ApplicationStatus
import com.company.model.company.domain.Company
import java.time.LocalDateTime

data class CompanyResponse(
    val id: Long,
    val name: String,
    val applicationStatus: ApplicationStatus,
    val memo: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(company: Company) = CompanyResponse(
            id = company.id,
            name = company.name,
            applicationStatus = company.applicationStatus,
            memo = company.memo,
            createdAt = company.createdAt
        )
    }
}
