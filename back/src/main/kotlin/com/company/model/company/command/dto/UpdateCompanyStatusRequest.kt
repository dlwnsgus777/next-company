package com.company.model.company.command.dto

import com.company.model.company.domain.JobChangeStatus

data class UpdateCompanyStatusRequest(val jobChangeStatus: JobChangeStatus)
