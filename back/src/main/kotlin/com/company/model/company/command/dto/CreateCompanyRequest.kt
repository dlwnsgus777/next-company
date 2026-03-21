package com.company.model.company.command.dto

import com.company.model.company.domain.ApplicationStatus
import jakarta.validation.constraints.NotBlank

data class CreateCompanyRequest(
    @field:NotBlank val name: String,
    val applicationStatus: ApplicationStatus = ApplicationStatus.NOT_APPLIED,
    val memo: String? = null
)
