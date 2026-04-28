package com.company.model.company.command.dto

import com.company.model.company.domain.JobChangeStatus
import java.time.LocalDate

data class UpdateCompanyRequest(
    val name: String? = null,
    val targetStatus: String? = null,
    val jobPostingUrl: String? = null,
    val recruitmentDeadline: LocalDate? = null,
    val jobChangeStatus: JobChangeStatus? = null,
    val scores: List<CompanyScoreRequest>? = null,
    val memo: String? = null
)
