package com.company.model.company.command.dto

import com.company.model.company.domain.ApplicationStatus

data class UpdateCompanyRequest(
    val name: String? = null,
    val applicationStatus: ApplicationStatus? = null,
    val memo: String? = null
)
