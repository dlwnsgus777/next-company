package com.company.model.company.command.dto

data class CompanyScoreRequest(
    val criteriaId: String,
    val actualInfo: String = "",
    val score: Int = 0
)
