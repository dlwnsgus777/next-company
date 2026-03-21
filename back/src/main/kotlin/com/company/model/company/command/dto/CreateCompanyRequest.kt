package com.company.model.company.command.dto

import com.company.model.company.domain.JobChangeStatus
import jakarta.validation.constraints.NotBlank

data class CreateCompanyRequest(
    @field:NotBlank val name: String,
    val jobChangeStatus: JobChangeStatus = JobChangeStatus.NOT_APPLIED,
    val memo: String? = null
)
