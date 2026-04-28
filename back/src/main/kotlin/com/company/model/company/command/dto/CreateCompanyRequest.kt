package com.company.model.company.command.dto

import com.company.model.company.domain.JobChangeStatus
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class CreateCompanyRequest(
    @field:NotBlank val name: String,
    val targetStatus: String = "O",
    val jobPostingUrl: String? = null,
    val recruitmentDeadline: LocalDate? = null,
    val jobChangeStatus: JobChangeStatus = JobChangeStatus.NOT_APPLIED,
    val scores: List<CompanyScoreRequest> = emptyList(),
    val memo: String? = null
)
