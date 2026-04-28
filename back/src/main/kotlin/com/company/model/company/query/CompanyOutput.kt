package com.company.model.company.query

import com.company.model.company.query.dto.CompanyScoreResponse
import com.company.model.company.domain.Company
import com.company.model.company.domain.JobChangeStatus
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDate
import java.time.LocalDateTime

data class CompanyOutput(
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
        private val scoresType = object : TypeReference<List<CompanyScoreResponse>>() {}

        fun from(company: Company, objectMapper: ObjectMapper) = CompanyOutput(
            id = company.id,
            name = company.name,
            targetStatus = company.targetStatus,
            jobPostingUrl = company.jobPostingUrl,
            recruitmentDeadline = company.recruitmentDeadline,
            jobChangeStatus = company.jobChangeStatus,
            scores = objectMapper.readValue(company.scores, scoresType),
            memo = company.memo,
            createdAt = company.createdAt
        )
    }
}
