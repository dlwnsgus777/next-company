package com.company.model.company.query.dto

import com.company.model.company.domain.Company
import com.company.model.company.domain.JobChangeStatus
import com.company.model.company.query.CompanyOutput
import java.time.LocalDate
import java.time.LocalDateTime

data class CompanyResponse(
    val id: Long,
    val name: String,
    val targetStatus: String,
    val jobPostingUrl: String?,
    val recruitmentDeadline: LocalDate?,
    val jobChangeStatus: JobChangeStatus,
    val scores: List<CompanyScoreResponse>,
    val memo: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(company: Company) = CompanyResponse(
            id = company.id,
            name = company.name,
            targetStatus = company.targetStatus,
            jobPostingUrl = company.jobPostingUrl,
            recruitmentDeadline = company.recruitmentDeadline,
            jobChangeStatus = company.jobChangeStatus,
            scores = emptyList(),
            memo = company.memo,
            createdAt = company.createdAt
        )

        fun from(output: CompanyOutput) = CompanyResponse(
            id = output.id,
            name = output.name,
            targetStatus = output.targetStatus,
            jobPostingUrl = output.jobPostingUrl,
            recruitmentDeadline = output.recruitmentDeadline,
            jobChangeStatus = output.jobChangeStatus,
            scores = output.scores,
            memo = output.memo,
            createdAt = output.createdAt
        )
    }
}
